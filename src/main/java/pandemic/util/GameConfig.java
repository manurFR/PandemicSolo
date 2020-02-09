/**
 *   Copyright (C) 2011 Emmanuel Bizieau <manur@manur.org>
 * 
 *   This file is part of PandemicSolo.
 *
 *   PandemicSolo is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   PandemicSolo is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with PandemicSolo.  If not, see <http://www.gnu.org/licenses/>.
 */
package pandemic.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pandemic.model.DifficultyLevel;
import pandemic.model.Disease;
import pandemic.model.Expansion;
import pandemic.model.Variant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static pandemic.model.Expansion.CORE;
import static pandemic.model.Expansion.ON_THE_BRINK;
import static pandemic.model.Variant.MUTATION;
import static pandemic.model.Variant.WORLDWIDE_PANIC;

/**
 * This class hold the game properties (number of players/roles, number of epidemics...)
 * 
 * @author manur
 * @since v2.6
 */
public class GameConfig implements Serializable {
	private static final long serialVersionUID = 28L;
	
	private static final Logger logger = LoggerFactory.getLogger(GameConfig.class);
	
	private static final String YES	= "Yes";
	private static final String NO = "No";
	
	private DifficultyLevel difficultyLevel;
	private int nbOfRoles;
	private Set<Expansion> rolesExpansions = new LinkedHashSet<Expansion>();
	private boolean fiveEvents;
	private Set<Expansion> eventCardsExpansions = new LinkedHashSet<Expansion>();
	private Set<Variant> variants = new LinkedHashSet<Variant>();
	private boolean survivalMode;
	
	/**
	 * Log the details of this game configuration through the Logger
	 */
	public void log() {
		for(String line : giveDetails()) {
			logger.info(line);
		}
	}
	
	public String[] giveDetails() {
		String[] details = new String[7];
		
		StringBuilder sb = new StringBuilder("Number of roles : ");
		sb.append(nbOfRoles);
		details[0] = sb.toString();

		sb = new StringBuilder("Roles used : ");
		List<String> rolesFrom = new ArrayList<String>();
		for (Expansion expansion: rolesExpansions) {
			rolesFrom.add(expansion.getLabel());
		}
		if (rolesFrom.isEmpty()) {
			rolesFrom.add("None");
		}
		int nbExpansionsRoles = rolesFrom.size();
		for (int i=0; i<nbExpansionsRoles; i++) {
			sb.append(rolesFrom.get(i));
			if (i<nbExpansionsRoles-1) {
				sb.append(" | ");
			}
		}
		details[1] = sb.toString();

		sb = new StringBuilder("Difficulty level :");
		sb.append(difficultyLevel);
		details[2] = sb.toString();

		sb = new StringBuilder("Number of event cards : ");
		sb.append(fiveEvents ? "Always 5" : "2 per player");
		details[3] = sb.toString();

		sb = new StringBuilder("Event Cards used : ");
		List<String> eventCards = new ArrayList<String>();
		for (Expansion expansion: eventCardsExpansions) {
			eventCards.add(expansion.getLabel());
		}
		if (eventCards.isEmpty()) {
			eventCards.add("None");
		}
		int nbExpansionsEventCards = eventCards.size();
		for (int i=0; i<nbExpansionsEventCards; i++) {
			sb.append(eventCards.get(i));
			if (i<nbExpansionsEventCards-1) {
				sb.append(" | ");
			}
		}
		details[4] = sb.toString();

		sb = new StringBuilder("Survival Mode ? ");
		sb.append(survivalMode ? YES : NO);
		details[5] = sb.toString();

		sb = new StringBuilder("Variants used : ");
		List<String> variantsUsed = new ArrayList<String>();
		for (Variant variant: variants) {
			variantsUsed.add(variant.getLabel());
		}
		if (variantsUsed.isEmpty()) {
			variantsUsed.add("None");
		}
		int nbVariants = variantsUsed.size();
		for (int i=0; i<nbVariants; i++) {
			sb.append(variantsUsed.get(i));
			if (i<nbVariants-1) {
				sb.append(" | ");
			}
		}
		details[6] = sb.toString();
		
		return details;
	}
	
	/**
	 * Build the "default" game configuration, ie the default selections
	 * in the "New game..." config dialog box.
	 * @return The prepared GameConfig
	 */
	public static GameConfig defaultConfigFactory() {
		GameConfig config = new GameConfig();
		
		config.setDifficultyLevel(DifficultyLevel.NORMAL);
		config.setNbOfRoles(2);
		config.getRolesExpansions().addAll(asList(CORE, ON_THE_BRINK));
		config.setFiveEvents(false);
		config.getEventCardsExpansions().addAll(asList(CORE, ON_THE_BRINK));
		config.getVariants().clear();
		config.setSurvivalMode(false);

		return config;
	}
	
	/**
	 * Returns an array with the diseases (colors) used when playing with this config.
	 * Regular game uses only blue, yellow, black and red. When using the Mutation/Worldwide Panic variants,
	 * one adds purple.
	 * @return An array of the diseases used by this config
	 */
	public Disease[] getDiseases() {
		if (variants.contains(MUTATION) || variants.contains(WORLDWIDE_PANIC)) {
			return Disease.values();
		} else {
			return new Disease[] { Disease.BLUE, Disease.YELLOW, Disease.BLACK, Disease.RED };
		}
	}

	/**
	 * Returns the number of cubes to prepare for the color, depending on the variants used.
	 */
	public int numberOfCubes(Disease color) {
		switch (color) {
			case BLUE:
			case RED:
			case BLACK:
			case YELLOW:
				return 24;
			case PURPLE:
				if (variants.contains(WORLDWIDE_PANIC)) {
					return 24;
				} else if (variants.contains(MUTATION)) {
					return 12;
				}
			default:
				return 0;
		}
	}

	/* -------------------------------------------------------------------------------------------- */

	public int getNbOfEpidemics() {
		return difficultyLevel.getLevel();
	}

	public DifficultyLevel getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	public int getNbOfRoles() {
		return nbOfRoles;
	}

	public void setNbOfRoles(int nbOfRoles) {
		this.nbOfRoles = nbOfRoles;
	}

	public Set<Expansion> getRolesExpansions() {
		return rolesExpansions;
	}

	public void setRolesExpansions(Set<Expansion> rolesExpansions) {
		this.rolesExpansions = rolesExpansions;
	}

	public boolean isFiveEvents() {
		return fiveEvents;
	}

	public void setFiveEvents(boolean fiveEvents) {
		this.fiveEvents = fiveEvents;
	}

	public Set<Expansion> getEventCardsExpansions() {
		return eventCardsExpansions;
	}

	public void setEventCardsExpansions(Set<Expansion> eventCardsExpansions) {
		this.eventCardsExpansions = eventCardsExpansions;
	}

	public Set<Variant> getVariants() {
		return variants;
	}

	public void setVariants(Set<Variant> variants) {
		this.variants = variants;
	}

	public boolean isSurvivalMode() {
		return survivalMode;
	}

	public void setSurvivalMode(boolean survivalMode) {
		this.survivalMode = survivalMode;
	}
}

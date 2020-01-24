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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pandemic.model.DifficultyLevel;
import pandemic.model.Disease;

/**
 * This class hold the game properties (number of players/roles, number of epidemics...)
 * 
 * @author manur
 * @since v2.6
 */
public class GameConfig implements Serializable {
	/*
	 * WARNING - Using this randomly generated UID was a bad idea.
	 * During the next change to this class, please modify the value to
	 * the new version number ; example :
	 *  private static final long serialVersionUID = 28L; // last major change : v2.8
	 */
	private static final long serialVersionUID = -583917870933664080L;
	
	private static final Logger logger = LoggerFactory.getLogger(GameConfig.class);
	
	private static final String YES	= "Yes";
	private static final String NO = "No";
	
	private DifficultyLevel difficultyLevel;
	private int nbOfRoles;
	private boolean useAllRoles;
	private boolean useRevisedOperationsExpert;
	private boolean eventsCore;
	private boolean eventsOnTheBrink;
	private boolean playVirulentStrain;
	private boolean playMutation;
	
	/**
	 * Log the details of this game configuration through the Logger
	 */
	public void log() {
		for(String line : giveDetails())
		{
			logger.info(line);
		}
	}
	
	public String[] giveDetails() {
		String[] details = new String[5];
		
		StringBuilder sb = new StringBuilder("Number of roles : ");
		sb.append(nbOfRoles);
		sb.append(" with new roles ? ");
		sb.append(useAllRoles ? YES : NO);
		sb.append(" (with ");
		sb.append(useRevisedOperationsExpert ? "revised" : "basic");
		sb.append(" Operations Expert)");
		details[0] = sb.toString();
				
		sb = new StringBuilder("Difficulty level :");
		sb.append(difficultyLevel);
		details[1] = sb.toString();
		
		sb = new StringBuilder("Event Cards used : ");
		List<String> eventCards = new ArrayList<String>();
		if (eventsCore) {
			eventCards.add("Core");
		}
		if (eventsOnTheBrink) {
			eventCards.add("On The Brink");
		}
		if (eventCards.isEmpty()) {
			eventCards.add("None");
		}
		int nbSets = eventCards.size();
		for (int i=0; i<nbSets; i++) {
			sb.append(eventCards.get(i));
			if (i<nbSets-1) {
				sb.append(" | ");
			}
		}
		details[2] = sb.toString();

		sb = new StringBuilder("Playing Virulent Strain expansion ? ");
		sb.append(playVirulentStrain ? YES : NO);
		details[3] = sb.toString();

		sb = new StringBuilder("Playing Mutation expansion ? ");
		sb.append(playMutation ? YES : NO);
		details[4] = sb.toString();
		
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
		config.setUseAllRoles(true);
		config.setUseRevisedOperationsExpert(true);
		config.setEventsCore(true);
		config.setEventsOnTheBrink(true);
		config.setPlayVirulentStrain(false);
		config.setPlayMutation(false);
		
		return config;
	}
	
	/**
	 * Returns an array with the diseases (colors) used when playing with this config.
	 * Regular game uses only blue, yellow, black and red. When using the Mutation variant,
	 * one adds purple.
	 * @return An array of the diseases used by this config
	 */
	public Disease[] getDiseases() {
		if (playMutation) {
			return Disease.values();
		} else {
			return new Disease[] { Disease.BLUE, Disease.YELLOW, Disease.BLACK, Disease.RED };
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

	public boolean isUseAllRoles() {
		return useAllRoles;
	}

	public void setUseAllRoles(boolean useAllRoles) {
		this.useAllRoles = useAllRoles;
	}
	
	public boolean isUseRevisedOperationsExpert() {
		return useRevisedOperationsExpert;
	}

	public void setUseRevisedOperationsExpert(boolean useRevisedOperationsExpert) {
		this.useRevisedOperationsExpert = useRevisedOperationsExpert;
	}

	public boolean isEventsCore() {
		return eventsCore;
	}

	public void setEventsCore(boolean eventsCore) {
		this.eventsCore = eventsCore;
	}

	public boolean isEventsOnTheBrink() {
		return eventsOnTheBrink;
	}

	public void setEventsOnTheBrink(boolean eventsOnTheBrink) {
		this.eventsOnTheBrink = eventsOnTheBrink;
	}

	public boolean isPlayVirulentStrain() {
		return playVirulentStrain;
	}

	public void setPlayVirulentStrain(boolean playVirulentStrain) {
		this.playVirulentStrain = playVirulentStrain;
	}

	public boolean isPlayMutation() {
		return playMutation;
	}

	public void setPlayMutation(boolean playMutation) {
		this.playMutation = playMutation;
	}
}

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
package pandemic.model;

/**
 * Possible difficulty levels, and the number of epidemic cards corresponding to each.
 * 
 * @author manur
 * @since v2.6
 */
public enum DifficultyLevel {
	INTRODUCTORY (4, "Introductory"),
	NORMAL		 (5, "Normal"),
	HEROIC		 (6, "Heroic"),
	LEGENDARY	 (7, "Legendary");
		
	private int level;
	private String description;
	
	private DifficultyLevel(int level, String description) {
		this.level = level;
		this.description = description;
	}
	
	public int getLevel() {
		return level;
	}

	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		return "'" + description + "' - " + level + " Epidemics";
	}
	
}

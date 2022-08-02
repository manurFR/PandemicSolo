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
package pandemic;

import org.junit.Test;
import pandemic.model.DifficultyLevel;
import pandemic.model.Variant;
import pandemic.util.GameConfig;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;


/** 
* @author manur
* @since v2.7
*/
public class TestGameManager {

	@Test
	public void testConstructMainTitle() {
		GameManager gameManager = new GameManager();
		
		String expected = "Pandemic Solitaire  version " + PandemicSolo.VERSION + "  -  Difficulty level:  'Heroic' - 6 Epidemics + Mutation";
		
		GameConfig config = new GameConfig();
		config.setDifficultyLevel(DifficultyLevel.HEROIC);
		config.getVariants().add(Variant.MUTATION);

		assertEquals(expected, gameManager.constructMainTitle(config));

		expected = "Pandemic Solitaire  version " + PandemicSolo.VERSION + "  -  Difficulty level:  'Normal' - 5 Epidemics + Virulent Strain! + Quarantines";
		
		config.setDifficultyLevel(DifficultyLevel.NORMAL);
		config.getVariants().clear();
		config.getVariants().addAll(asList(Variant.VIRULENT_STRAIN, Variant.QUARANTINES));

		assertEquals(expected, gameManager.constructMainTitle(config));
	}

}

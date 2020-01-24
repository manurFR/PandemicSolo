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
package pandemic.configuration;

import pandemic.util.GameConfig;
import pandemic.util.ResourceProvider;

/**
 * This class gives back a default game configuration, without the user's input
 * 
 * @author manur
 * @since v2.7
 */
public class DefaultConfigBuilder implements ConfigBuilder {

	/**
	 * Returns the default configuration, as implemented in the class GameConfig
	 * @param resourceProvider Resource provider not used here
	 */
	@Override
	public GameConfig queryGameConfig(ResourceProvider resourceProvider) {
		return GameConfig.defaultConfigFactory();
	}

}

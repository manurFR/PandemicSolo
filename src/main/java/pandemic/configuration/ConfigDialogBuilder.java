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

import java.awt.Frame;

import javax.swing.JDialog;

import pandemic.dialog.ConfigDialog;
import pandemic.util.GameConfig;
import pandemic.util.ResourceProvider;

/**
 * This dialog asks the user for all game configuration information at once.
 * It returns a fully prepared GameConfig object, ready to be used as a new game.
 * 
 * @author manur
 * @since v2.7
 */
public class ConfigDialogBuilder implements ConfigBuilder {
	
	private Frame ownerFrame;
	
	/**
	 * Constructor
	 * @param ownerFrame
	 */
	public ConfigDialogBuilder(Frame ownerFrame) {
		this.ownerFrame = ownerFrame;
	}

	/**
	 * Prepares the dialog and display it.
	 * @param resourceProvider The ResourceProvider that can be queried to get icons
	 * @return The prepared GameConfig filled with the user's choices
	 */
	@Override
	public GameConfig queryGameConfig(ResourceProvider resourceProvider) {
		GameConfig config = GameConfig.defaultConfigFactory();
		
		ConfigDialog dialog = new ConfigDialog(ownerFrame);
		dialog.viewCreateComponents(resourceProvider);
		dialog.setModelConfig(config);
		dialog.viewRefresh();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);

		return dialog.getModelConfig();
	}


}

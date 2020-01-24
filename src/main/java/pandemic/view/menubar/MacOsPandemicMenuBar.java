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
package pandemic.view.menubar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import pandemic.GameManager;
import pandemic.dialog.AboutBox;
import pandemic.util.ResourceProvider;

import com.apple.eawt.AboutHandler;
import com.apple.eawt.AppEvent.AboutEvent;
import com.apple.eawt.Application;

/**
 * This is the class to customize the menu bar elements for
 * Mac OS X systems. It contains fairly specific code.
 * 
 * http://www.devdaily.com/apple/mac/java-mac-native-look/java-on-mac.shtml
 * 
 * @author manur
 * @since v2.7
 */
public class MacOsPandemicMenuBar extends PandemicMenuBar {
	
	private Application macApplication;

	public MacOsPandemicMenuBar() {
		macApplication = Application.getApplication();
	}


	@Override
	void constructQuitItem(JMenu menuFile) {
		// Stub. There's nothing to do here as it will be managed automatically by Mac OS X.
	}


	/**
	 * Build the "?" menu for Mac OS platform.
	 * For now, there's only the About... item in the "?" menu, and it's taken care of
	 * differently on Mac OS X. So we don't create the "?" menu, but activate the system
	 * About... instead.
	 * @param menuBar This menu will be added to the given JMenuBar
	 * @param gameManager GameManager for the whole session
	 * @param resourceProvider The ResourceProvider that will be queried to get the icons
	 */
	@Override
	void createWhatMenu(JMenuBar menuBar, final GameManager gameManager, final ResourceProvider resourceProvider) {
		macApplication.setAboutHandler(new AboutHandler() {
			@Override
			public void handleAbout(AboutEvent event) {
				AboutBox about = new AboutBox(owner);
				about.setGameManager(gameManager);
				about.createComponents(resourceProvider);
				about.setVisible(true);
			}
		});
	}

}

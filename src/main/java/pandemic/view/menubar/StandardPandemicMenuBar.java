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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import pandemic.GameManager;
import pandemic.dialog.AboutBox;
import pandemic.util.ResourceProvider;

/**
 * This is the standard way to create the menu bar elements, featuring only
 * classic Java JMenuBar operations.
 * 
 * @author manur
 * @since v2.7
 */
public class StandardPandemicMenuBar extends PandemicMenuBar {

	/**
	 * Build the "Exit" menu item for standard platforms
	 * @param menuFile This menu item will be attached to the given JMenu
	 */
	@Override
	void constructQuitItem(JMenu menuFile) {
		JMenuItem mntmQuitPandemicsolo = new JMenuItem("Exit PandemicSolo");
		mntmQuitPandemicsolo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		menuFile.add(mntmQuitPandemicsolo);
	}

	/**
	 * Build the "?" menu for standard platforms
	 * @param menuBar This menu will be added to the given JMenuBar
	 * @param gameManager GameManager for the whole session
	 * @param resourceProvider The ResourceProvider that will be queried to get the icons
	 */
	@Override
	void createWhatMenu(JMenuBar menuBar, final GameManager gameManager, final ResourceProvider resourceProvider) {
		JMenu menuWhat = new JMenu("?");

		JMenuItem mntmAbout = new JMenuItem("About...");
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AboutBox about = new AboutBox(owner);
				about.setGameManager(gameManager);
				about.createComponents(resourceProvider);
				about.setVisible(true);
			}
		});
		menuWhat.add(mntmAbout);

		menuBar.add(menuWhat);
	}

}

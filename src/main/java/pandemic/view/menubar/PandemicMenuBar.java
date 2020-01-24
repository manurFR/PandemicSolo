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

import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import pandemic.GameManager;
import pandemic.util.ResourceProvider;

/**
 * This class guides the construct of the menu bar.
 * Different OS Look&Feels will extend this abstract class to customize the menu bar.
 * 
 * @author manur
 * @since v2.7
 */
public abstract class PandemicMenuBar {

	protected final int systemKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	
	protected Frame owner;
	
	/**
	 * Template method.
	 * Builds and returns the menu bar, making calls to abstract methods and hooks.
	 * @param owner The window to which the menuBar will be attached
	 * @param gameManager The object the menu items will call to be in charge of operations
	 * @param resourceProvider The ResourceProvider that will be queried to get the icons
	 * @return The completed JMenuBar
	 */
	public JMenuBar constructMenuBar(Frame owner, GameManager gameManager, ResourceProvider resourceProvider) {
		this.owner = owner;
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menuFile = createFileMenu(gameManager);
		constructQuitItem(menuFile);
		menuBar.add(menuFile);
		
		createWhatMenu(menuBar, gameManager, resourceProvider);
		
		return menuBar;
	}
	
	/**
	 * Creates the File menu.
	 * Some elements may be built in different ways depending of the OS
	 * @param gameManager The GameManager object that will take care of the operations launched by the menu
	 * @return The completed JMenu
	 */
	protected JMenu createFileMenu(final GameManager gameManager) {
		JMenu menuFile = new JMenu("File");
		
		JMenuItem mntmNewGame = new JMenuItem("New game...");
		mntmNewGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameManager.launchNewGame();
			}
		});
		menuFile.add(mntmNewGame);
		
		menuFile.addSeparator();
		
		JMenuItem mntmSave = new JMenuItem("Save...");
		mntmSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameManager.saveGame();
			}
		});
		menuFile.add(mntmSave);
		
		JMenuItem mntmLoad = new JMenuItem("Load...");
		mntmLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameManager.loadGame();
			}
		});
		menuFile.add(mntmLoad);
		
		return menuFile;
	}
	
	abstract void constructQuitItem(JMenu menuFile);
	
	abstract void createWhatMenu(JMenuBar menuBar, GameManager gameManager, ResourceProvider resourceProvider);
}

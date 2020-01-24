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
package pandemic.dialog;

import java.awt.Container;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import pandemic.GameManager;
import pandemic.util.ResourceProvider;

/**
 * The responsibility of this class is to display the dialogs of the application. 
 *  
 * @author manur
 * @since v2.6
 */
public class ModalDialogsManager implements DialogsManager {
	
	private Container container;
	private ResourceProvider resourceProvider;
	private JFileChooser fileChooser;
	
	/**
	 * Constructor
	 * @param container The container inside which the dialogs will be displayed
	 * @param resourceProvider The ResourceProvider that can be queried to get icons and other resources
	 */
	public ModalDialogsManager(Container container, ResourceProvider resourceProvider, JFileChooser fileChooser) {
		this.container = container;
		this.resourceProvider = resourceProvider;
		this.fileChooser = fileChooser;
	}
	
	/**
	 * Just show an "OK" message dialog with the alert.
	 * @param alertMessage The message in the body of the dialog
	 */
	@Override
	public void showAlert(String alertMessage) {
		JOptionPane.showMessageDialog(container, alertMessage);
	}

	/**
	 * Ask the user for a choice between an array of Strings.
	 * @param title The displayed title of the dialog box
	 * @param message The displayed message on the body of the dialog box, explaining the choice
	 * @param selectionValues An array containing all possible choices (one element per choice)
	 * @param defaultValueIndex The index of the choice initially selected in the array selectionValues
	 * @param imageName The name of the decorating icon to place on the dialog box
	 * @return null if the user clicks the Cancel button ; the chosen String from the selectionValues array otherwise
	 */
	@Override
	public String chooseFromStrings(String title, String message, String[] selectionValues, int defaultValueIndex, String imageName) {
		if (defaultValueIndex<0 || defaultValueIndex>=selectionValues.length) {
			throw new IndexOutOfBoundsException();
		}

		String input = (String) JOptionPane.showInputDialog(
				container, message, title, JOptionPane.QUESTION_MESSAGE, 
				resourceProvider.getIcon(imageName), selectionValues, selectionValues[defaultValueIndex]);
		
		return input;
	}

	/**
	 * Ask the user for a confirmation
	 * @param title The displayed title of the dialog box
	 * @param message The displayed message, explaining the option to confirm
	 * @return true if the user confirms, false otherwise
	 */
	@Override
	public boolean confirm(String title, String message) {
		int reply = JOptionPane.showConfirmDialog(container, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		return reply == JOptionPane.YES_OPTION;
	}

	/**
	 * Ask the user to choose a file on the filesystem.
	 * @param title The displayed title in the dialog box
	 * @param approveButtonText The text to display on the action button (eg "Save"...)
	 * @return the selected file, or null if the user cancels
	 */
	@Override
	public File selectFile(String title, String approveButtonText) {
		fileChooser.setDialogTitle(title);
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "PandemicSolo save file";
			}
			
			@Override
			public boolean accept(File f) {
			    if (f.isDirectory()) {
			        return true;
			    }
			    
				String name = f.getName();
				int dot = name.lastIndexOf('.');
				
				String extension = "";
				if (dot > 0 && dot < name.length() - 1) {
					extension = name.substring(dot+1).toLowerCase();
				}
				
				return (extension.equals(GameManager.SAVEFILE_EXTENSION));
			}
		});
		
		if (fileChooser.showDialog(container, approveButtonText) == JFileChooser.CANCEL_OPTION) {
			return null;
		}
		
		return fileChooser.getSelectedFile();
	}
}

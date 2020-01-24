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

import java.io.File;

/**
 * Interface for the class used to display dialogs
 *  
 * @author manur
 * @since v2.6
 */
public interface DialogsManager {
	public void showAlert(String alertMessage);
	public String chooseFromStrings(String title, String message, String[] selectionValues, int defaultValueIndex, String imageName);
	public boolean confirm(String title, String message);
	public File selectFile(String title, String approveButtonText);
}

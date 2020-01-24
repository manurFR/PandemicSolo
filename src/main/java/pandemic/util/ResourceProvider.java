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

import java.io.InputStream;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

/**
 * Interface for providers fetching resources, such as images, 
 * for the rest of the application.
 * 
 * @author manur
 * @since v2.6.1
 */
public interface ResourceProvider {

	public ImageIcon getIcon(String imageName);
	public InputStream getAudioStream(String fileName);
	
	public ResourceBundle getBundle(String fileName);
	
}

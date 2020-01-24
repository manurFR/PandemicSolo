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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ResourceProvider that can handle both fetching resources
 * from a JAR file when the application is running from such a 
 * packaged released, and fetching from the filesystem when it's
 * running non-packaged.
 * 
 * @author manur
 * @since v2.6.1
 */
public class GenericResourceProvider implements ResourceProvider {

	private static final Logger logger = LoggerFactory.getLogger(GenericResourceProvider.class);
	
	public static final String IMAGE_PREFIX = "images";
	public static final String SOUND_PREFIX = "wavs";
	
	/**
	 * Utility method to create an ImageIcon from its file name.
	 * When the application is packaged in a JAR, we have to access
	 * resource files through Class.getResource(), which can only
	 * access the classpath. And we need to prefix the resource file
	 * name with the "absolute" path to where it is stocked -- 
	 * "absolute" here only means relative to the root of the jarfile 
	 * and with a "/" at the beginning.
	 * If it doesn't get the resource, then we're not running from a JAR, 
	 * and we step back to fetching the resource from the filesystem, with
	 * new ImageIcon(String) and a relative path (no beginning "/").
	 * 
	 * @param imageName Simple file name of the resource to be made to an icon (eg: "icon.jpg")
	 * @return The prepared ImageIcon with the image file loaded
	 */
	@Override
	public ImageIcon getIcon(String imageName) {
		if (imageName == null) {
			logger.trace("getIcon(null) : return new ImageIcon()");
			return new ImageIcon();
		}
		
		String imagePath = null;
		
		// Look for the resource if we're in a JAR ("absolute" path)
		if (!imageName.startsWith("/" + IMAGE_PREFIX)) {
			imagePath = "/" + IMAGE_PREFIX + "/" + imageName;
		}
		URL resourceURL = this.getClass().getResource(imagePath);
		
		if (resourceURL != null) {
			logger.trace("getIcon(" + imageName + ") : resource found in the classpath (JAR)");
			return new ImageIcon(resourceURL);
		}
		
		// If no URL was found, we're not JAR-Packaged, so we look 
		//  for the resource in the filesystem
		logger.trace("getIcon(" + imageName + ") : resource found on the filesystem");
		imagePath = IMAGE_PREFIX + "/" + imageName;
		return new ImageIcon(imagePath);
	}
	
	/**
	 * Utility method to open an InputStream on a file with
	 * a given name.
	 * We manage two types of resource access : if the 
	 * application is running from a JAR, we need to use 
	 * Class.getResourceAsStream() and to look for the resource
	 * in its "absolute" path, meaning the root of the jarfile
	 * and a slash "/" at the beginning.
	 * If the application is not running packaged from a JAR,
	 * we have to look for the resource in the filesystem with
	 * a new FileInputStream and no slash at the beginning of
	 * the path.
	 * @param fileName Simple file name of the resource to open a Stream on.
	 * @return The opened InputStream on the resource, null if it wasn't found.
	 */
	@Override
	public InputStream getAudioStream(String fileName) {
		if (fileName == null) {
			return null;
		}
		
		String filePath = null;
		
		// Look for the resource if we're in a JAR ("absolute" path)
		if (!fileName.startsWith("/" + SOUND_PREFIX)) {
			filePath = "/" + SOUND_PREFIX + "/" + fileName;
		}
		InputStream resourceStream = this.getClass().getResourceAsStream(filePath);
		
		if (resourceStream != null) {
			return resourceStream;
		}
		
		// If no Stream was produced, we're not JAR-Packaged, so we look 
		//  for the resource in the filesystem
		filePath = SOUND_PREFIX + "/" + fileName;
		try {
			return new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * Returns the default ResourceBundle of the given fileName.
	 * Search in the ClassLoader resources is automatically performed
	 * in ResourceBundle.getBundle().
	 * @param fileName The base name of the bundle, without Locale 
	 * 				   informations and the .properties extension
	 * @return The ResourceBundle if it was found
	 * @throws MissingResourceException - 
	 * 		if no resource bundle for the specified base name can be found
	 */
	@Override
	public ResourceBundle getBundle(String fileName) {
		return ResourceBundle.getBundle(fileName);
	}

}

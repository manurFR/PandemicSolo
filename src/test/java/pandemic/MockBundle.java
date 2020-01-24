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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Mock ResourceBundle where the getString() method will return 
 *   the value corresponding to a key/value pair set programmatically
 *   and not via a file.
 * Important Note : if a key doesn't exist, the getString() method will
 *   return the value of the first pair (set in the constructor).
 *   
 * @author manur
 * @since v2.6
 */
class MockBundle extends ResourceBundle {
	private HashMap<String, String> properties;
	private String defaultValue;
	
	public MockBundle(String key, String value) {
		properties = new HashMap<String, String>();
		properties.put(key, value);
		this.defaultValue = value;
	}
	
	public void addKV(String key, String value) {
		properties.put(key, value);
	}
	
	/* Empty abstract method we're bound to implement */ 
	@Override
	public Enumeration<String> getKeys() {
		return null;
	}

	@Override
	protected Object handleGetObject(String key) {
		if (properties.containsKey(key)) {
			return properties.get(key);
		} else {
			return defaultValue;
		}
	}
	
	@Override
	public boolean containsKey(String key) {
		return true;
	}
}

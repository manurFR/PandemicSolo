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
package pandemic.model.objects;

import javax.swing.ImageIcon;

import pandemic.model.BoardZone;
import pandemic.model.Disease;

/**
 * @author manur 
 * @since v2.6
 */
public class Cube extends PandemicObject {
	/*
	 * WARNING - Using this randomly generated UID was a bad idea.
	 * During the next change to this class, please modify the value to
	 * the new version number ; example :
	 *  private static final long serialVersionUID = 28L; // last major change : v2.8
	 */
	private static final long serialVersionUID = -2803435415972087322L;

	private Disease color;
	
	public Cube(int id, ImageIcon imageIcon, int x, int y, Disease color, BoardZone boardZone) {
		super(PandemicObject.Type.CUBE, id, imageIcon, x, y, boardZone);
		this.color = color;
	}
	
	public Disease getColor() {
		return color;
	}
	
}

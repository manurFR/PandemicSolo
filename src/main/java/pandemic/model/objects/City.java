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

import pandemic.model.BoardZone;
import pandemic.model.Disease;

/**
 * A City component.
 * Useful to keep track of the coordinates and color of each city on the board.
 * 
 * @author manur
 * @since v2.6
 */
public class City extends PandemicObject {
	/*
	 * WARNING - Using this randomly generated UID was a bad idea.
	 * During the next change to this class, please modify the value to
	 * the new version number ; example :
	 *  private static final long serialVersionUID = 28L; // last major change : v2.8
	 */
	private static final long serialVersionUID = -9129946583906633152L;

	private Disease color;
	
	/**
	 * Constructor.
	 * City is the only Component child which is not draggable. We take advantage of this fact 
	 * by putting the field protected in Component, so we access it directly here. 
	 * This way we don't have to offer a public setter.
	 * The boardZone is BOARD by default and not modiable.
	 * @param id
	 * @param name
	 * @param x
	 * @param y
	 * @param color
	 */
	public City(int id, String name,int x, int y, Disease color) {
		super(PandemicObject.Type.CITY, id, name, null, x, y, BoardZone.BOARD);
		this.color = color;
		this.draggable = false;
	}
	
	public Disease getColor() {
		return color;
	}

	public void setBoardZone(BoardZone boardZone) {
		throw new UnsupportedOperationException("Can't move a City outside of the board.");
	}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("City [toString()=").append(super.toString()).append(", color=").append(color).append("]");
        return builder.toString();
    }
}

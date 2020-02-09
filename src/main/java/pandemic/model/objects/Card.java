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

/**
 * A card
 * An optional "city" field is useful for player city cards and infection city cards.
 * 
 * @author manur
 * @since v2.6
 */
public class Card extends PandemicObject {
	/*
	 * WARNING - Using this randomly generated UID was a bad idea.
	 * During the next change to this class, please modify the value to
	 * the new version number ; example :
	 *  private static final long serialVersionUID = 28L; // last major change : v2.8
	 */
	private static final long serialVersionUID = 7296482810949651823L;

	private City city;

	/**
	 * Minimal constructor, with the same signature as the parent class Component
	 * @param type
	 * @param id
	 * @param name
	 * @param imageIcon
	 * @param x
	 * @param y
	 */
	public Card(Type type, int id, String name, ImageIcon imageIcon, int x,	int y, BoardZone boardZone) {
		super(type, id, name, imageIcon, x, y, boardZone);
	}
	
	/**
	 * Constructor for player and infection city cards, including an added "city" argument
	 * @param type
	 * @param id
	 * @param name
	 * @param imageIcon
	 * @param x
	 * @param y
	 * @param city
	 */
	public Card(Type type, int id, String name, ImageIcon imageIcon, int x,	int y, City city, BoardZone boardZone) {
		this(type, id, name, imageIcon, x, y, boardZone);
		this.city = city;
	}

	public City getCity() {
		return city;
	}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Card [");
        if (super.toString() != null) {
			builder.append("toString()=").append(super.toString()).append(", ");
		}
        if (city != null) {
			builder.append("city=").append(city.getId());
		}
        builder.append("]");
        return builder.toString();
    }
	
}

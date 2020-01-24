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
 * @author manur
 * @since v2.6
 */
public class Role extends PandemicObject {
    /*
     * WARNING - Using this randomly generated UID was a bad idea.
     * During the next change to this class, please modify the value to
     * the new version number ; example :
     *  private static final long serialVersionUID = 28L; // last major change : v2.8
     */
    private static final long serialVersionUID = 706007195078135977L;
	
	private static final int TROUBLE_SHOOTER_ROLE_ID = 121;

	private boolean expansionRole;

	public Role(int id, String name, ImageIcon imageIcon, int x, int y, boolean expansionRole, BoardZone boardZone) {
		super(PandemicObject.Type.PAWN, id, name, imageIcon, x, y, boardZone);
		this.expansionRole = expansionRole;
	}

	public boolean isExpansionRole() {
		return expansionRole;
	}
	
    public boolean isTroubleShooter() {
        return TROUBLE_SHOOTER_ROLE_ID == getId();
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (this.getId() != other.getId())
			return false;
		return true;
	}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Role [toString()=").append(super.toString()).append(", expansionRole=").append(expansionRole).append("]");
        return builder.toString();
    }

}

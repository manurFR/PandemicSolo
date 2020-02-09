/**
 *   Copyright (C) 2011 Emmanuel Bizieau <manur@manur.org>,
 *             (C) 2010 Andras Damian <http://boardgamegeek.com/user/jancsoo>
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

import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * Pandemic Solitaire
 * Class to represent the graphical properties of each component of the gameboard (cubes, markers, cards...)
 * 
 * @author jancsoo
 * @author manur
 */

public class GraphicalProperties implements Serializable {
	private static final long serialVersionUID = 28L;

	private ImageIcon image;
    private int x;
    private int y;
    private JComponent swingComponent;
    
    public GraphicalProperties(ImageIcon image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
    }

	public ImageIcon getImage() {
		return image;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public JComponent getSwingComponent() {
		return swingComponent;
	}

	public void setSwingComponent(JComponent swingComponent) {
		this.swingComponent = swingComponent;
	}
}

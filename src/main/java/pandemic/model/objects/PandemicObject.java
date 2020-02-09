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

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import pandemic.model.BoardZone;

/**
 * Base class of the component hierarchy.
 * "PandemicObject(s)" are all the concepts of the game : cubes, cards, cities, markers...
 * 
 * Class diagram of the component hierarchy :
 * 
 *           +----------------+             
 *           | PandemicObject |             +---------------------+
 *           +----------------+             | GraphicalProperties |
 *           | graphProps     |------------>+---------------------+
 *           | type           |             | x, y, imageIcon     |
 *           | id, name, zone |             +---------------------+
 *           | draggable      |<- - - - - - - - - - - - - -+------------------------------.
 *           +----------------+                            | All other counters are simple \
 *            A   A   A   A                                | PandemicObject(s) (markers,    \
 *           /    |    \   \____________________           | research stations, cures...)    |
 *          /     |     \                       \          +---------------------------------+
 *         /      |      \                       \
 * +-------+  +-------+  +---------------+   +---------+
 * | City  |  | Cube  |  |      Role     |   |  Card   |
 * +-------+  +-------+  +---------------+   +---------+
 * | color |  | color |  | expansionRole |   | city    |
 * +-------+  +-------+  +---------------+   +---------+
 *
 * @author jancsoo
 * @author manur
 */
public class PandemicObject implements MouseListener, MouseMotionListener, Serializable {
	/*
	 * WARNING - Using this randomly generated UID was a bad idea.
	 * During the next change to this class, please modify the value to
	 * the new version number ; example :
	 *  private static final long serialVersionUID = 28L; // last major change : v2.8
	 */
	private static final long serialVersionUID = 2366879559150311995L;

	// Ids are not bound to be unique. For example, all cubes from the same color will have the same id.
	private int id;
	
	private String name;
	private Type type;
	
	private GraphicalProperties graphicalProperties;
	
	private BoardZone boardZone;
	protected boolean draggable;	
	
	public enum Type {
		CITY,
		CUBE, PAWN, RESEARCH_STATION, 
		CURE_MARKER, ERADICATION_MARKER, INFECTION_MARKER, OUTBREAKS_MARKER, CURRENT_PLAYER_MARKER,
		INFECTION_CARD, 
		PLAYER_CITY_CARD, EPIDEMIC_CARD, SPECIAL_EVENT_CARD, MUTATION_EVENT_CARD, EMERGENCY_EVENT_CARD
	}
	
	/**
	 * Constructor with id.
	 * By default, components are considered draggable.
	 * @param type
	 * @param id
	 * @param imageIcon
	 * @param x
	 * @param y
	 */
	public PandemicObject(Type type, int id, ImageIcon imageIcon, int x, int y, BoardZone boardZone) {
		this.type = type;
		this.id = id;
		
		this.graphicalProperties = new GraphicalProperties(imageIcon, x, y);
		
		this.boardZone = boardZone;
		this.draggable = true;
	}
	
	/**
	 * Constructor with id and name. Draggable by default.
	 * @param type
	 * @param id
	 * @param name
	 * @param imageIcon
	 * @param x
	 * @param y
	 */
	public PandemicObject(Type type, int id, String name, ImageIcon imageIcon, int x, int y, BoardZone boardZone) {
		this(type, id, imageIcon, x, y, boardZone);
		this.name = name;
	}
	
	/**
	 * Constructor with only a name (no id). Draggable by default.
	 * @param type
	 * @param name
	 * @param imageIcon
	 * @param x
	 * @param y
	 */
	public PandemicObject(Type type, String name, ImageIcon imageIcon, int x, int y, BoardZone boardZone) {
		this(type, -1, name, imageIcon, x, y, boardZone);
	}

	/*  Indirect getters & setters for GraphicalProperties fields  */
	public int getX() {
		return graphicalProperties.getX();
	}
	
	public void setX(int x) {
		graphicalProperties.setX(x);
	}
	
	public int getY() {
		return graphicalProperties.getY();
	}
	
	public void setY(int y) {
		graphicalProperties.setY(y);
	}
	
	public ImageIcon getImage() {
		return graphicalProperties.getImage();
	}
	
	public JComponent getSwingComponent() {
		return graphicalProperties.getSwingComponent();
	}
	
	public void setSwingComponent (JComponent swingComponent) {
		graphicalProperties.setSwingComponent(swingComponent);
	}

	/*  Getters & Setters  */
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public GraphicalProperties getGraphicalProperties() {
		return graphicalProperties;
	}

	public BoardZone getBoardZone() {
		return boardZone;
	}

	public boolean isDraggable() {
		return draggable;
	}

	public void setBoardZone(BoardZone boardZone) {
		this.boardZone = boardZone;
	}

	/* ******* SWING ******* */
	
	/**
	 * Pass new coordinates and move the actual swing component to that location
	 * @param newX X coordinates
	 * @param newY Y coordinates
	 */
	public void move(int newX, int newY) {
		setX(newX);
		setY(newY);
		
		getSwingComponent().setLocation(newX, newY);
	}
	
	/*  MouseListener  */
	
	private Point pressedPoint;
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (draggable) {
			pressedPoint = e.getPoint();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	/*  MouseMotionListener  */
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (draggable && pressedPoint != null) {
			JComponent swingComponent = getSwingComponent();
			Point objectLocation = swingComponent.getLocation();
			
			// The parent (JPanel) dimensions are the borders one cannot cross
			int parentWidth = swingComponent.getParent().getWidth();
			int parentHeight = swingComponent.getParent().getHeight();
			
			// If the action would move the graphical component out of the parent Panel, 
			//  block it at the border. Check it in the two dimensions.
			int translateX = e.getX() - pressedPoint.x;
			if (objectLocation.x + translateX < 0) {
				translateX = -objectLocation.x; // to obtain 0
			} else if (objectLocation.x + translateX > (parentWidth - this.getImage().getIconWidth())) {
				translateX = parentWidth - this.getImage().getIconWidth() - objectLocation.x;
			}
			
			int translateY = e.getY() - pressedPoint.y;
			if (objectLocation.y + translateY < 0) {
				translateY = -objectLocation.y;
			} else if (objectLocation.y + translateY > (parentHeight - this.getImage().getIconHeight())) {
				translateY = parentHeight - this.getImage().getIconHeight() - objectLocation.y;
			}
			
			// Move the graphical component by the corrected distance
			objectLocation.translate(translateX, translateY);
			swingComponent.setLocation(objectLocation);
			setX(swingComponent.getX());
			setY(swingComponent.getY());
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PandemicObject [id=").append(id).append(", name=").append(name).append(", type=").append(type).append(", getX()=")
                .append(getX()).append(", getY()=").append(getY()).append("]");
        return builder.toString();
    }
}

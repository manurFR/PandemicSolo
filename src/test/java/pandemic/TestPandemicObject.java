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

import static org.junit.Assert.assertEquals;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import pandemic.model.BoardZone;
import pandemic.model.objects.PandemicObject;

/** 
* @author manur
* @since v2.6
*/
public class TestPandemicObject {

	private PandemicObject testObject;
	
	@Before
	public void setUp() throws Exception {
		testObject = new PandemicObject(PandemicObject.Type.PLAYER_CITY_CARD, 35, "Paris", new ImageIcon(), 100, 100, BoardZone.RESERVE);
		JComponent component = new JLabel("Test");
		component.setLocation(100, 100);
		testObject.setSwingComponent(component);
		
		JPanel board = new JPanel();
		//board.setPreferredSize(new Dimension(1000, 920));
		board.setSize(new Dimension(1000, 920));
		board.add(component);
	}

	@Test
	public void testMove()
	{
		testObject.move(250, 300);
		
		assertEquals(250, testObject.getX());
		assertEquals(300, testObject.getY());
		assertEquals(250, testObject.getSwingComponent().getLocation().x);
		assertEquals(300, testObject.getSwingComponent().getLocation().y);
	}

	@Test
	public void testMouseDragged()
	{
		MouseEvent ePressed = new MouseEvent(testObject.getSwingComponent(), MouseEvent.MOUSE_PRESSED, new Date().getTime(), 0, 101, 101, 1, false);
		testObject.mousePressed(ePressed);
		
		MouseEvent eDraggedOK = new MouseEvent(testObject.getSwingComponent(), MouseEvent.MOUSE_DRAGGED, new Date().getTime(), 0, 81, 121, 0, false);
		testObject.mouseDragged(eDraggedOK);
		
		assertEquals(80, testObject.getSwingComponent().getLocation().x);
		assertEquals(120, testObject.getSwingComponent().getLocation().y);
		assertEquals(80, testObject.getX());
		assertEquals(120, testObject.getY());
		
		// Out of the frame
		testObject.move(100, 100);
		testObject.mousePressed(ePressed);
		
		MouseEvent eDraggedBorder = new MouseEvent(testObject.getSwingComponent(), MouseEvent.MOUSE_DRAGGED, new Date().getTime(), 0, -5, 141, 0, false);
		testObject.mouseDragged(eDraggedBorder);
		
		assertEquals(0, testObject.getSwingComponent().getLocation().x);
		assertEquals(140, testObject.getSwingComponent().getLocation().y);
		assertEquals(0, testObject.getX());
		assertEquals(140, testObject.getY());
	}

}

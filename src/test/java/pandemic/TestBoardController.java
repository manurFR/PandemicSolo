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

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import pandemic.BoardController;
import pandemic.DefaultBoardController;
import pandemic.model.PandemicModel;

/** 
* @author manur
* @since v2.6
*/
public class TestBoardController {

	private PandemicModel mockModel;
	
	private BoardController controller;
	
	@Before
	public void setUp() {
		mockModel = mock(PandemicModel.class);
		
		controller = new DefaultBoardController(mockModel);
	}
	
	@Test
	public void testDrawPlayerCardWhenNoCards()
	{
		when(mockModel.getNbOfPlayerCards()).thenReturn(0);
		
		controller.drawPlayerCard();
		
		verify(mockModel, never()).drawPlayerCard();
	}
	
	@Test
	public void testDrawPlayerCard()
	{
		when(mockModel.getNbOfPlayerCards()).thenReturn(30);
		
		controller.drawPlayerCard();
		
		verify(mockModel).drawPlayerCard();
	}

	@Test
	public void testRemoveDiscardedCardWrongIndex()
	{
		when(mockModel.getDiscardPile()).thenReturn(Arrays.asList(new Integer[] { 1, 2 }));
		
		controller.removeDiscardedCard(5);
		
		verify(mockModel, never()).removeDiscardedCard(anyInt());
	}

}

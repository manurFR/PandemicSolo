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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pandemic.model.BoardZone;
import pandemic.model.ComponentsFactory;
import pandemic.model.objects.Card;
import pandemic.model.objects.City;
import pandemic.util.GenericResourceProvider;
import pandemic.util.ResourceProvider;

public class TestDistributeStartingCards {

	List<City> cities;
	List<Card> cityCards;
	
	private ComponentsFactory componentsFactory;
	private ResourceProvider mockResourceProvider;
	
	@Before
	public void setUp() {
		componentsFactory = new ComponentsFactory();
		
		mockResourceProvider = mock(GenericResourceProvider.class);
		// This allows partial mocking (getIcon() is not stubbed) :
		when(mockResourceProvider.getIcon(anyString())).thenCallRealMethod();
		// (Note : partial mocking needed the mock() to be on the implementation, 
		//  not the ResourceProvider interface)
		
		componentsFactory.setResourceProvider(mockResourceProvider);
		
		MockBundle mb = new MockBundle("city.1", "367;166;Algiers;BLACK");
		for (int i=2; i<11; i++) {
			// Add 9 other cards
			mb.addKV("city." + i, "121;163;Atlanta;BLUE");
		}
		mb.addKV("cards.defaultPosition", "3000;3000;images/card{0}.jpg");
		mb.addKV("playerCardsFor.2", "4");
		mb.addKV("playerCardsFor.3", "3");
		mb.addKV("playerCardsFor.4", "2");
		mb.addKV("playerCardsBox.separation", "0;31");
		mb.addKV("playerCardsBox.1", "12;451");
		mb.addKV("playerCardsBox.2", "169;451");
		mb.addKV("playerCardsBox.3", "328;451");
		mb.addKV("playerCardsBox.4", "485;451");
		when(mockResourceProvider.getBundle(anyString())).thenReturn(mb);	
		
		cities = componentsFactory.createCities();
		cityCards = componentsFactory.createPlayerCards(cities);
	}

	int[] xRoleBox = { 12, 169, 328, 485 };
	int[] yRoleCard = { 451, 451+31, 451+31+31, 451+31+31+31 };
	
	@Test
	public void testDistributeStartingCards2Roles() {
		testForXRoles(2);
	}
	
	@Test
	public void testDistributeStartingCards3Roles() {
		testForXRoles(3);
	}

	@Test
	public void testDistributeStartingCards4Roles() {
		testForXRoles(4);
	}
	
	private void testForXRoles(int nbOfRoles)
	{
		componentsFactory.distributeStartingCards(cityCards, nbOfRoles);
		
		final int expectedNbOfRows = Integer.parseInt(componentsFactory.getValue("playerCardsFor." + nbOfRoles, 0));
		
		for (int row=0; row<expectedNbOfRows; row++) {
			for (int role=0; role<nbOfRoles; role++) {
				int index = row*nbOfRoles + role;
				String message = "Row: " + row + " Role : " + role + " / Index: " + index;
				
				Card card = cityCards.get(index);
				assertEquals(message, xRoleBox[role], card.getX());
				assertEquals(message, yRoleCard[row], card.getY());
				assertEquals(message, BoardZone.HAND_OR_DISCARD, card.getBoardZone());
			}
		}
		
		// Check that the next card is still out of the role boxes 
		Card card = cityCards.get(nbOfRoles*expectedNbOfRows + 1);
		assertEquals(3000, card.getX());
		assertEquals(3000, card.getY());
		assertEquals(BoardZone.RESERVE, card.getBoardZone());
	}
	
}

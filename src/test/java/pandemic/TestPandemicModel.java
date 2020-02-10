/**
 * Copyright (C) 2011 Emmanuel Bizieau <manur@manur.org>
 * <p>
 * This file is part of PandemicSolo.
 * <p>
 * PandemicSolo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * PandemicSolo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with PandemicSolo.  If not, see <http://www.gnu.org/licenses/>.
 */
package pandemic;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pandemic.model.BoardZone;
import pandemic.model.DifficultyLevel;
import pandemic.model.Expansion;
import pandemic.model.PandemicModel;
import pandemic.model.objects.Card;
import pandemic.model.objects.Role;
import pandemic.util.DecksObserver;
import pandemic.util.GameConfig;
import pandemic.util.GenericResourceProvider;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * @author manur
 * @since v2.6
 */
public class TestPandemicModel {

    private PandemicModel model = null;

    @Before
    public void setUp() throws Exception {
        GameConfig config = new GameConfig();

        config.setDifficultyLevel(DifficultyLevel.NORMAL);
        config.setNbOfRoles(4);
        config.getRolesExpansions().add(Expansion.CORE);

        model = new PandemicModel(config);
        model.initialize(new GenericResourceProvider());
    }

    @Test
    public void testDrawPlayerCard() {
        Card topCard = model.getPlayerDeck().get(0);
        topCard.setBoardZone(BoardZone.RESERVE);

        model.drawPlayerCard();

        assertFalse(model.getPlayerDeck().get(0) == topCard);
        assertEquals(BoardZone.HAND_OR_DISCARD, topCard.getBoardZone());
    }

    @Test
    public void testDrawInfectionCard() {
        Integer topCard = model.getInfectionDeck().get(0);

        int drawnCard = model.drawInfectionCard();

        assertFalse(model.getInfectionDeck().get(0).intValue() == topCard.intValue());
        assertEquals(topCard.intValue(), drawnCard);
    }

    @Test
    public void testDrawBottomInfectionCard() {
        List<Integer> infectionDeck = model.getInfectionDeck();
        Integer bottomCard = infectionDeck.get(infectionDeck.size() - 1);

        model.drawBottomInfectionCard();

        assertFalse(infectionDeck.get(infectionDeck.size() - 1).intValue() == bottomCard.intValue());
        assertEquals(bottomCard, model.getDiscardPile().get(model.getDiscardPile().size() - 1));
    }

    @Test
    public void testReshuffleInfectionCards() {
        // Make a copy of the list before the reshuffling
        List<Integer> discardPile = new ArrayList<Integer>(model.getDiscardPile());

        model.reshuffleInfectionCards();

        // Check that each card that was in the discard pile before is in the deck now
        int nbCards = 0;
        for (Integer card : discardPile) {
            assertTrue(model.getInfectionDeck().contains(card));
            nbCards++;
        }
        assertTrue(nbCards > 0);

        // Check that the discard pile is empty now
        assertEquals(0, model.getDiscardPile().size());
    }

	@Test
	public void testReshuffleIsIdempotent() throws IOException, ClassNotFoundException {
    	// For Survival Mode, there is the need that each reshuffle from the same saved copy ends with the same infection deck

		// Make a copy of the model
		ByteArrayOutputStream savedModel = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(savedModel);
		objectOutputStream.writeObject(model);
		objectOutputStream.flush();
		objectOutputStream.close();

		model.reshuffleInfectionCards();

		final List<Integer> infectionDeckFirstTime = model.getInfectionDeck();

		// Reload copy of the model
		ByteArrayInputStream inputStream = new ByteArrayInputStream(savedModel.toByteArray());
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		PandemicModel copyOfModel = (PandemicModel) objectInputStream.readObject();
		inputStream.close();
		objectInputStream.close();

		copyOfModel.registerDecksObserver(mock(DecksObserver.class)); // hack to initialize the observers which are transient

		copyOfModel.reshuffleInfectionCards();
		final List<Integer> infectionDeckSecondTime = copyOfModel.getInfectionDeck();

		assertThat(infectionDeckFirstTime, is(infectionDeckSecondTime));
	}

	@Test
    public void testRemoveDiscardedCard() {
        model.getDiscardPile().add(0, new Integer(100));

        assertFalse(model.removeDiscardedCard(0));

        Integer card3 = model.getDiscardPile().get(3);
        assertTrue(model.removeDiscardedCard(3));

        for (Integer card : model.getDiscardPile()) {
            assertFalse(card.intValue() == card3.intValue());
        }
    }

    @Test
    public void testRearrangeInfectionDeck() {
        List<Integer> newCardOrder = Arrays.asList(new Integer[]{5, 4, 3, 2, 1, 0});

        List<Integer> oldCards = new ArrayList<Integer>(model.getInfectionDeck().subList(0, 6));

        model.rearrangeInfectionDeck(newCardOrder);

        for (int index = 0; index < newCardOrder.size(); index++) {
            assertEquals(oldCards.get(newCardOrder.get(index)), model.getInfectionDeck().get(index));
        }
    }

    @Test
    public void testChangeRole() {
        List<Role> affectedRolesBefore = new ArrayList<Role>(model.getAffectedRoles());

        // Let's test on role #2
        Role oldRole = model.getAffectedRoles().get(2);
        // Let's take the first role not already affected
        Role newRole = null;
        for (Role role : model.getAllRoles()) {
            if (!affectedRolesBefore.contains(role)) {
                newRole = role;
                break;
            }
        }

        model.changeRole(2, newRole);

        assertFalse(oldRole.equals(model.getAffectedRoles().get(2)));
        assertEquals(newRole, model.getAffectedRoles().get(2));

        // Check that there are no other changes
        for (int i = 0; i < affectedRolesBefore.size(); i++) {
            if (i != 2) {
                assertEquals(affectedRolesBefore.get(i), model.getAffectedRoles().get(i));
            }
        }
    }


}

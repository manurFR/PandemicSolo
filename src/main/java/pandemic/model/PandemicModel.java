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
package pandemic.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pandemic.model.objects.Card;
import pandemic.model.objects.City;
import pandemic.model.objects.PandemicObject;
import pandemic.model.objects.Role;
import pandemic.util.DecksObserver;
import pandemic.util.GameConfig;
import pandemic.util.ResourceProvider;
import pandemic.util.RolesObserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.util.Collections.unmodifiableList;
import static pandemic.model.Variant.EMERGENCY_EVENTS;
import static pandemic.model.Variant.MUTATION;

/**
 * The "model" in the MVC pattern.
 * This class will own and manage all information about the internal state of the game, 
 *  including a handle to all displayed components, the state of the different 
 *  stacks of cards, etc.
 * Graphical operations should NOT be handled here.
 * 
 * @author jancsoo
 * @author manur
 */

public class PandemicModel implements Serializable {

	private static final long serialVersionUID = 28L;
	
	private static final Logger logger = LoggerFactory.getLogger(PandemicModel.class);

	private Random randomizer;

	private GameConfig config;

	private transient List<DecksObserver> decksObservers;
	private transient List<RolesObserver> rolesObservers;
	
	private List<PandemicObject> countersLibrary = new ArrayList<PandemicObject>();
	
	private List<Card> cardsLibrary = new ArrayList<Card>(); // All cards
	private List<Card> playerDeck = new ArrayList<Card>(); // Player cards still in the drawing pile
	private int currentPlayerCard; // for debugging purposes
	
	private List<City> cityList = new ArrayList<City>();
	
	private List<Role> allRoles = null;
	private List<Role> affectedRoles = null;
	
	private List<Integer> infectionDeck = new ArrayList<Integer>(); // Infection pile to draw

	private List<Integer> discardPile = new ArrayList<Integer>(); // Infection cards discarded

	/**
	 * Constructor
	 * @param config The game configuration submitted by the user
	 */
	public PandemicModel(GameConfig config) {
		this.config = config;
		this.decksObservers = new ArrayList<DecksObserver>();
		this.rolesObservers = new ArrayList<RolesObserver>();
	}
	
	/**
	 * Set up the model objects for a new game.
	 */
	public void initialize(ResourceProvider resourceProvider) {
	    logger.trace("Initializing model...");   
	    
		// Creates the factory for instantiating the components,
		//  and inject the bundle of the file with the pixel coordinates.
		ComponentsFactory componentsFactory = new ComponentsFactory();
		componentsFactory.setResourceProvider(resourceProvider);

		/**************************************************************************
		 *                            Randomizer                                  *
		 **************************************************************************/
		randomizer = new Random();

		/**************************************************************************
		 *                               Roles                                    *
		 **************************************************************************/
		
		allRoles = componentsFactory.createRoles(config.getRolesExpansions());
		affectedRoles = drawRoles(allRoles, config.getNbOfRoles());

		/**************************************************************************
		 *                               Cities                                   *
		 **************************************************************************/
		
		// Load cities
		cityList.addAll(componentsFactory.createCities());
		
		/**************************************************************************
		 *                           Infection Cards                              *
		 **************************************************************************/
		
		for (int q=1; q<=48; q++) {
		    infectionDeck.add(q);
		}
		Collections.shuffle(infectionDeck, randomizer);

		// Mutation expansion
		// The two mutation cards are put on top of the Infection *discard* pile
		if (config.getVariants().contains(MUTATION)) {
			discardPile.add(0, 100);
			discardPile.add(1, 101);

			logger.debug("...add mutation to infection discard pile : {}", discardPile.get(0));
			logger.debug("...add mutation to infection discard pile : {}", discardPile.get(0));
		}
		
        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            for (Integer card : infectionDeck) {
                sb.append(card);
                sb.append(" ");
            }
            logger.debug("...generate infection deck : {}", sb.toString());
        }
		
		/**************************************************************************
		 *                             Player Cards                               *
		 **************************************************************************/
    	 /* They are put far beyond the Frame-limit (3000,3000) 
    	      where they are going to be "imported" from :) */

		// First, let's set up a deck containing only player (city) cards and Special Events		
		cardsLibrary.addAll(componentsFactory.createPlayerCards(cityList));
		cardsLibrary.addAll(componentsFactory.createSpecialEvents(config, randomizer));
		
		// Then shuffle
		Collections.shuffle(cardsLibrary, randomizer);
		
		// Distribute (and remove the distributed cards from the library)
		componentsFactory.distributeStartingCards(cardsLibrary, config.getNbOfRoles());
		
		// Constitute the deck without the distributed cards
		for (Card card : cardsLibrary) {
			if (card.getBoardZone().equals(BoardZone.RESERVE)) {
				playerDeck.add(card);
			}
		}
       currentPlayerCard = 1; // initialization, for debugging purposes
		
		// Add the Mutation event cards randomly in the deck
		if (config.getVariants().contains(MUTATION)) {
			componentsFactory.addMutationEventsCards(playerDeck, cardsLibrary, randomizer);
		}
		
		// Get the epidemic cards...
		List<Card> epidemicCards = componentsFactory.createEpidemicCards(config, randomizer);

		// Prepare Emergency Events...
		List<Card> emergencyEvents = Collections.emptyList();
		if (config.getVariants().contains(EMERGENCY_EVENTS)) {
			emergencyEvents = componentsFactory.createEmergencyEvents(config, randomizer);
		}

		// ...And add them intelligently to the player deck, splitting it into piles of the same size
		// and adding one epidemic card at a random place in each pile
		logger.debug("Add Epidemic cards :");
		playerDeck = componentsFactory.addCardsEvenly(playerDeck, unmodifiableList(epidemicCards), unmodifiableList(emergencyEvents), randomizer);
		
		// Finally, add the epidemic cards in the library to keep a reference on them
		cardsLibrary.addAll(epidemicCards);
		cardsLibrary.addAll(emergencyEvents);

		if (logger.isDebugEnabled()) {
		    logger.debug("Starting player deck :");
		    for (int i=0; i<playerDeck.size(); i++) {
		        Card card = playerDeck.get(i);
		        logger.debug("#{} : id={} type={} name={}", new Object[] {i, card.getId(), card.getType(), card.getName() });
		    }
		}

		/**************************************************************************
		 *                               Counters                                 *
		 **************************************************************************/
		
		// Add cubes in the reserve (including the purple disease when the Mutation variant is activated)
		countersLibrary.addAll(componentsFactory.createCubes(config));
		
		// ***************** Place the initial infection-cubes on the board
		// *************************************************************
		
		// Draw 9 cards from the infection deck and discard them.
		// Place 3 cubes for each of the first 3 infections cards, 2 cubes for the next 
		// 3 infection cards, and 1 cube for the last 3 infection cards. 
		// The cubes are to be taken from the reserve and placed on the city depicted 
		// on each infection card.
		//
		for (int nbOfCubes = 3; nbOfCubes > 0; nbOfCubes--) {
			logger.debug("...placing {} cube{} in :", nbOfCubes, ((nbOfCubes>1) ? "s" : "") );
			for (int infectionCard=0; infectionCard < 3; infectionCard++) {
				int nextInfectionCard = drawInfectionCard(false); // no cough sound on set up
				
				// Move the nbOfCubes cubes
				componentsFactory.moveCubesToCity(countersLibrary, nbOfCubes, cityList.get(nextInfectionCard));
			}
		}
		
		// RESEARCH STATIONS ***************************************************************************
		
		// First, find Atlanta
		City atlanta = null;
		for (City c : cityList) {
			if (c.getName().equals("Atlanta")) {
				atlanta = c;
				break;
			}
		}
		
		// Set up the Research Stations
		countersLibrary.addAll(componentsFactory.createResearchStations(atlanta));
		
		// Set up the pawns
		componentsFactory.placeUsedRoles(affectedRoles, atlanta);
		countersLibrary.addAll(allRoles);

		// CURE MARKERS ****************** (including purple only for the Mutation variant)

		countersLibrary.addAll(componentsFactory.createCureMarkers(config.getDiseases()));

		// ERADICATION TOKENS ********** (including a fifth one only for the Mutation variant)

		countersLibrary.addAll(componentsFactory.createEradicationMarkers(config.getDiseases()));
		
		// INFECTION RATE TOKEN **********

		countersLibrary.add(componentsFactory.createInfectionRateMarker());

		// OUTBREAKS TOKEN **********

		countersLibrary.add(componentsFactory.createOutbreaksMarker());
		
		// CURRENT PLAYER-MARKER TOKEN **********

		countersLibrary.add(componentsFactory.createCurrentPlayerMarker());
		
		logger.trace("...Model initialization done");    
	}

	/**
	 * Draw the role of each player, chosen randomly.
	 * @param allRoles The list of roles from which to choose randomly
	 * @param nbOfRoles The number of roles to draw
	 */
	private List<Role> drawRoles(List<Role> allRoles, int nbOfRoles) {
		List<Role> chosenRoles = new ArrayList<Role>();

		List<Role> availableRoles = new ArrayList<Role>(allRoles); // make a copy so that allRoles stays in logical order and the Z-order of the icons is correct
		Collections.shuffle(availableRoles, randomizer);

		// Let's take the first "nbOfRoles" roles
		for (int roleIndex=0; roleIndex<nbOfRoles; roleIndex++) {
			final Role chosenRole = availableRoles.get(roleIndex);
			logger.debug("...chosen Role #{} : id={}", roleIndex, chosenRole.getId());
			chosenRoles.add(chosenRole);
			// the chosen roles are put at the top of allRoles for the Z-order of their icon to be correct
			allRoles.remove(chosenRole);
			allRoles.add(roleIndex, chosenRole);
		}
		
		return chosenRoles;
	}

	/**********************************************************************/
	
	/**
	 * Draw the next player card from the stack and put it on the board, 
	 *  in the appropriate space. 
	 */
	public void drawPlayerCard() {
		Card playerCard = playerDeck.remove(0);
		playerCard.setBoardZone(BoardZone.HAND_OR_DISCARD);
        logger.debug("Drew player card #{} : {} ({})", 
                new Object[] { currentPlayerCard++, playerCard.getName(), playerCard.getImage().getDescription() });

		// Notify the observers
		for (DecksObserver observer : decksObservers) {
			observer.playerCardDrawn(playerCard);
		}
	}
	
	/**
	 * Draw a card from the top of the infection pile and display it on that pile.
	 * @param cough true if the cough sound must be launched (but it will be heard only if the sound is on)
	 * @return the drawn card id
	 */
	public int drawInfectionCard(boolean cough) {
		int nextCard = infectionDeck.remove(0);
		discardPile.add(nextCard);

        logger.debug("Drew infection card: {} - {} => discarded", new Object[] { nextCard,
                (nextCard == 100 || nextCard == 101) ? "MUTATION!" : cityList.get(nextCard).getName() });
        		
		// Notify the observers (to put the card graphically on top of the pile)
		for (DecksObserver observer : decksObservers) {
			observer.infectionCardDrawn(true);
		}

		return nextCard;
	}
		
	/**
	 * Draw a card from the BOTTOM of the infection pile and display it on that pile.
	 */
	public void drawBottomInfectionCard() {
		// Draw the bottom card
		int bottomCard = infectionDeck.remove(infectionDeck.size() - 1);
		discardPile.add(bottomCard);
		
		logger.debug("Drew bottom infection card: {} - {} => discarded", new Object[] { bottomCard,
                (bottomCard == 100 || bottomCard == 101) ? "MUTATION!" : cityList.get(bottomCard).getName() });

		// Notify the observers
		for (DecksObserver observer : decksObservers) {
			observer.infectionCardDrawn(false);
		}
	}

	/**
	 * Shuffle the cards from the discard pile and put them on top of the Infection Deck
	 */
	public void reshuffleInfectionCards() {
		// Shuffle discarded pile
		StringBuilder sb = new StringBuilder("Discard pile BEFORE :");
		for (Integer card : discardPile) {
			sb.append(card);
			sb.append(" ");
		}
		logger.debug(sb.toString());
		
		Collections.shuffle(discardPile, randomizer);
	      // Notify the observers
        for (DecksObserver observer : decksObservers) {
            observer.infectionDeckShuffled();
        }
		
		sb = new StringBuilder("Discard pile AFTER :");
		for (Integer card : discardPile) {
			sb.append(card);
			sb.append(" ");
		}
		logger.debug(sb.toString());
		
		// Add the shuffled discard pile to the top of the infection drawing deck
		infectionDeck.addAll(0, discardPile);
		
		sb = new StringBuilder("New Infection deck :");
		for (Integer card : infectionDeck) {
			sb.append(card);
			sb.append(" ");
		}
		logger.debug(sb.toString());
		
		// Clear the discard pile
		discardPile.clear();

		// Notify the observers
		for (DecksObserver observer : decksObservers) {
			observer.infectionDeckCleared();
		}
	}

	/**
	 * Remove the card from the discarded pile of the Infection deck
	 * @param discardedCardIndex Index of the card to remove in the discardPile List
	 * @return true only when a card has been effectively removed
	 */
	public boolean removeDiscardedCard(int discardedCardIndex) {
		int discardedCard = discardPile.get(discardedCardIndex);
		
		// MUTATION! cards 
		if (discardedCard == 100 || discardedCard == 101) {
			return false;
		}

		discardPile.remove(discardedCardIndex);
		logger.debug("Removed infection card: " + discardedCard + " " + cityList.get(discardedCard).getName());
		
		// Notify the observers
		for (DecksObserver observer : decksObservers) {
			observer.infectionDeckCardRemoved(); // Necessary if the discarded card was the top one
		}
		
		return true;
	}
	
	/**
	 * Rearrange the first 6 cards at the top of the Infection deck following the given new order
	 *  (or the N remaining cards if there are less than 6 cards in the deck).
	 * @param newCardOrder The position indexes of the cards in the current configuration of the Infection deck,
	 *  					in the order in which the method has to place them now. 
	 */
	public void rearrangeInfectionDeck(List<Integer> newCardOrder) {
		StringBuilder sb = new StringBuilder("New Infection cards order : ");
		for (Integer cardIndex : newCardOrder) {
			sb.append(cardIndex);
			sb.append(" ");
		}
		logger.debug(sb.toString());
		
		sb = new StringBuilder("Old Infection deck: ");
		for (Integer infectionCard : infectionDeck) {
			sb.append(infectionCard);
			sb.append(" ");
		}
		logger.debug(sb.toString());
		
		// 1) Let's each of the moved infection cards and put them in another List BUT in the new order
		List<Integer> newTopOfDeck = new ArrayList<Integer>();
		sb = new StringBuilder("New top of deck: ");
		for (Integer cardIndex : newCardOrder) {
			newTopOfDeck.add(infectionDeck.get(cardIndex));
			sb.append(infectionDeck.get(cardIndex));
			sb.append(" ");
		}
		logger.debug(sb.toString());
		
		// 2) Let's remove the moved infection cards from the deck
		for (Integer infectionCard : newTopOfDeck) {
			infectionDeck.remove(infectionCard);
		}
		
		// 3) Let's put back all infection cards, in the new order, by adding the alternate List on top of the infection deck
		infectionDeck.addAll(0,	newTopOfDeck);
		
		sb = new StringBuilder("New Infection deck: ");
		for (Integer infectionCard : infectionDeck) {
			sb.append(infectionCard);
			sb.append(" ");
		}
		logger.debug(sb.toString());

	    // Notify the observers
        for (DecksObserver observer : decksObservers) {
            observer.infectionDeckRearranged();
        }
	}
	
	/**
	 * Change a role
	 * @param roleIndex The index of the role changing
	 * @param newRole The new role
	 */
	public void changeRole(int roleIndex, Role newRole) {
		// Notify the observers before the change
		for (RolesObserver observer : rolesObservers) {
			observer.roleWillChange(roleIndex);
		}
		
		affectedRoles.set(roleIndex, newRole);
		
		// Notify the observers after the change
		for (RolesObserver observer : rolesObservers) {
			observer.roleHasChanged(roleIndex);
		}
	}
	
	/**********************************************************************/
	
	/**
	 * Register an observer that wants to be notified everytime a card is drawn 
	 *  from the Infection Pile or the Player deck
	 * @param observer DecksObserver to notify
	 */
	public void registerDecksObserver(DecksObserver observer) {
		if (decksObservers == null) {
			decksObservers = new ArrayList<DecksObserver>();
		}
				
		if (!decksObservers.contains(observer)) {
			decksObservers.add(observer);
		}
	}

	/**
	 * Register an observer that wants to be notified everytime a role has changed
	 * @param observer RolesObserver to notify
	 */
	public void registerRolesObserver(RolesObserver observer) {
		if (rolesObservers == null) {
			rolesObservers = new ArrayList<RolesObserver>();
		}
		
		if (!rolesObservers.contains(observer)) {
			rolesObservers.add(observer);
		}
	}

	/**********************************************************************/

	public int getNbOfRoles() {
		return config.getNbOfRoles();
	}

	public int getNbOfPlayerCards() {
		return playerDeck.size();
	}

	public List<Card> getPlayerDeck() {
		return playerDeck;
	}

	public List<PandemicObject> getCountersLibrary() {
		return countersLibrary;
	}

	public List<Card> getCardsLibrary() {
		return cardsLibrary;
	}

	public List<City> getCityList() {
		return cityList;
	}

	public List<Role> getAllRoles() {
		return allRoles;
	}

	public List<Role> getAffectedRoles() {
		return affectedRoles;
	}

	public List<Integer> getInfectionDeck() {
		return infectionDeck;
	}

	public List<Integer> getDiscardPile() {
		return discardPile;
	}

	public GameConfig getConfig() {
		return config;
	}
}
/**
 * Copyright (C) 2011 Emmanuel Bizieau <manur@manur.org>,
 * (C) 2010 Andras Damian <http://boardgamegeek.com/user/jancsoo>
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
package pandemic.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pandemic.model.objects.*;
import pandemic.util.GameConfig;
import pandemic.util.ResourceProvider;

import javax.swing.*;
import java.text.MessageFormat;
import java.util.*;

/**
 * Factory to create and dispatch the components of the game (counters, cards...)
 *
 * @author manur
 * @author jancsoo
 */
public class ComponentsFactory {

    private static final Logger logger = LoggerFactory.getLogger(ComponentsFactory.class);

    // Indexes of the X and Y coordinates in the .properties file
    private static final int X_INDEX = 0;
    private static final int Y_INDEX = 1;

    // Keys in the resource bundle .properties file
    private static final String KEY_RESERVE_SEPARATION = "reserve.separation";
    private static final String KEY_RESERVE_RESEARCHSTATIONS = "researchStations.reserve";
    private static final String KEY_CUBES = "cubes.";
    private static final String KEY_CITY = "city.";
    private static final String KEY_RESEARCHSTATIONS_CITYSHIFT = "researchStations.cityShift";
    private static final String KEY_ROLE = "role.";
    private static final String KEY_FIRSTPAWN_CITYSHIFT = "firstPawn.cityShift";
    private static final String KEY_NEXTPAWNS_SHIFT = "nextPawns.cityShift";
    private static final String KEY_CURE = "cure.";
    private static final String KEY_RESERVE_ERADICATION = "eradication.reserve";
    private static final String KEY_ERADICATION_SEPARATION = "eradication.separation";
    private static final String KEY_INFECTIONRATE = "infectionRateMarker";
    private static final String KEY_OUTBREAKS = "outbreaksMarker";
    private static final String KEY_CURRENTPLAYER = "currentPlayerMarker";
    private static final String KEY_CARD_DEFAULTPOSITION = "cards.defaultPosition";
    private static final String KEY_CARD_SPECIALEVENT = "specialEvent.";
    private static final String KEY_CARD_MUTATIONEVENT = "mutationEvent.";
    private static final String KEY_CARD_VIRULENTEPIDEMIC = "virulentEpidemic.";
    private static final String KEY_CARD_EMERGENCYEVENT = "emergencyEvent.";
    private static final String KEY_CARDSPERROLE = "playerCardsFor.";
    private static final String KEY_PLAYERCARDSBOX = "playerCardsBox.";
    private static final String KEY_PLAYERCARDSBOX_SEPARATION = "playerCardsBox.separation";

    private static final int EPIDEMIC_CARD_ID = 0;

    private static final int CUBES_PER_LINE = 12;

    private static final Integer[] EVENTS_FORBIDDEN_IN_SURVIVAL_MODE = {50, 52, 53, 55, 57, 68, 69};

    private static final String RESOURCEBUNDLE_BASENAME = "componentsCoordinates";

    private ResourceProvider resourceProvider;

    public void setResourceProvider(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    /**********************************************************
     * Utility methods for querying the resourcebundle file   *
     **********************************************************/

    public String getValue(String key, int index) {
        ResourceBundle bundleFile = resourceProvider.getBundle(RESOURCEBUNDLE_BASENAME);

        // Get the value and cast it to an int
        if (bundleFile.containsKey(key)) {
            String[] values = bundleFile.getString(key).split(";");
            if (index <= values.length) {
                return values[index].replaceAll("\"", ""); // Get rid of (sometimes necessary) double quotes around the value
            }
        }
        return "";
    }

    private int getXCoordinate(String key) {
        return Integer.parseInt(getValue(key, X_INDEX));
    }

    private int getYCoordinate(String key) {
        return Integer.parseInt(getValue(key, Y_INDEX));
    }

    /**
     * Create a marker and place it on the board.
     * @param type The Component.Type
     * @param key Key of the marker to fetch the icon and the coordinates
     * @param iconIndex Index of the icon path in the value corresponding to the key
     * @return The marker
     */
    private PandemicObject createIconMarker(PandemicObject.Type type, String key, int iconIndex) {
        ImageIcon image = resourceProvider.getIcon(getValue(key, iconIndex));
        return new PandemicObject(type, key, image, getXCoordinate(key), getYCoordinate(key), BoardZone.BOARD);
    }

    /**********************************************************
     * Factory methods to create the game components          *
     **********************************************************/

    /**
     * Create all useful roles
     * @param expansionRoles list of expansions used for the roles
     * @return The List of all useful roles
     */
    public List<Role> createRoles(Set<Expansion> expansionRoles) {
        List<Role> listRole = new ArrayList<Role>();

        List<Integer> availableRoles = new ArrayList<Integer>();
        for (Expansion expansion : expansionRoles) {
            availableRoles.addAll(expansion.getRoles());
        }

        for (Integer roleId : availableRoles) {
            String roleName = getValue(KEY_ROLE + roleId, 2);

            int xPos = getXCoordinate(KEY_ROLE + roleId);
            int yPos = getYCoordinate(KEY_ROLE + roleId);
            BoardZone boardZone = BoardZone.RESERVE;

            // Prepare the pawn on the board
            ImageIcon imageIcon = resourceProvider.getIcon(getValue(KEY_ROLE + roleId, 3));
            Role pawn = new Role(roleId, roleName, imageIcon, xPos, yPos, boardZone);
            logger.debug("...created {}", pawn);

            listRole.add(pawn);
        }

        return listRole;
    }

    /**
     * Return a List of all the cities in the game
     */
    public List<City> createCities() {
        List<City> cities = new ArrayList<City>();

        for (int cityId = 0; cityId < 49; cityId++) {
            String name = getValue(KEY_CITY + cityId, 2);
            int x = getXCoordinate(KEY_CITY + cityId);
            int y = getYCoordinate(KEY_CITY + cityId);
            Disease color = null;
            try {
                color = Disease.valueOf(getValue(KEY_CITY + cityId, 3));
            } catch (IllegalArgumentException e) {
                // Do nothing. The city won't have a color ; it's for the Epidemic card.
            }

            City city = new City(cityId, name, x, y, color);
            logger.debug("...created {}", city);

            cities.add(city);
        }

        return cities;
    }

    /**
     * Create the deck of the 48 regular City player cards, and associate them each with their city
     * @param citiesList The List of all cities in the game, passed to permit to associate each card 
     *          with its corresponding city
     * @return The deck of player cards
     */
    public List<Card> createPlayerCards(List<City> citiesList) {
        List<Card> playerCards = new ArrayList<Card>();

        int xPos = getXCoordinate(KEY_CARD_DEFAULTPOSITION);
        int yPos = getYCoordinate(KEY_CARD_DEFAULTPOSITION);
        String templateName = getValue(KEY_CARD_DEFAULTPOSITION, 2);

        // 48 "regular" City player cards
        for (int cityId = 1; cityId < 49; cityId++) {
            String imageName = MessageFormat.format(templateName, cityId);

            City city = citiesList.get(cityId);

            ImageIcon imageIcon = resourceProvider.getIcon(imageName);
            Card card = new Card(PandemicObject.Type.PLAYER_CITY_CARD, cityId, city.getName(), imageIcon, xPos, yPos, city, BoardZone.RESERVE);
            logger.debug("...created {}", card);

            playerCards.add(card);
        }

        return playerCards;
    }

    /**
     * Create the deck of Special Event cards
     */
    public List<Card> createSpecialEvents(GameConfig config, Random randomizer) {
        List<Card> specialEventsCards = new ArrayList<Card>();

        int xPos = getXCoordinate(KEY_CARD_DEFAULTPOSITION);
        int yPos = getYCoordinate(KEY_CARD_DEFAULTPOSITION);
        String templateName = getValue(KEY_CARD_DEFAULTPOSITION, 2);

        List<Integer> availableEvents = new ArrayList<Integer>();

        for (Expansion expansion : config.getEventCardsExpansions()) {
            availableEvents.addAll(expansion.getEventCards());
        }

        if (config.isSurvivalMode()) {
            availableEvents.removeAll(Arrays.asList(EVENTS_FORBIDDEN_IN_SURVIVAL_MODE));
        }

        Collections.shuffle(availableEvents, randomizer);

        // never draw more event cards than is available
        int nbEventCardsToCreate = Math.min(config.isFiveEvents() ? 5 : config.getNbOfRoles() * 2, availableEvents.size());

        for (int i = 0; i < nbEventCardsToCreate; i++) {
            int eventId = availableEvents.get(i);

            String imageName = MessageFormat.format(templateName, eventId);

            String eventName = getValue(KEY_CARD_SPECIALEVENT + eventId, 0);

            ImageIcon imageIcon = resourceProvider.getIcon(imageName);
            Card card = new Card(PandemicObject.Type.SPECIAL_EVENT_CARD, eventId, eventName, imageIcon, xPos, yPos, BoardZone.RESERVE);
            logger.debug("...created {}", card);

            specialEventsCards.add(card);
        }

        return specialEventsCards;
    }

    /**
     * Distribute player cards to each role.
     *
     * @param cardsLibrary
     *            The deck (previously shuffled) of player cards where to draw
     * @param nbOfRoles
     *            The number of roles
     */
    public void distributeStartingCards(List<Card> cardsLibrary, int nbOfRoles) {
        int nbOfCardsPerRole = Integer.parseInt(getValue(KEY_CARDSPERROLE + nbOfRoles, 0));
        int ySeparation = getYCoordinate(KEY_PLAYERCARDSBOX_SEPARATION);

        for (int roleboxRow = 0; roleboxRow < nbOfCardsPerRole; roleboxRow++) {
            for (int roleNumber = 0; roleNumber < nbOfRoles; roleNumber++) {
                int cardIndex = roleboxRow * nbOfRoles + roleNumber;
                Card card = cardsLibrary.get(cardIndex);

                card.setX(getXCoordinate(KEY_PLAYERCARDSBOX + (roleNumber + 1)));
                card.setY(getYCoordinate(KEY_PLAYERCARDSBOX + (roleNumber + 1)) + roleboxRow * ySeparation);
                card.setBoardZone(BoardZone.HAND_OR_DISCARD);

                logger.debug("...card {} ({}) distributed to player {} (x={}, y={})",
                        new Object[]{card.getId(), card.getName(), roleNumber, card.getX(), card.getY()});
            }
        }
    }


    /**
     * Add the three specific Event cards for the Mutation variant.
     * The cards are placed randomly in the deck, except at the top and bottom.
     * @param pileDeck The deck of cards where to insert the Mutation Event cards
     * @param cardsLibrary We also add the new cards in the Library to keep a reference on them
     * @param randomizer Utility to get random numbers
     */
    public void addMutationEventsCards(List<Card> pileDeck, List<Card> cardsLibrary, Random randomizer) {
        logger.debug("...remaining player cards deck size : {}", pileDeck.size());

        int xPos = getXCoordinate(KEY_CARD_DEFAULTPOSITION);
        int yPos = getYCoordinate(KEY_CARD_DEFAULTPOSITION);
        String templateName = getValue(KEY_CARD_DEFAULTPOSITION, 2);

        for (int cardIndex = 301; cardIndex <= 303; cardIndex++) {
            String imageName = MessageFormat.format(templateName, cardIndex);

            String eventName = getValue(KEY_CARD_MUTATIONEVENT + cardIndex, 0);

            ImageIcon imageIcon = resourceProvider.getIcon(imageName);
            Card card = new Card(PandemicObject.Type.MUTATION_EVENT_CARD, cardIndex, eventName, imageIcon, xPos, yPos, BoardZone.RESERVE);

            // We exclude the top and the bottom of the deck, 
            //  so the position must be between 1 and pileDeck.size()-2.
            int placeToInsert = 1 + randomizer.nextInt(pileDeck.size() - 2);
            pileDeck.add(placeToInsert, card);
            logger.debug("...mutation event card " + cardIndex + " added at index : " + placeToInsert);
            cardsLibrary.add(card);
        }
    }

    /**
     * Create the deck of epidemic cards
     */
    public List<Card> createEpidemicCards(GameConfig config, Random randomizer) {
        List<Card> epidemicCards = new ArrayList<Card>();

        int xPos = getXCoordinate(KEY_CARD_DEFAULTPOSITION);
        int yPos = getYCoordinate(KEY_CARD_DEFAULTPOSITION);
        String templateName = getValue(KEY_CARD_DEFAULTPOSITION, 2);

        if (config.getVariants().contains(Variant.VIRULENT_STRAIN)) { // Virulent Strain challenge epidemic cards
            Integer[] cardIds = {201, 202, 203, 204, 205, 206, 207, 208, 209, 210};
            List<Integer> availableCards = Arrays.asList(cardIds);
            Collections.shuffle(availableCards, randomizer);
            for (int cardIndex = 0; cardIndex < config.getNbOfEpidemics(); cardIndex++) {
                String imageName = MessageFormat.format(templateName, availableCards.get(cardIndex));

                String epidemicName = getValue(KEY_CARD_VIRULENTEPIDEMIC + availableCards.get(cardIndex), 0);

                ImageIcon imageIcon = resourceProvider.getIcon(imageName);
                Card card = new Card(PandemicObject.Type.EPIDEMIC_CARD, availableCards.get(cardIndex), epidemicName, imageIcon, xPos, yPos, BoardZone.RESERVE);
                logger.debug("...created {}", card);

                epidemicCards.add(card);
            }
        } else { // classic epidemic cards
            String imageName = MessageFormat.format(templateName, EPIDEMIC_CARD_ID);
            ImageIcon imageIcon = resourceProvider.getIcon(imageName);

            for (int cardIndex = 0; cardIndex < config.getNbOfEpidemics(); cardIndex++) {
                Card card = new Card(PandemicObject.Type.EPIDEMIC_CARD, EPIDEMIC_CARD_ID, "Epidemic!", imageIcon, xPos, yPos, BoardZone.RESERVE);
                logger.debug("...created {}", card);

                epidemicCards.add(card);
            }
        }

        return epidemicCards;
    }

    public List<Card> createEmergencyEvents(GameConfig config, Random randomizer) {
        List<Card> emergencyEventCards = new ArrayList<Card>();

        int xPos = getXCoordinate(KEY_CARD_DEFAULTPOSITION);
        int yPos = getYCoordinate(KEY_CARD_DEFAULTPOSITION);

        for (int id = 401; id <= 410; id++) {
            String imageName = MessageFormat.format("emergency{0,number,00}.jpg", id);

            String cardName = getValue(KEY_CARD_EMERGENCYEVENT + id, 0);

            ImageIcon imageIcon = resourceProvider.getIcon(imageName);
            Card card = new Card(PandemicObject.Type.EMERGENCY_EVENT_CARD, id, cardName, imageIcon, xPos, yPos, BoardZone.RESERVE);
            logger.debug("...created {}", card);

            emergencyEventCards.add(card);
        }

        Collections.shuffle(emergencyEventCards, randomizer);
        return emergencyEventCards;
    }

    /**
     * Split the player deck in as many piles as there are epidemic cards to add.
     * Add one epidemic card in each pile (plus one Emergency Event card if this variant is used), shuffle the piles,
     * and finally stack these piles to create the final player deck
     * @param initialDeck The deck of player cards to which the epidemic cards will be added
     * @param epidemics The List of epidemic cards to add evenly to the deck
     * @param emergencyEventsToAdd The list of emergency event cards to add to the deck (an empty list if Emergency Events is not used)
     * @return the new player deck, with epidemic cards added
     */
    public List<Card> addCardsEvenly(List<Card> initialDeck, List<Card> epidemics, List<Card> emergencyEventsToAdd, Random randomizer) {
        int nbOfPiles = epidemics.size();
        logger.debug("...nb of cards in deck : {}", initialDeck.size());
        logger.debug("...nb of piles / cards to add : {}", nbOfPiles);

        List<Card> finalDeck = new ArrayList<Card>();

        int[] pileSizes = determinePileSizes(initialDeck.size(), epidemics.size());

        int idx = 0; // index of cards in the initial deck
        int iCurrPile = 0; // number of the current pile
        for (Integer pileSize : pileSizes) {
            List<Card> currentPile = new ArrayList<Card>(initialDeck.subList(idx, idx + pileSize));

            currentPile.add(epidemics.get(iCurrPile));
            if (!emergencyEventsToAdd.isEmpty()) {
                currentPile.add(emergencyEventsToAdd.get(iCurrPile));
            }
            Collections.shuffle(currentPile, randomizer);
            finalDeck.addAll(currentPile);

            idx += pileSize;
            iCurrPile++;
        }

        return finalDeck;
    }

    /**
     * Prepare an array specifying the number of cards in each pile.
     * We need to follow precisely the rulebook :
     * #8. (...) Make the piles as equal in size as is possible.
     * #9. (...) If the piles aren't exactly the same size, stack them so that the larger piles are above the smaller piles.
     * Thanks to BGG user B Factor for pointing this out.
     * @param nbOfCards Size of the deck before introducing the epidemic cards
     * @param nbOfPiles Number of epidemic cards to introduce, thus number of piles to prepare
     * @return An array of ints, of size nbOfPiles. Each element is the size of the corresponding pile.
     */
    public int[] determinePileSizes(int nbOfCards, int nbOfPiles) {
        int[] pileSizes = new int[nbOfPiles];

        int minSize = nbOfCards / nbOfPiles;
        int remainingCards = nbOfCards - (nbOfPiles * minSize);

        /* Each pile will have at least minSize cards, and the X remaining ones will be put,
         *  one per pile, on the first X piles.	 */
        for (int i = 0; i < nbOfPiles; i++) {
            pileSizes[i] = minSize + ((i < remainingCards) ? 1 : 0);
        }

        logger.debug("...nb of cards per pile : {}", pileSizes);

        return pileSizes;
    }

    /**
     * Creates cubes for the four or five diseases, depending on the variants.
     * The cubes are split in rows of 12 items.
     * @return The list of all cubes set up.
     */
    public List<Cube> createCubes(GameConfig config) {
        List<Cube> listCubes = new ArrayList<Cube>();

        for (Disease color : config.getDiseases()) {
            int xSeparation = getXCoordinate(KEY_RESERVE_SEPARATION);
            int ySeparation = getYCoordinate(KEY_RESERVE_SEPARATION);

            // Starting pixel references - will not change
            int xRef = getXCoordinate(KEY_CUBES + color.name());
            int yRef = getYCoordinate(KEY_CUBES + color.name());

            ImageIcon cubeImage = resourceProvider.getIcon(getValue(KEY_CUBES + color.name(), 2));

            int nbOfCubes = config.numberOfCubes(color);
            // Calculate the number of full rows
            int nbOfRows = nbOfCubes / CUBES_PER_LINE;

            // Current cube coordinates - initialized at the reference point
            int xPos = xRef;
            int yPos = yRef;

            for (int row = 0; row < nbOfRows; row++) {
                for (int item = 0; item < CUBES_PER_LINE; item++) {
                    Cube cube = new Cube(color.ordinal(), cubeImage, xPos, yPos, color, BoardZone.RESERVE);
                    listCubes.add(cube);

                    xPos += xSeparation;
                }
                yPos += ySeparation;
                xPos = xRef; // start back at the beginning of the row
            }

            // if the nb of cubes is not divisible by CUBES_PER_LINE, add a row for the remaining cubes
            if ((nbOfCubes % CUBES_PER_LINE) != 0) {
                for (int item = 0; item < (nbOfCubes - nbOfRows * CUBES_PER_LINE); item++) {
                    Cube cube = new Cube(color.ordinal(), cubeImage, xPos, yPos, color, BoardZone.BOARD);
                    listCubes.add(cube);

                    xPos += xSeparation;
                }
            }

            logger.debug("...color {}, created {} cubes", color, nbOfCubes);
        }

        // Reverse the order of the cubes in the List, in order to display them logically according to the shadows
        Collections.reverse(listCubes);

        return listCubes;
    }

    /**
     * Take cubes from the reserve to put on a city on the board
     * @param countersLibrary The main counters reference, where to find the cubes
	 * @param cubeColors The cubes to move, by their color
	 * @param city The destination city
     */
    public void moveCubesToCity(List<PandemicObject> countersLibrary, List<Disease> cubeColors, City city) {
        for (int cubeIndex = 0; cubeIndex < cubeColors.size(); cubeIndex++) {
            // Look for the first cube of the city's color in the reserve
            Cube cube = null;
            for (int counterIndex = countersLibrary.size() - 1; counterIndex >= 0; counterIndex--) { // Reverse order to preserve a coherent display
                PandemicObject counter = countersLibrary.get(counterIndex);
                if (counter.getType().equals(PandemicObject.Type.CUBE)) {
                    cube = (Cube) counter;
                    if (cube.getColor().equals(cubeColors.get(cubeIndex)) && cube.getBoardZone().equals(BoardZone.RESERVE)) {
                        break;
                    } else {
                        cube = null;
                    }
                }
            }

            if (cube == null) {
                throw new IllegalArgumentException("No cube available in the Reserve for this city's color (" + cubeColors.get(cubeIndex).name() + ")");
            }

            // Calculate the new position around the city marker, depending on how many cubes we have to arrange.
            int posX = 0;
            int posY = 0;
            switch (cubeColors.size()) {
				case 4:
					if (cubeIndex == 0) {
						posX = -21;
						posY = -14;
					} else if (cubeIndex == 1) {
						posX = -4;
						posY = -14;
					} else if (cubeIndex == 2) {
						posX = -21;
						posY = -3;
					} else if (cubeIndex == 3) {
						posX = -4;
						posY = -3;
					}
					break;
				case 3:
					if (cubeIndex == 0) {
						posX = -21;
						posY = -14;
					} else if (cubeIndex == 1) {
						posX = -4;
						posY = -14;
					} else if (cubeIndex == 2) {
						posX = -12;
						posY = -3;
					}
					break;
				case 2:
					if (cubeIndex == 0) {
						posX = -21;
						posY = -12;
					} else if (cubeIndex == 1) {
						posX = -4;
						posY = -12;
					}
					break;
				case 1:
					posX = -12;
					posY = -12;
					break;
			}

            cube.setX(city.getX() + posX);
            cube.setY(city.getY() + posY);
            cube.setBoardZone(BoardZone.BOARD);

            logger.debug("...moving {} cube to x={}, y={}", new Object[] {cube.getColor().name(), cube.getX(), cube.getY()});
        }
    }

    /**
     * Creates the 6 available Research Stations : 5 in the reserve and 1 on the starting city (Atlanta in the normal game)
     * @param startingCity The city where the first Research Station will be set up
     * @return The list of the 6 Research Stations created
     */
    public List<PandemicObject> createResearchStations(City startingCity) {
        List<PandemicObject> listStations = new ArrayList<PandemicObject>();

        int xPosReserve = getXCoordinate(KEY_RESERVE_RESEARCHSTATIONS);
        int yPosReserve = getYCoordinate(KEY_RESERVE_RESEARCHSTATIONS);

        int xSeparation = getXCoordinate(KEY_RESERVE_SEPARATION);

        ImageIcon imgResearchStation = resourceProvider.getIcon(getValue(KEY_RESERVE_RESEARCHSTATIONS, 2));

        // 5 Research Stations in the reserve
        for (int i = 0; i < 5; i++) {
            PandemicObject researchStation = new PandemicObject(
                    PandemicObject.Type.RESEARCH_STATION,
                    "Research Station",
                    imgResearchStation,
                    xPosReserve + i * xSeparation,
                    yPosReserve,
                    BoardZone.RESERVE);
            listStations.add(researchStation);
        }

        // 1 starting Research Station in the starting city
        int xShift = getXCoordinate(KEY_RESEARCHSTATIONS_CITYSHIFT);
        int yShift = getYCoordinate(KEY_RESEARCHSTATIONS_CITYSHIFT);
        PandemicObject researchStation = new PandemicObject(
                PandemicObject.Type.RESEARCH_STATION,
                "Research Station",
                imgResearchStation,
                startingCity.getX() + xShift,
                startingCity.getY() + yShift,
                BoardZone.BOARD);
        listStations.add(researchStation);

        // Reverse the order of the research stations in the List, in order to display them coherently
        Collections.reverse(listStations);

        return listStations;
    }

    /**
     * Move the selected roles to their starting city
     * @param usedRoles List of the roles that will be played
     * @param startingCity The city where all the affected roles are starting the game
     */
    public void placeUsedRoles(List<Role> usedRoles, City startingCity) {
        int j = 0;

        for (Role pawn : usedRoles) {
            // Change coordinates to the starting city (Atlanta)
            pawn.setX(startingCity.getX() + getXCoordinate(KEY_FIRSTPAWN_CITYSHIFT) + j * getXCoordinate(KEY_NEXTPAWNS_SHIFT));
            pawn.setY(startingCity.getY() + getYCoordinate(KEY_FIRSTPAWN_CITYSHIFT) + j * getYCoordinate(KEY_NEXTPAWNS_SHIFT));
            pawn.setBoardZone(BoardZone.BOARD);

            j++;
        }
    }

    /**
     * Create the cure markers for used diseases and place them on the board
     * @param colors The array of the colors (Diseases) used ; a marker will be created for each color
     * @return The List of the cure markers
     */
    public List<PandemicObject> createCureMarkers(Disease[] colors) {
        List<PandemicObject> listMarkers = new ArrayList<PandemicObject>();

        for (Disease color : colors) {
            String key = KEY_CURE + color.name();
            listMarkers.add(createIconMarker(PandemicObject.Type.CURE_MARKER, key, 2));
        }

        return listMarkers;
    }

    /**
     * Create and place the eradication markers needed for playing (one per disease)
     * @param colors The array of the colors (diseases) used for this game
     * @return The List of the eradication markers
     */
    public List<PandemicObject> createEradicationMarkers(Disease[] colors) {
        List<PandemicObject> listMarkers = new ArrayList<PandemicObject>();

        int xSeparation = getXCoordinate(KEY_ERADICATION_SEPARATION);

        int xPos = getXCoordinate(KEY_RESERVE_ERADICATION);
        int yPos = getYCoordinate(KEY_RESERVE_ERADICATION);
        ImageIcon imgEradication = resourceProvider.getIcon(getValue(KEY_RESERVE_ERADICATION, 2));

        for (@SuppressWarnings("unused") Disease color : colors) { // color is not used, but it ensures there's one marker per color
            PandemicObject eradicationMarker = new PandemicObject(PandemicObject.Type.ERADICATION_MARKER, "Eradication marker", imgEradication, xPos, yPos, BoardZone.RESERVE);
            listMarkers.add(eradicationMarker);

            xPos += xSeparation;
        }

        return listMarkers;
    }

    /**
     * Create the Infection Rate marker and place it on the first space of the Infection Rate Track
     * @return The marker
     */
    public PandemicObject createInfectionRateMarker() {
        return createIconMarker(PandemicObject.Type.INFECTION_MARKER, KEY_INFECTIONRATE, 2);
    }

    /**
     * Create the Outbreaks marker and place it on the first space of the Outbreaks Track
     * @return The marker
     */
    public PandemicObject createOutbreaksMarker() {
        return createIconMarker(PandemicObject.Type.OUTBREAKS_MARKER, KEY_OUTBREAKS, 2);
    }

    /**
     * Create the current player marker and place it on the first rolebox
     * @return The marker
     */
    public PandemicObject createCurrentPlayerMarker() {
        return createIconMarker(PandemicObject.Type.CURRENT_PLAYER_MARKER, KEY_CURRENTPLAYER, 2);
    }
}
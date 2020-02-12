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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pandemic.model.*;
import pandemic.model.objects.*;
import pandemic.util.GameConfig;
import pandemic.util.GenericResourceProvider;
import pandemic.util.ResourceProvider;

import java.util.*;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static pandemic.model.Disease.BLUE;
import static pandemic.model.Disease.PURPLE;
import static pandemic.model.Expansion.*;
import static pandemic.model.Variant.*;

/**
 * @author manur
 * @since v2.6
 */
public class TestComponentsFactory {

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
    }

    @Test
    public void testGetValue() {
        when(mockResourceProvider.getBundle(anyString())).thenReturn(
                new MockBundle("dummy", "A1;\"B2\";c3"));

        assertEquals("B2", componentsFactory.getValue("dummy", 1));

        try {
            assertEquals("", componentsFactory.getValue("dummy", 9));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void test_withMutation_createCubesIncludesPurple() {
        final MockBundle properties = new MockBundle("cubes.RED", "10;10;\"cube_red.jpg\"");
        properties.addKV("cubes.BLUE", "10;20;\"\"cube_blue.jpg\"");
        properties.addKV("cubes.YELLOW", "10;30;\"\"cube_yellow.jpg\"");
        properties.addKV("cubes.BLACK", "10;40;\"\"cube_black.jpg\"");
        properties.addKV("cubes.PURPLE", "100;100;\"\"cube_purple.jpg\"");
        properties.addKV("reserve.separation", "0;5");
        when(mockResourceProvider.getBundle(anyString())).thenReturn(properties);

        GameConfig configWithMutation = prepareAdvancedConfig();

        List<Cube> cubes = componentsFactory.createCubes(configWithMutation);
        int purples = 0;
        for (Cube c: cubes) {
            if (c.getColor().equals(Disease.PURPLE)) {
                purples++;
            }
        }
        assertEquals(12, purples);

        Cube c = cubes.get(0);
        assertTrue(c.getImage().getDescription().endsWith("cube_purple.jpg"));
        assertEquals(c.getX(), 100);
        assertEquals(c.getY(), 100);
    }

    @Test
    public void test_withoutMutation_createCubesDoesNotIncludePurple() {
        when(mockResourceProvider.getBundle(anyString())).thenReturn(new MockBundle("cubes.RED", "814;150;\"cube_red.jpg\""));

        GameConfig configWithoutMutation = prepareBasicConfig();

        List<Cube> cubes = componentsFactory.createCubes(configWithoutMutation);
        assertEquals(4*24, cubes.size());

        for (Cube c : cubes) {
            if (c.getColor().equals(Disease.PURPLE)) {
                fail();
            }
        }
    }

    @Test
    public void testCreateCities() {
        when(mockResourceProvider.getBundle(anyString())).thenReturn(
                new MockBundle("city.35", "362;97;\"Paris\";BLUE"));

        List<City> cities = componentsFactory.createCities();

        assertEquals(cities.size(), 49);

        City tested = cities.get(0);
        assertEquals(tested.getX(), 362);
        assertEquals(tested.getY(), 97);
        assertEquals(tested.getName(), "Paris");
        assertEquals(tested.getColor(), BLUE);
    }

    @Test
    public void testMoveCubesToCity() {
        when(mockResourceProvider.getBundle(anyString())).thenReturn(new MockBundle("cubes.RED", "814;150;\"cube_red.jpg\""));

		GameConfig config = prepareBasicConfig();
		config.getVariants().add(WORLDWIDE_PANIC);
		List<PandemicObject> listCubes = new ArrayList<PandemicObject>(componentsFactory.createCubes(config));

        when(mockResourceProvider.getBundle(anyString())).thenReturn(new MockBundle("city.35", "362;97;\"Paris\";BLUE"));

        List<City> cities = componentsFactory.createCities();
        City paris = cities.get(0);

        List<Disease> cubeColors = asList(BLUE, BLUE, PURPLE);
        componentsFactory.moveCubesToCity(listCubes, cubeColors, paris);

        List<Cube> movedCubes = new ArrayList<Cube>();
        for (PandemicObject cube: listCubes) {
            if (cube.getBoardZone().equals(BoardZone.BOARD)) {
                movedCubes.add((Cube)cube);
            }
        }
        assertEquals(3, movedCubes.size());

        Cube cube1 = movedCubes.get(0);
        Cube cube2 = movedCubes.get(1);
        Cube cube3 = movedCubes.get(2);

        assertEquals(paris.getX() - 12, cube1.getX());
        assertEquals(paris.getY() - 3, cube1.getY());
        assertEquals(PURPLE, cube1.getColor());

        assertEquals(paris.getX() - 4, cube2.getX());
        assertEquals(paris.getY() - 14, cube2.getY());
        assertEquals(BLUE, cube2.getColor());

        assertEquals(paris.getX() - 21, cube3.getX());
        assertEquals(paris.getY() - 14, cube3.getY());
        assertEquals(BLUE, cube3.getColor());
    }

    @Test
    public void testCreateResearchStations() {
        MockBundle mb = new MockBundle("city.35", "362;97;\"Paris\";BLUE");
        mb.addKV("researchStations.reserve", "814;230;\"research_station.jpg\"");
        mb.addKV("researchStations.cityShift", "15;-17");
        when(mockResourceProvider.getBundle(anyString())).thenReturn(mb);

        List<City> cities = componentsFactory.createCities();

        List<PandemicObject> stations = componentsFactory.createResearchStations(cities.get(0));
        // Warning : the List is reversed before returning.

        assertEquals(stations.size(), 6);

        PandemicObject st = stations.get(stations.size() - 1); // First station at last position
        assertEquals(814, st.getX());
        assertEquals(230, st.getY());

        PandemicObject stInParis = stations.get(0); // Last station (in starting city) at first position
        assertEquals(362 + 15, stInParis.getX());
        assertEquals(97 - 17, stInParis.getY());
    }

    @Test
    public void testCreateRoles() {
        when(mockResourceProvider.getBundle(anyString())).thenReturn(
                new MockBundle("role.118", "945;270;\"Archivist\";\"pawn_archivist.jpg\";OnTheBrink"));

        Set<Expansion> allExpansions = new LinkedHashSet<Expansion>();
        allExpansions.addAll(asList(Expansion.values()));
        List<Role> pawns = componentsFactory.createRoles(allExpansions);

        assertEquals(20, pawns.size()); // With expansion roles, there are 13 of them

        Role p = pawns.get(0);
        assertEquals("Archivist", p.getName());
        assertEquals(945, p.getX());
        assertEquals(270, p.getY());
    }

    @Test
    public void testCreateCureMarkers() {
        when(mockResourceProvider.getBundle(anyString())).thenReturn(
                new MockBundle("cure.BLACK", "860;316;\"cure_black.jpg\""));

        // With purple
        List<PandemicObject> markers = componentsFactory.createCureMarkers(prepareAdvancedConfig().getDiseases());

        assertEquals(5, markers.size());

        PandemicObject cure = markers.get(0);
        assertEquals(860, cure.getX());
        assertEquals(316, cure.getY());

        assertEquals(PandemicObject.Type.CURE_MARKER, cure.getType());

        // Without purple
        markers = componentsFactory.createCureMarkers(prepareBasicConfig().getDiseases());

        assertEquals(4, markers.size());
    }

    @Test
    public void testCreateEradicationMarkers() {
        when(mockResourceProvider.getBundle(anyString())).thenReturn(
                new MockBundle("eradication.reserve", "814;356;\"eradication_token.jpg\""));

        List<PandemicObject> markers = componentsFactory.createEradicationMarkers(prepareAdvancedConfig().getDiseases());

        assertEquals(5, markers.size());

        PandemicObject marker = markers.get(0);
        assertEquals(814, marker.getX());
        assertEquals(356, marker.getY());
        assertEquals(PandemicObject.Type.ERADICATION_MARKER, marker.getType());
        assertEquals(BoardZone.RESERVE, marker.getBoardZone());

        // Without the Mutation variant, purple is not used and there's only 4 markers needed
        markers = componentsFactory.createEradicationMarkers(prepareBasicConfig().getDiseases());

        assertEquals(4, markers.size());
    }

    @Test
    public void testCreateQuarantineTokens() {
        when(mockResourceProvider.getBundle(anyString())).thenReturn(
                new MockBundle("quarantine.reserve", "918;326;\"quarantine#.png\""));

        List<PandemicObject> markers = componentsFactory.createQuarantineTokens();

        assertEquals(12, markers.size());

        PandemicObject marker = markers.get(0);
        assertEquals(918, marker.getX());
        assertEquals(326, marker.getY());
        assertEquals(PandemicObject.Type.QUARANTINE_TOKEN, marker.getType());
        assertEquals(BoardZone.RESERVE, marker.getBoardZone());
    }

    @Test
    public void testCreateInfectionRateMarker() {
        when(mockResourceProvider.getBundle(anyString())).thenReturn(
                new MockBundle("infectionRateMarker", "551;8;\"infection_token.jpg\""));

        PandemicObject marker = componentsFactory.createInfectionRateMarker();

        assertEquals(551, marker.getX());
        assertEquals(8, marker.getY());
        assertEquals(PandemicObject.Type.INFECTION_MARKER, marker.getType());
        assertTrue(marker.getImage().getDescription().endsWith("infection_token.jpg"));
    }

    @Test
    public void testCreateOutbreaksMarker() {
        when(mockResourceProvider.getBundle(anyString())).thenReturn(
                new MockBundle("outbreaksMarker", "38;238;outbreak_token.jpg"));

        PandemicObject marker = componentsFactory.createOutbreaksMarker();

        assertEquals(38, marker.getX());
        assertEquals(238, marker.getY());
        assertEquals(PandemicObject.Type.OUTBREAKS_MARKER, marker.getType());
        assertTrue(marker.getImage().getDescription().endsWith("outbreak_token.jpg"));
    }

    @Test
    public void testCreateCurrentPlayerMarker() {
        when(mockResourceProvider.getBundle(anyString())).thenReturn(
                new MockBundle("currentPlayerMarker", "112;406;\"currentplayer_marker.jpg\""));

        PandemicObject marker = componentsFactory.createCurrentPlayerMarker();

        assertEquals(112, marker.getX());
        assertEquals(406, marker.getY());
        assertEquals(PandemicObject.Type.CURRENT_PLAYER_MARKER, marker.getType());
    }

    @Test
    public void testCreatePlayerCards() {
        MockBundle mb = new MockBundle("city.35", "362;97;\"Paris\";BLUE");
        mb.addKV("cards.defaultPosition", "3000;3000;\"card{0}.jpg\"");
        when(mockResourceProvider.getBundle(anyString())).thenReturn(mb);

        List<City> cities = componentsFactory.createCities();

        List<Card> cards = componentsFactory.createPlayerCards(cities);

        assertEquals(48, cards.size());

        Card c1 = cards.get(0);

        assertEquals(3000, c1.getX());
        assertEquals(3000, c1.getY());
        assertEquals(PandemicObject.Type.PLAYER_CITY_CARD, c1.getType());
        assertEquals("Paris", c1.getName());
        assertEquals(BoardZone.RESERVE, c1.getBoardZone());
    }

    @Test
    public void test_createSpecialEvents_withOnlyCore() {
        MockBundle mb = new MockBundle("specialEvent.50", "Forecast");
        mb.addKV("cards.defaultPosition", "3000;3000;\"card{0}.jpg\"");
        when(mockResourceProvider.getBundle(anyString())).thenReturn(mb);

        List<Card> events = componentsFactory.createSpecialEvents(prepareBasicConfig(), new Random());
        assertEquals(5, events.size());

        Card event = events.get(0);
        assertEquals(3000, event.getX());
        assertEquals(3000, event.getY());
        assertEquals(PandemicObject.Type.SPECIAL_EVENT_CARD, event.getType());
        assertEquals("Forecast", event.getName());
    }

    @Test
    public void test_createSpecialEvents_withCoreAndOnTheBrink() {
        MockBundle mb = new MockBundle("specialEvent.50", "Forecast");
        mb.addKV("cards.defaultPosition", "3000;3000;\"card{0}.jpg\"");
        when(mockResourceProvider.getBundle(anyString())).thenReturn(mb);

        List<Card> events = componentsFactory.createSpecialEvents(prepareAdvancedConfig(), new Random());
        assertEquals(8, events.size());

        // In the Lab events are #54, 55, 56, 57, 58, 59, 60, 61
        boolean onTheBrink = false;
        for (Card card : events) {
            if (card.getId() >= 54 && card.getId() <= 61) {
                onTheBrink = true;
                break;
            }
        }
        assertTrue(onTheBrink);
    }

    @Test
    public void test_createSpecialEvents_withCoreAndInTheLab() {
        MockBundle mb = new MockBundle("specialEvent.50", "Forecast");
        mb.addKV("cards.defaultPosition", "3000;3000;\"card{0}.jpg\"");
        when(mockResourceProvider.getBundle(anyString())).thenReturn(mb);

        final GameConfig advancedConfig = prepareAdvancedConfig();
        advancedConfig.getEventCardsExpansions().remove(ON_THE_BRINK);
        advancedConfig.getEventCardsExpansions().add(IN_THE_LAB);
        List<Card> events = componentsFactory.createSpecialEvents(advancedConfig, new Random());
        assertEquals(8, events.size());

        // In the Lab events are #62, 63, 64
        boolean inTheLab = false;
        for (Card card : events) {
            if (card.getId() >= 62 && card.getId() <= 64) {
                inTheLab = true;
                break;
            }
        }
        assertTrue(inTheLab);
    }

    @Test
    public void test_createSpecialEvents_inSurvivalMode() {
        MockBundle mb = new MockBundle("specialEvent.50", "Error");
        mb.addKV("cards.defaultPosition", "3000;3000;\"card{0}.jpg\"");
        when(mockResourceProvider.getBundle(anyString())).thenReturn(mb);

        GameConfig basicConfig = prepareBasicConfig(); // two players, only core
        basicConfig.setSurvivalMode(true);

        List<Card> events = componentsFactory.createSpecialEvents(basicConfig, new Random());
        assertEquals(2, events.size());

        Collections.sort(events, new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                return new Integer(o1.getId()).compareTo(o2.getId());
            }
        });

        int[] eventIds = new int[] {events.get(0).getId(), events.get(1).getId()};
        assertArrayEquals(new int[] {49, 51}, eventIds);
    }

    @Test
    public void test_createSpecialEvents_withFiveEvents() {
        MockBundle mb = new MockBundle("specialEvent.50", "Error");
        mb.addKV("cards.defaultPosition", "3000;3000;\"card{0}.jpg\"");
        when(mockResourceProvider.getBundle(anyString())).thenReturn(mb);

        GameConfig basicConfig = prepareBasicConfig(); // two players, only core
        basicConfig.setFiveEvents(true);

        List<Card> events = componentsFactory.createSpecialEvents(basicConfig, new Random());
        assertEquals(5, events.size());
    }

    /* Note: distributeStartingCards() is tested inside the class TestDistributeStartingCards */

    @Test
    public void testAddMutationEventsCards() {
        MockBundle mb = new MockBundle("city.35", "362;97;\"Paris\";BLUE");
        mb.addKV("cards.defaultPosition", "3000;3000;\"card{0}.jpg\"");
        mb.addKV("mutationEvent.301", "The Mutation Intensifies !");
        mb.addKV("mutationEvent.302", "The Mutation Spreads !");
        mb.addKV("mutationEvent.303", "The Mutation Threatens !");
        when(mockResourceProvider.getBundle(anyString())).thenReturn(mb);

        List<City> cities = componentsFactory.createCities();
        List<Card> cityCards = componentsFactory.createPlayerCards(cities);

        componentsFactory.addMutationEventsCards(cityCards, new ArrayList<Card>(), new Random());

        List<Card> mutationEvents = new ArrayList<Card>();
        for (Card card : cityCards) {
            if (card.getType().equals(PandemicObject.Type.MUTATION_EVENT_CARD)) {
                mutationEvents.add(card);
                if (card.getId() < 301 || card.getId() > 303) {
                    fail();
                }
            }
        }
        assertEquals(3, mutationEvents.size());

        Card mutation = mutationEvents.get(0);
        assertEquals(3000, mutation.getX());
        assertEquals(3000, mutation.getY());
        assertEquals(BoardZone.RESERVE, mutation.getBoardZone());
    }

    @Test
    public void testCreateEpidemicCards() {
        MockBundle mb = new MockBundle("virulentEpidemic.203", "Government Interference (Continuing Effect)");
        mb.addKV("cards.defaultPosition", "3000;3000;\"card{0}.jpg\"");
        when(mockResourceProvider.getBundle(anyString())).thenReturn(mb);

        // Classic game
        GameConfig config = prepareBasicConfig();
        config.setDifficultyLevel(DifficultyLevel.HEROIC);
        List<Card> classicEpidemics = componentsFactory.createEpidemicCards(config, new Random());
        assertEquals(6, classicEpidemics.size());

        Card epidemic = classicEpidemics.get(0);
        assertEquals(3000, epidemic.getX());
        assertEquals(3000, epidemic.getY());
        assertEquals(0, epidemic.getId());
        assertEquals(PandemicObject.Type.EPIDEMIC_CARD, epidemic.getType());

        // Virulent strain variant
        config = prepareAdvancedConfig();
        config.setDifficultyLevel(DifficultyLevel.NORMAL);
        List<Card> virulentEpidemics = componentsFactory.createEpidemicCards(config, new Random());
        assertEquals(5, virulentEpidemics.size());

        epidemic = virulentEpidemics.get(0);
        if (epidemic.getId() < 201 || epidemic.getId() > 210) {
            fail();
        }
        assertEquals(PandemicObject.Type.EPIDEMIC_CARD, epidemic.getType());
        assertEquals("Government Interference (Continuing Effect)", epidemic.getName());
    }

    @Test
    public void testDeterminePileSizes() {
        /* Thanks to BGG user B Factor for pointing out I was not following strictly the rulebook for the
         * epidemic cards distribution.
         *  Rule #8 : piles must be "as equal in size as possible", ie one must try to limit the difference between small and big piles
         *  Rule #9 : bigger piles must be above smaller piles in the final deck
         * In this example, with a deck of 58 cards and 7 piles to make, their expected size (before introducting the epidemics) is :
         */
        int[] expectedPileSizes = new int[]{8, 8, 7, 7, 7, 7, 7};
        int[] actualPileSizes = componentsFactory.determinePileSizes(51, 7);

        Assert.assertArrayEquals(expectedPileSizes, actualPileSizes);
    }

    @Test
    public void testAddCardsEvenlySimple() {
        List<Card> initialDeck = new ArrayList<Card>();
        List<Card> cardsToAdd = new ArrayList<Card>();

        Card deckCard = new Card(PandemicObject.Type.PLAYER_CITY_CARD, 1, "Dummy", null, 1, 1, BoardZone.RESERVE);
        // Place 7 dummy cards in the deck
        for (int i = 0; i < 7; i++) {
            initialDeck.add(deckCard);
        }

        // Create 2 epidemics to add
        cardsToAdd.add(new Card(PandemicObject.Type.EPIDEMIC_CARD, 201, "201", null, 1, 1, BoardZone.RESERVE));
        cardsToAdd.add(new Card(PandemicObject.Type.EPIDEMIC_CARD, 202, "202", null, 1, 1, BoardZone.RESERVE));

        // Add those 2 epidemics "evenly"
        Random mockRandomizer = mock(Random.class);
        when(mockRandomizer.nextInt(anyInt())).thenReturn(1);
        final List<Card> finalDeck =
                componentsFactory.addCardsEvenly(initialDeck, cardsToAdd, Collections.<Card>emptyList(), mockRandomizer);

        // the mock randomizer will always place the epidemic cards at the place before the last in each pile
        assertEquals(201, finalDeck.get(3).getId());
        assertEquals(202, finalDeck.get(7).getId());
    }

    @Test
    public void testAddCardsEvenly_withEmergencyEvents() {
        List<Card> initialDeck = new ArrayList<Card>();
        List<Card> cardsToAdd = new ArrayList<Card>();

        Card deckCard = new Card(PandemicObject.Type.PLAYER_CITY_CARD, 1, "Dummy", null, 1, 1, BoardZone.RESERVE);
        // Place 7 dummy cards in the deck
        for (int i = 0; i < 7; i++) {
            initialDeck.add(deckCard);
        }

        // Create 2 epidemics to add
        cardsToAdd.add(new Card(PandemicObject.Type.EPIDEMIC_CARD, 201, "201", null, 1, 1, BoardZone.RESERVE));
        cardsToAdd.add(new Card(PandemicObject.Type.EPIDEMIC_CARD, 202, "202", null, 1, 1, BoardZone.RESERVE));

        // Emergency Events
        List<Card> emergencyEvents = new ArrayList<Card>();
        emergencyEvents.add(new Card(PandemicObject.Type.EMERGENCY_EVENT_CARD, 401, "401", null, 1, 1, BoardZone.RESERVE));
        emergencyEvents.add(new Card(PandemicObject.Type.EMERGENCY_EVENT_CARD, 402, "402", null, 1, 1, BoardZone.RESERVE));

        // Add those 2 epidemics "evenly"
        Random mockRandomizer = mock(Random.class);
        when(mockRandomizer.nextInt(anyInt())).thenReturn(1);
        final List<Card> finalDeck = componentsFactory.addCardsEvenly(initialDeck, cardsToAdd, emergencyEvents, mockRandomizer);

        // the mock randomizer will always place the emergency event cards at the place before the last in each pile
        assertEquals(201, finalDeck.get(3).getId());
        assertEquals(401, finalDeck.get(4).getId());
        assertEquals(202, finalDeck.get(8).getId());
        assertEquals(402, finalDeck.get(9).getId());
    }


    private GameConfig prepareBasicConfig() {
        GameConfig basicConfig = new GameConfig();
        basicConfig.setDifficultyLevel(DifficultyLevel.NORMAL);
        basicConfig.setNbOfRoles(3);
        basicConfig.getVariants().clear();
        basicConfig.getRolesExpansions().add(CORE);
        basicConfig.setFiveEvents(false);
        basicConfig.getEventCardsExpansions().add(CORE);
        return basicConfig;
    }

    private GameConfig prepareAdvancedConfig() {
        GameConfig advancedConfig = new GameConfig();
        advancedConfig.setDifficultyLevel(DifficultyLevel.HEROIC);
        advancedConfig.setNbOfRoles(4);
        advancedConfig.getVariants().addAll(asList(VIRULENT_STRAIN, MUTATION));
        advancedConfig.getRolesExpansions().addAll(asList(CORE, IN_THE_LAB));
        advancedConfig.setFiveEvents(false);
        advancedConfig.getEventCardsExpansions().addAll(asList(CORE, ON_THE_BRINK));
        return advancedConfig;
    }
}

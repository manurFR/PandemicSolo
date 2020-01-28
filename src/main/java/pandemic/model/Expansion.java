package pandemic.model;

import java.util.List;

import static java.util.Arrays.asList;

public enum Expansion {
    CORE ("Core", new Integer[] {49, 50, 51, 52, 53}),
    ON_THE_BRINK ("On The Brink", new Integer[] {54, 55, 56, 57, 58, 59, 60, 61}),
    IN_THE_LAB ("In The Lab", new Integer[] {62, 63, 64}),
    STATE_OF_EMERGENCY ("State Of Emergency", new Integer[] {65, 66, 67, 68, 69, 70});

    private final String label;
    private final List<Integer> eventCards;

    Expansion(String label, Integer[] eventCardsArray) {
        this.label = label;
        this.eventCards = asList(eventCardsArray);
    }

    public String getLabel() {
        return label;
    }

    public List<Integer> getEventCards() {
        return eventCards;
    }
}

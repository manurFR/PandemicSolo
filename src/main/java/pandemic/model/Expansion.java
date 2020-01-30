package pandemic.model;

import java.util.List;

import static java.util.Arrays.asList;

public enum Expansion {
    CORE ("Core", new Integer[] {49, 50, 51, 52, 53}, new Integer[] {111, 112, 113, 114, 115, 116, 117}),
    ON_THE_BRINK ("On The Brink", new Integer[] {54, 55, 56, 57, 58, 59, 60, 61}, new Integer[] {118, 119, 120, 121, 122, 123}),
    IN_THE_LAB ("In The Lab", new Integer[] {62, 63, 64}, new Integer[] {124, 125, 126, 127}),
    STATE_OF_EMERGENCY ("State Of Emergency", new Integer[] {65, 66, 67, 68, 69, 70}, new Integer[] {128, 129, 130});

    private final String label;
    private final List<Integer> eventCards;
    private final List<Integer> roles;

    Expansion(String label, Integer[] eventCardsArray, Integer[] roles) {
        this.label = label;
        this.eventCards = asList(eventCardsArray);
        this.roles = asList(roles);
    }

    public String getLabel() {
        return label;
    }

    public List<Integer> getEventCards() {
        return eventCards;
    }

    public List<Integer> getRoles() {
        return roles;
    }
}

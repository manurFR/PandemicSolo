package pandemic.model;

public enum Variant {
    VIRULENT_STRAIN("Virulent Strain!"),
    MUTATION("Mutation"),
    WORLDWIDE_PANIC("Worldwide Panic"),
    EMERGENCY_EVENTS("Emergency Events"),
    QUARANTINES("Quarantines");

    private final String label;

    Variant(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
package pandemic.util.sounds;

/**
 * The sounds effects for the game.
 *
 * @author jancsoo
 * @author manur
 */
public enum Sounds {
        COUGH1("effect1.wav"), COUGH2("effect2.wav"), COUGH3("effect3.wav"), COUGH4("effect4.wav"), 
        SIREN("effect8.wav"), FLIP("effect21.wav"), SHUFFLE("effect22.wav"), GONG("effect31.wav"), 
        BUBBLE("effect32.wav"), FEELSBETTER("effect41.wav"), RADAR("effect51.wav"), BLIP("effect61.wav");

    private String wavFile;

    private Sounds(String wavFile) {
        this.wavFile = wavFile;
    }

    public String getWavFile() {
        return wavFile;
    }
}
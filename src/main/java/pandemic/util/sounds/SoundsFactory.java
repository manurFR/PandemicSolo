package pandemic.util.sounds;

public interface SoundsFactory {

    /**
     * When the game is being setup, sounds will be disabled.
     *
     * @param isSettingUp
     *            whether the game is being setup
     */
    public void setSettingUp(boolean isSettingUp);

    /**
     * @return true if sounds should be played, false otherwise.
     */
    public boolean isSoundOn();

    /**
     * Set the sounds on/off.
     *
     * @param isSoundsOn true to turn sounds on, false otherwise.
     */
    public void setSoundsOn(boolean isSoundsOn);

    /**
     * Toggle the soundOn state.
     */
    public void switchMute();

    /**
     * Plays the sound asked for, but only is <code>isSoundOn() == true</code>
     *
     * @param sound
     *            The sound to play
     */
    public void playSound(Sounds sound);

}
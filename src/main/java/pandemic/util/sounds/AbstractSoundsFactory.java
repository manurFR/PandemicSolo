package pandemic.util.sounds;

/**
 * Abstract implementation of <code>SoundsFactory</code>. Concreate
 * implementations only need to provide <code>doPlaySound</code>
 *
 * @author Barrie Treloar
 */
public abstract class AbstractSoundsFactory implements SoundsFactory {

    /** Should we emit sounds ? */
    private boolean soundOn = true;

    /** Dont emit sounds while setting up */
    private boolean settingUp = false;

    @Override
    public void setSettingUp(boolean isSettingUp) {
        settingUp = isSettingUp;
    }

    @Override
    public boolean isSoundOn() {
        return soundOn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSoundsOn(boolean isSoundsOn) {
        this.soundOn = isSoundsOn;
    }

    /**
     * Switch the soundOn state.
     * If true, we will produce sound ; if false, we won't.
     * Each change of state is notified to all observers.
     */
    @Override
    public void switchMute() {
        soundOn = !soundOn;
    }

    @Override
    public void playSound(Sounds sound) {
        if (!soundOn || settingUp) {
            return;
        }
        doPlaySound(sound);
    }

    /**
     * Does the real work of playing sounds
     *
     * @param sound
     *            the sound to play
     */
    protected abstract void doPlaySound(Sounds sound);

}

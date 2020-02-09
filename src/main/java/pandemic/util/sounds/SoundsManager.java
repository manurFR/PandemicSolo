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
package pandemic.util.sounds;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import pandemic.model.objects.Card;
import pandemic.model.objects.PandemicObject;
import pandemic.util.DecksObserver;
import pandemic.util.GenericResourceProvider;
import pandemic.util.ResourceProvider;
import pandemic.util.RolesObserver;

/**
 * This class has a triple purpose : 
 * - It contains a top-level (static) private class SoundThread implementing the Runnable interface, 
 * so that the instances of SoundThread are the threads which effectively play the sounds.
 * - By extending AbstractSoundFactory and supplying a protected doPlaySound() method that is the only one 
 * allowed to created instances of SoundThread, it is the only place from which one can trigger sounds.
 * - By implementing DecksObserver and RolesObserver, it can be registered to the model to receive
 * fired game event and react by triggering the appropriate sounds.
 * 
 * Effectively, this is the central nexus for all sound processing.
 *
 * @author jancsoo
 * @author manur
 */
public class SoundsManager extends AbstractSoundsFactory implements DecksObserver, RolesObserver {

    /**********************
     *   DecksObserver    *
     **********************/
    
    @Override
    public void infectionCardDrawn(boolean isDrawnFromTop) {
        if (isDrawnFromTop) {
            Sounds[] coughSounds = { Sounds.COUGH1, Sounds.COUGH2, Sounds.COUGH3, Sounds.COUGH4 };

            Sounds randomCough = coughSounds[(int) (Math.random() * coughSounds.length)];
            playSound(randomCough);
        } else {
            playSound(Sounds.SIREN);
        }
    }

    @Override
    public void playerCardDrawn(Card newCard) {
        playSound(Sounds.FLIP);
        if (newCard.getType().equals(PandemicObject.Type.EPIDEMIC_CARD)) {
            playSound(Sounds.GONG);
        } else if (newCard.getType().equals(PandemicObject.Type.EMERGENCY_EVENT_CARD)) {
            playSound(Sounds.EMERGENCY);
        } else if (newCard.getType().equals(PandemicObject.Type.MUTATION_EVENT_CARD)) {
            playSound(Sounds.BUBBLE);
        }
    }

    @Override
    public void infectionDeckShuffled() {
        playSound(Sounds.SHUFFLE);
    }

    @Override
    public void infectionDeckCleared() {
        // No sound to emit there
    }

    @Override
    public void infectionDeckCardRemoved() {
        playSound(Sounds.FEELSBETTER);
    }

    @Override
    public void infectionDeckRearranged() {
        playSound(Sounds.RADAR);
    }

    /**********************
     *   RolesObserver    *
     **********************/
    
    @Override
    public void roleWillChange(int roleIndex) {
        playSound(Sounds.BLIP);
    }

    @Override
    public void roleHasChanged(int roleIndex) {
        playSound(Sounds.BLIP);
    }
    
	/**********************************************
	 *   By extension of AbstractSoundsFactory    *
	 **********************************************/

    /**
     * Plays the sound asked for, creating a thread. 
     * 
     * @param sound
     *            The sound, from the available choices in the Sounds enum above.
     */
    @Override
    protected void doPlaySound(Sounds sound) {
        Thread soundThread = new Thread(new SoundThread(sound.getWavFile()));
        soundThread.start();
    }
	
	/************************************************
	 * Runnable, instantiable inner top-level class *
	 ************************************************/
	private static class SoundThread implements Runnable{
		private String fileName;

		/**
		 * Do not instantiate this class directly. One must use the static
		 * method playSound() to create the thread using an instance of this
		 */
		private SoundThread(String fileName) {
			this.fileName = fileName;
		}

		/**
		 * The operational thread method, playing the sound effect
		 */
		@Override
		public void run() {
			try {
				ResourceProvider resourceProvider = new GenericResourceProvider();
				
				AudioInputStream audioInputStream = 
					AudioSystem.getAudioInputStream(
						resourceProvider.getAudioStream(this.fileName));
				
				AudioFormat audioFormat = audioInputStream.getFormat();
				DataLine.Info dataLineInfo = 
						new DataLine.Info(Clip.class, audioFormat);
				Clip clip = (Clip) AudioSystem.getLine(dataLineInfo);
				clip.open(audioInputStream);
				clip.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}

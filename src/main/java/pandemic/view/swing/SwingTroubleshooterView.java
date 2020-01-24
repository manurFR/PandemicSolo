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
package pandemic.view.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pandemic.BoardController;
import pandemic.model.PandemicModel;
import pandemic.view.TroubleshooterView;

/**
 * View class to create a window used by the Troubleshooter role to peek at the first few cards of the Infection deck
 *  (without changing their order). The number of cards to reveal is equal to the current Infection Rate, thus a maximum of 4.
 * 
 * @author jancsoo
 * @author manur
 */
public class SwingTroubleshooterView extends SwingView implements TroubleshooterView {
	
	JFrame troubleshooterFrame = null;
	
	private FlippableCard[] infectionCards;
	private int flipCount;
	
	/**
	 * Constructor
	 * We follow the MVC paradigm, and thus the view gets access to the model (to fetch data) and to the controller (to notify it of requests)
	 * @param controller
	 * @param model
	 */
	public SwingTroubleshooterView(BoardController controller, PandemicModel model) {
		super(controller, model);
	}
	
	/**
	 * Create the graphical window displaying the first 4 cards of the Infection deck, face down
	 */
	@Override
	public void createBoard() {
		troubleshooterFrame = new JFrame("Troubleshooter...");
		
		List<Integer> infectionDeck = getModel().getInfectionDeck();

		final int nbOfCards = Math.min(infectionDeck.size(), 4); // final is necessary to use the variable inside the doneButton listener

		// Panel to hold the components
		JPanel cardsPanel = new JPanel();
		cardsPanel.setBackground(new Color(208, 185, 141));
		cardsPanel.setLayout(null);
		
		StringBuilder sb = new StringBuilder("Troubleshooter capability used. Next " + nbOfCards + " cards displayed: ");
		
		// Display the cards, face down, and add them to the panel
		infectionCards = new FlippableCard[nbOfCards];
		ImageIcon cardBackImage = getResourceProvider().getIcon("cardinf300.jpg");
		for (int cardIndex=0; cardIndex < nbOfCards; cardIndex++) {
			
			sb.append(infectionDeck.get(cardIndex));
			if (infectionDeck.get(cardIndex) == 100 || infectionDeck.get(cardIndex) == 101) {
				sb.append(" MUTATION! ");
			} else {
				sb.append(" ");
				sb.append(getModel().getCityList().get(infectionDeck.get(cardIndex)).getName());
				sb.append(" ");
			}
			
			ImageIcon cardFrontImage = getResourceProvider().getIcon("cardinf" + infectionDeck.get(cardIndex) + ".jpg");
			infectionCards[cardIndex] = new FlippableCard(cardBackImage, cardFrontImage);
			infectionCards[cardIndex].placeFaceDown(10 + cardIndex*102, 10, 100, 70);
			cardsPanel.add(infectionCards[cardIndex].getSwingComponent());
		}
		logger.info(sb.toString());
		
		// Set the count of cards flipped to zero
		flipCount = 0;
		
		// Instructions
		JLabel troubleshooterInstruction1 = new JLabel("Flip the number of cards equal to the");
		troubleshooterInstruction1.setForeground(new Color(255, 255, 206));
		troubleshooterInstruction1.setFont(new Font("Sanserif", Font.BOLD, 12));
		troubleshooterInstruction1.setBounds(145, 85, 400, 25);

		JLabel troubleshooterInstruction2 = new JLabel("current Infection rate!");
		troubleshooterInstruction2.setForeground(new Color(255, 255, 206));
		troubleshooterInstruction2.setFont(new Font("Sanserif", Font.BOLD, 12));
		troubleshooterInstruction2.setBounds(145, 100, 400, 25);

		// Flip button
		JButton flipButton = new JButton("Flip next card!");
		flipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getController().flipNextCard();
			}
		});
		flipButton.setBounds(10, 90, 120, 30);
		
		// Add the rest of the components to the panel
		cardsPanel.add(troubleshooterInstruction1);
		cardsPanel.add(troubleshooterInstruction2);
		cardsPanel.add(flipButton);

		// Add the panel to the frame and display it
		troubleshooterFrame.add(cardsPanel);
		
		troubleshooterFrame.setSize(new Dimension(435, 165));
		troubleshooterFrame.setLocation(167, 290);
		troubleshooterFrame.setVisible(true);
		troubleshooterFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
	}
	
	/**
	 * Turn the next unflipped card
	 */
	public void flipNextCard() {
		// If all available cards have already been flipped, do nothing
		if (flipCount >= infectionCards.length) {
			return;
		}
		infectionCards[flipCount++].flip();
	}
	
	/**
	 * Private class to hold both sides of an infection card, and to provide a "flip" functionality.
	 * This class should probably be part of the model, though.
	 *  
	 * @author manur
	 */
	private class FlippableCard {
		private JLabel swingComponent;
		private ImageIcon faceDown;
		private ImageIcon faceUp;
		
		public FlippableCard(ImageIcon faceDown, ImageIcon faceUp) {
			this.faceDown = faceDown;
			this.faceUp = faceUp;
		}
		
		public void placeFaceDown(int x, int y, int width, int height) {
			swingComponent = new JLabel(faceDown);
			swingComponent.setBounds(x, y, width, height);
		}
		
		public void flip() {
			swingComponent.setIcon(faceUp);
		}
		
		public JLabel getSwingComponent() {
			return swingComponent;
		}		
	}

}

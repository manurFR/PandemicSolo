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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import pandemic.BoardController;
import pandemic.dialog.ModalDialogsManager;
import pandemic.model.PandemicModel;
import pandemic.view.ForecastView;
import pandemic.view.listener.ForecastDoneButtonListener;

/**
 * Pop-up for the Forecast special event, that permits a player to reorder the first 
 *  6 cards of the Infection deck.
 * 
 * @author jancsoo
 * @author ebizieau
 */
public class SwingForecastView extends SwingView implements ForecastView {

	Container container;
	
	JFrame forecastFrame = null;
	JTextField inputForecast = new JTextField(5);
		
	/**
	 * Constructor
	 * We follow the MVC paradigm, and thus the view gets access to the model (to fetch data) and to the controller (to notify it of requests)
     * @param container The owner Pane
	 * @param controller
	 * @param model
	 */
	public SwingForecastView(Container container, BoardController controller, PandemicModel model) {
		super(controller, model);
		this.container = container;
	}
	
	/**
	 * Create the graphical window displaying the first 6 cards of the Infection deck
	 *  (or all cards of the deck if there are less than 6).
	 */
	@Override
	public void createBoard() {
		forecastFrame = new JFrame("Forecast...");
		
		List<Integer> infectionDeck = getModel().getInfectionDeck();

		final int nbOfCards = Math.min(infectionDeck.size(), 6); // final is necessary to use the variable inside the doneButton listener

		// Mail panel
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(new Color(208, 185, 141));
		mainPanel.setLayout(null);

		// Display cards
		for (int cardIndex=0; cardIndex < nbOfCards; cardIndex++) {
			ImageIcon cardImage = getResourceProvider().getIcon("cardinf" + infectionDeck.get(cardIndex) + ".jpg");
			JLabel cardLabel = new JLabel(cardImage);
			cardLabel.setBounds(10 + 102*cardIndex, 10, 100, 70);
			
			JLabel cardLetter = new JLabel(Character.toString((char)('A' + cardIndex)));
			cardLetter.setForeground(new Color(166, 140, 74));;
			cardLetter.setFont(new Font("Sanserif", Font.BOLD, 15));
			cardLetter.setBounds(57 + 102*cardIndex, 80, 25, 25); // Shift of 47 pixels to center the letter under the card
			
			mainPanel.add(cardLabel);
			mainPanel.add(cardLetter);
		}
		
		// Display the special event card and the instructions
		ImageIcon forecastImage = getResourceProvider().getIcon("forecast_picture.jpg");
		JLabel forecastImageLabel = new JLabel(forecastImage);
		forecastImageLabel.setBackground(new Color(208, 185, 141));
		forecastImageLabel.setBounds(23, 112, 190, 30);

		JLabel forecastImageInstruction1 = new JLabel(
				"Set the order of cards by typing the corresponding CAPITAL letters!");
		forecastImageInstruction1.setForeground(new Color(255, 255, 206));
		forecastImageInstruction1.setFont(new Font("Sanserif", Font.BOLD, 12));
		forecastImageInstruction1.setBounds(24, 145, 500, 25);

		JLabel forecastImageInstruction2 = new JLabel(
				"No commas, slashes or any additional characters needed!");
		forecastImageInstruction2.setForeground(new Color(255, 255, 206));
		forecastImageInstruction2.setFont(new Font("Sanserif", Font.BOLD, 12));
		forecastImageInstruction2.setBounds(24, 158, 400, 25);

		JLabel forecastImageInstruction3 = new JLabel(
				"For example:   BCFAED");
		forecastImageInstruction3.setForeground(new Color(255, 255, 206));
		forecastImageInstruction3.setFont(new Font("Sanserif", Font.BOLD, 12));
		forecastImageInstruction3.setBounds(24, 171, 400, 25);

		// Panel to hold the input field
		JPanel textPanel = new JPanel();
		textPanel.setBackground(new Color(215, 201, 166));
		textPanel.setBounds(240, 112, 80, 30);

		textPanel.add(inputForecast);

		// Button to validate
		JButton doneButton = new JButton("Done");
		doneButton.addActionListener(
				new ForecastDoneButtonListener(
						inputForecast, nbOfCards, getController(), 
						new ModalDialogsManager(container, getResourceProvider(), null)));
		doneButton.setBounds(339, 112, 75, 30);
		
		// Add components on the panel
		mainPanel.add(forecastImageLabel);
		mainPanel.add(forecastImageInstruction1);
		mainPanel.add(forecastImageInstruction2);
		mainPanel.add(forecastImageInstruction3);
	
		mainPanel.add(textPanel);
		mainPanel.add(doneButton);

		// Scroll pane to hold the panel
		JScrollPane scrollPane = new JScrollPane(mainPanel);

		forecastFrame.add(scrollPane);
		
		forecastFrame.setSize(new Dimension(640, 240));
		forecastFrame.setLocation(244, 290);
		forecastFrame.setVisible(true);
		forecastFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	/**
	 * Close the window
	 */
	@Override
	public void closeView() {
		forecastFrame.dispose();
		forecastFrame = null;
	}
}
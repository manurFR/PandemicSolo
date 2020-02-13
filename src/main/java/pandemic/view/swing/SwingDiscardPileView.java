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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import pandemic.BoardController;
import pandemic.model.PandemicModel;
import pandemic.util.ResourceProvider;
import pandemic.view.DiscardPileView;

/**
 * Pop-up for the Resilient Population special event, that permits a player to suppress
 *  a card from the Infection deck.
 * 
 * @author jancsoo
 * @author ebizieau
 */
public class SwingDiscardPileView extends SwingView implements DiscardPileView {

	private JFrame discardFrame = null;
	private JTextField resilientPopulationTextField = new JTextField(2);
		
	/**
	 * Constructor
	 * We follow the MVC paradigm, and thus the view gets access to the model (to fetch data) and to the controller (to notify it of requests)
	 * @param controller
	 * @param model
	 */
	public SwingDiscardPileView(BoardController controller, PandemicModel model) {
		super(controller, model);
	}

	/**
	 * Create the graphical window displaying the content of the discarded Infection cards,
	 *  and letting the user remove one of them. 
	 */
	@Override
	public void createBoard() {
		List<Integer> discardedCards = getModel().getDiscardPile();
		
		discardFrame = new JFrame("Discarded Infection Cards");
		
		// This window is created to implement the special event card "Resilient Population".
		//  Place a reminder of this card on the frame
		ImageIcon resilientPopulationImage = getResourceProvider().getIcon("resilientpop_picture.jpg");
		JLabel resilientPopulationLabel = new JLabel(resilientPopulationImage);
		resilientPopulationLabel.setBackground(new Color(215, 201, 166));
		resilientPopulationLabel.setBounds(23, 112, 190, 30);

		// Interactive panel to type the card to remove
		JPanel resilientPopulationTextPanel = new JPanel();
		resilientPopulationTextPanel.setBackground(new Color(208, 185, 141));
		resilientPopulationTextPanel.setBounds(240, 112, 40, 30);

		resilientPopulationTextPanel.add(resilientPopulationTextField);

		// Button to execute the removal
		JButton remove = new JButton("Remove");
		remove.setBounds(299, 112, 85, 30);
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = resilientPopulationTextField.getText();
				try {
					getController().removeDiscardedCard(Integer.parseInt(text)-1);
				} catch (NumberFormatException exception) {
					// Do nothing for now
				}
			}
		});

		// Text reminder of the Resilient Population card instructions
		JLabel resilientPopulationInstructions = new JLabel(
				"Enter the number of the Infection card that you would like to remove.");
		resilientPopulationInstructions.setForeground(new Color(166, 140, 74));
		resilientPopulationInstructions.setFont(new Font("Sanserif", Font.BOLD, 12));
		resilientPopulationInstructions.setBounds(24, 145, 500, 25);

		JLabel mutationCardsWarning = new JLabel(
				"Remember, that MUTATION! cards may not be removed.");
		mutationCardsWarning.setForeground(new Color(122, 63, 131));
		mutationCardsWarning.setFont(new Font("Sanserif", Font.BOLD, 12));
		mutationCardsWarning.setBounds(24, 160, 400, 25);

		// Set up the panel to display the cards
		JPanel discardedCardsPanel = createDiscardPanel(discardedCards, getResourceProvider());

		// Set up the elements on the panel
		discardedCardsPanel.add(resilientPopulationLabel);
		discardedCardsPanel.add(resilientPopulationTextPanel);
		discardedCardsPanel.add(remove);
		discardedCardsPanel.add(resilientPopulationInstructions);
		discardedCardsPanel.add(mutationCardsWarning);
		
		// A scrollpane inside the frame to place the discarded cards panel
		JScrollPane scrollPane = new JScrollPane(
				discardedCardsPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		scrollPane.setBounds(2, 2, 100, 260);
		discardFrame.add(scrollPane);

		// Size and display the frame
		discardFrame.setSize(new Dimension(550, 250));
		discardFrame.setLocation(334, 460);
		discardFrame.setVisible(true);
		discardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
	}
	
	/**
	 * Prepare the panel displaying the discarded cards
	 * @param discardedCards The List of the discarded cards
	 * @param resourceProvider ResourceProvider to get the image files as resources 
	 * @return The prepared JPanel
	 */
	public JPanel createDiscardPanel(List<Integer> discardedCards, ResourceProvider resourceProvider) {
		JPanel discardedCardsPanel = new JPanel();

		discardedCardsPanel.setLayout(null);
		discardedCardsPanel.setBackground(new Color(215, 201, 166));

		// Calculated width : 102 pixels per card + 50 pixels of margins
		int discardedCardsPanelWidth = (discardedCards.size() * 102) + 50; 
		discardedCardsPanel.setPreferredSize(new Dimension(discardedCardsPanelWidth, 250));
		
		// Create the images and their label
		for (int cardIndex = 0; cardIndex < discardedCards.size(); cardIndex++) {
			ImageIcon cardImage = resourceProvider.getIcon("cardinf" + discardedCards.get(cardIndex) + ".jpg");
			JLabel cardLabel = new JLabel(cardImage);
			cardLabel.setBounds(10 + 102*cardIndex, 10, 100, 70);
			discardedCardsPanel.add(cardLabel);
			
			JLabel cardNumber = new JLabel(Integer.toString(cardIndex + 1));
			cardNumber.setForeground(new Color(166, 140, 74));
			cardNumber.setFont(new Font("Sanserif", Font.BOLD, 15));
			cardNumber.setBounds(57 + 102*cardIndex, 80, 25, 25); // 47 pixels of shift to center the number
			discardedCardsPanel.add(cardNumber);

		}
		
		return discardedCardsPanel;
	}
	
	/**
	 * Close the window
	 */
	@Override
	public void closeView() {
		discardFrame.dispose();
		discardFrame = null;
	}
}

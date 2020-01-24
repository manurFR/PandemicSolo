/**
 *   Copyright (C) 2011 Emmanuel Bizieau <manur@manur.org>
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
package pandemic.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;

import pandemic.BoardController;
import pandemic.dialog.DialogsManager;

/**
 * Action listener for ForecastView, to control the new order of the first 6 cards
 *  from the Infection deck.
 * 
 * @author manur
 * @since v2.6
 */
public class ForecastDoneButtonListener implements ActionListener {

	private JTextField inputField = null;
	private int nbOfCards;
	private BoardController controller;
	
	private final String WRONG_FORMAT_MESSAGE = "Wrong Forecast format!";
	private DialogsManager dialogsManager;
	
	public ForecastDoneButtonListener(JTextField inputField, int nbOfCards, BoardController controller, DialogsManager dialogsManager) {
		this.inputField = inputField;
		this.nbOfCards  = nbOfCards;
		this.controller = controller;
		
		this.dialogsManager = dialogsManager;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String newOrder = inputField.getText();
		
		if (newOrder.length() != nbOfCards) {
			dialogsManager.showAlert(WRONG_FORMAT_MESSAGE);
			return;
		}
		
		List<Integer> newCardOrder = new ArrayList<Integer>();
		
		// Loop over all letters and check they are in the allowed range
		for (int letter=0; letter < nbOfCards; letter++) {
			int cardIndex = (newOrder.charAt(letter) - 'A');
			if (cardIndex < 0 || cardIndex > (nbOfCards-1)) {
				dialogsManager.showAlert(WRONG_FORMAT_MESSAGE);
				return;
			}
			
			// Check duplicate letters (by comparing the current with all previously parsed letters)
			for (int j=0; j<letter; j++) {
				if (newCardOrder.get(j).equals(cardIndex)) {
					dialogsManager.showAlert(WRONG_FORMAT_MESSAGE);
					return;
				}
			}
			
			newCardOrder.add(cardIndex);
		}
		
		controller.rearrangeInfectionDeck(newCardOrder);
	}
}

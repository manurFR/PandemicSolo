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
package pandemic.dialog;

import pandemic.PandemicSolo;
import pandemic.model.DifficultyLevel;
import pandemic.model.Expansion;
import pandemic.util.GameConfig;
import pandemic.util.ResourceProvider;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedHashSet;
import java.util.Set;

import static pandemic.model.Expansion.*;

/**
 * This dialog asks the user for all game configuration information at once.
 * 
 * We will simulate a small MVC system inside this class to manage the use of the
 * dialog :
 * - The modelConfig object is the Model ;
 * - The view*() methods are the View ;
 * - The controller*() method constitute the Controller. 
 * 
 * @author manur
 * @since v2.7
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ConfigDialog extends JDialog {

	private static final long serialVersionUID = 28L;
	
	private static final int ICON_CENTER = 55;
	
	private GameConfig modelConfig;
	
	public JComboBox comboBoxNbOfRoles = new JComboBox();
	public JCheckBox chckbxAddNewRoles = new JCheckBox();
	public JComboBox comboBoxDifficultyLevel = new JComboBox();
	public JCheckBox chckbxEventsCore = new JCheckBox();
	public JCheckBox chckbxEventsOnTheBrink = new JCheckBox();
	public JCheckBox chckbxEventsInTheLab = new JCheckBox();
	public JCheckBox chckbxEventsStateOfEmergency = new JCheckBox();
	public JCheckBox chckbxSurvivalMode = new JCheckBox();
	public JCheckBox chckbxVirulentStrain = new JCheckBox();
	public JCheckBox chckbxMutation = new JCheckBox();
	
	/**
	 * Constructor. Make the dialog modal.
	 * Set up the "model" (ie list of options) of the two combo boxes.
	 * @param ownerFrame The owner Frame object of this dialog.
	 */
	public ConfigDialog(Frame ownerFrame) {
		super(ownerFrame, true);
				
		comboBoxNbOfRoles.setModel(new DefaultComboBoxModel(new Integer[] {2, 3, 4}));
		comboBoxDifficultyLevel.setModel(new DefaultComboBoxModel(DifficultyLevel.values()));
	}
	
	/**
	 * CONTROLLER
	 * Validate the choice by saving it in the model.
	 * Dispose the dialog before leaving.
	 */
	@SuppressWarnings("ConstantConditions")
	public void controllerValidateAndClose() {
		modelConfig.setNbOfRoles((Integer)comboBoxNbOfRoles.getSelectedItem());
		modelConfig.setUseAllRoles(chckbxAddNewRoles.isSelected());
		modelConfig.setDifficultyLevel((DifficultyLevel)comboBoxDifficultyLevel.getSelectedItem());

		final Set<Expansion> eventCardsExpansions = new LinkedHashSet<Expansion>();
		if (chckbxEventsCore.isSelected()) {
			eventCardsExpansions.add(CORE);
		}
		if (chckbxEventsOnTheBrink.isSelected()) {
			eventCardsExpansions.add(ON_THE_BRINK);
		}
		if (chckbxEventsInTheLab.isSelected()) {
			eventCardsExpansions.add(IN_THE_LAB);
		}
		if (chckbxEventsStateOfEmergency.isSelected()) {
			eventCardsExpansions.add(STATE_OF_EMERGENCY);
		}
		modelConfig.setEventCardsExpansions(eventCardsExpansions);

		modelConfig.setPlayVirulentStrain(chckbxVirulentStrain.isSelected());
		modelConfig.setPlayMutation(chckbxMutation.isSelected());
		modelConfig.setSurvivalMode(chckbxSurvivalMode.isSelected());

		this.dispose();
	}

	/**
	 * CONTROLLER
	 * Cancel the configuration informations already selected and return to the caller.
	 * The model GameConfig is set to null to warn the call it was cancelled.
	 */
	public void controllerCancel() {
		modelConfig = null;
		
		this.dispose();
	}
	
	/**
	 * VIEW
	 * Set the state of the graphical components with the state of the model
	 */
	public void viewRefresh() {
		comboBoxNbOfRoles.setSelectedItem(modelConfig.getNbOfRoles());
		chckbxAddNewRoles.setSelected(modelConfig.isUseAllRoles());
		comboBoxDifficultyLevel.setSelectedItem(modelConfig.getDifficultyLevel());
		chckbxEventsCore.setSelected(modelConfig.getEventCardsExpansions().contains(CORE));
		chckbxEventsOnTheBrink.setSelected(modelConfig.getEventCardsExpansions().contains(ON_THE_BRINK));
		chckbxEventsInTheLab.setSelected(modelConfig.getEventCardsExpansions().contains(IN_THE_LAB));
		chckbxEventsStateOfEmergency.setSelected(modelConfig.getEventCardsExpansions().contains(STATE_OF_EMERGENCY));
		chckbxVirulentStrain.setSelected(modelConfig.isPlayVirulentStrain());
		chckbxMutation.setSelected(modelConfig.isPlayMutation());
		chckbxSurvivalMode.setSelected(modelConfig.isSurvivalMode());
	}

	/**
	 * VIEW
	 * Set up the graphical components
	 * @param resourceProvider The ResourceProvider that can be queried to get icons
	 */
	public void viewCreateComponents(ResourceProvider resourceProvider) {
		JPanel contentPanel = new JPanel();

		setTitle("New game...");
		setBounds(0, 0, 700, 560);
		// Center the dialog relatively to the owner Frame
		setLocationRelativeTo(getOwner());

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblTitle = new JLabel("Pandemic Solitaire v. " + PandemicSolo.VERSION);
		lblTitle.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
		lblTitle.setBounds(28, 6, 294, 30);
		contentPanel.add(lblTitle);

		JLabel lblCredits1 = new JLabel("Solitaire adaption of the Matt Leacock game.");
		lblCredits1.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblCredits1.setBounds(28, 40, 294, 16);
		contentPanel.add(lblCredits1);

		JLabel lblCredits2 = new JLabel("Current version by Emmanuel \"manur\" Bizieau.");
		lblCredits2.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblCredits2.setBounds(28, 57, 362, 16);
		contentPanel.add(lblCredits2);

		// ** Icons **

		contentPanel.add(createIconLabel(resourceProvider.getIcon("roleicon.jpg"), 100));
		contentPanel.add(createIconLabel(resourceProvider.getIcon("difficultyicon.jpg"), 165));
		contentPanel.add(createIconLabel(resourceProvider.getIcon("speceventicon.jpg"), 250));
		contentPanel.add(createIconLabel(resourceProvider.getIcon("virulenticon.jpg"), 335));
		contentPanel.add(createIconLabel(resourceProvider.getIcon("mutationicon.jpg"), 420));

		// ** Input fields **

		// Roles

		JLabel lblNumberOfRoles = new JLabel("Number of roles :");
		lblNumberOfRoles.setBounds(116, 120, 110, 16);
		contentPanel.add(lblNumberOfRoles);

		comboBoxNbOfRoles.setBounds(231, 115, 64, 27);
		contentPanel.add(comboBoxNbOfRoles);

		chckbxAddNewRoles.setText("Add new roles");
		chckbxAddNewRoles.setBounds(299, 115, 188, 23);
		contentPanel.add(chckbxAddNewRoles);

		// Difficulty

		JLabel lblDifficultyLevel = new JLabel("Difficulty level :");
		lblDifficultyLevel.setBounds(116, 194, 110, 16);
		contentPanel.add(lblDifficultyLevel);

		comboBoxDifficultyLevel.setBounds(231, 189, 251, 27);
		contentPanel.add(comboBoxDifficultyLevel);

		// Event Cards

		JLabel lblEventCards = new JLabel("Event Cards :");
		lblEventCards.setBounds(116, 250, 110, 16);
		contentPanel.add(lblEventCards);

		chckbxEventsCore.setText(CORE.getLabel());
		chckbxEventsCore.setBounds(116, 275, 80, 23);
		contentPanel.add(chckbxEventsCore);

		chckbxEventsOnTheBrink.setText(ON_THE_BRINK.getLabel());
		chckbxEventsOnTheBrink.setBounds(200, 275, 100, 23);
		contentPanel.add(chckbxEventsOnTheBrink);

		chckbxEventsInTheLab.setText(IN_THE_LAB.getLabel());
		chckbxEventsInTheLab.setBounds(320, 275, 100, 23);
		contentPanel.add(chckbxEventsInTheLab);

		chckbxEventsStateOfEmergency.setText(STATE_OF_EMERGENCY.getLabel());
		chckbxEventsStateOfEmergency.setBounds(420, 275, 150, 23);
		contentPanel.add(chckbxEventsStateOfEmergency);

		chckbxSurvivalMode.setText("Survival Mode (no event or role that could influence the roles or the decks)");
		chckbxSurvivalMode.setBounds(130, 300, 500, 23);
		contentPanel.add(chckbxSurvivalMode);

		// Challenges

		chckbxVirulentStrain.setText("Add the VIRULENT STRAIN challenge");
		chckbxVirulentStrain.setBounds(116, 363, 280, 23);
		contentPanel.add(chckbxVirulentStrain);

		chckbxMutation.setText("Add the MUTATION challenge");
		chckbxMutation.setBounds(116, 430, 222, 23);
		contentPanel.add(chckbxMutation);

		// Button Pane

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("Play !");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controllerValidateAndClose();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controllerCancel();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		// Closing the dialog is the same as clicking on Cancel
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				controllerCancel();
			}
		});
	}
	
	/**
	 * Creates and returns a JLabel displaying an image icon.
	 *  The x coordinate is calculated to center the image on ICON_CENTER ;
	 *  The y coordinate is given as argument.
	 * @param icon The ImageIcon to prepare as a JLabel and to place
	 * @param y Y coordinate where to place the label
	 * @return The constructed JLabel
	 */
	private JLabel createIconLabel(ImageIcon icon, int y) {
		JLabel label = new JLabel(icon);
		label.setBounds(ICON_CENTER - icon.getIconWidth() / 2, y, icon.getIconWidth(), icon.getIconHeight());
		return label;
	}
	
	// ** Model accessor ***
	
	public void setModelConfig(GameConfig modelConfig) {
		this.modelConfig = modelConfig;
	}

	public GameConfig getModelConfig() {
		return modelConfig;
	}

}

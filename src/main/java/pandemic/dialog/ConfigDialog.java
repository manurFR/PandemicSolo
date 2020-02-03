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
	public JCheckBox chckbxRolesCore = new JCheckBox();
	public JCheckBox chckbxRolesOnTheBrink = new JCheckBox();
	public JCheckBox chckbxRolesInTheLab = new JCheckBox();
	public JCheckBox chckbxRolesStateOfEmergency = new JCheckBox();
	public JComboBox comboBoxDifficultyLevel = new JComboBox();
	public JRadioButton rbEventsTwoPerPlayers = new JRadioButton();
	public JRadioButton rbEventsAlwaysFive = new JRadioButton();
	public JCheckBox chckbxEventsCore = new JCheckBox();
	public JCheckBox chckbxEventsOnTheBrink = new JCheckBox();
	public JCheckBox chckbxEventsInTheLab = new JCheckBox();
	public JCheckBox chckbxEventsStateOfEmergency = new JCheckBox();
	public JCheckBox chckbxSurvivalMode = new JCheckBox();
	public JCheckBox chckbxVirulentStrain = new JCheckBox();
	public JCheckBox chckbxMutation = new JCheckBox();

	final JLabel lblRolesWarning = new JLabel("There are not enough roles to choose from. Please select more expansions.");

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
		final Set<Expansion> rolesExpansions = new LinkedHashSet<Expansion>();
		if (chckbxRolesCore.isSelected()) {
			rolesExpansions.add(CORE);
		}
		if (chckbxRolesOnTheBrink.isSelected()) {
			rolesExpansions.add(ON_THE_BRINK);
		}
		if (chckbxRolesInTheLab.isSelected()) {
			rolesExpansions.add(IN_THE_LAB);
		}
		if (chckbxRolesStateOfEmergency.isSelected()) {
			rolesExpansions.add(STATE_OF_EMERGENCY);
		}

		// validate that the chosen expansions include enough roles for the required number of players (State of Emergency includes only 3 roles)
		int nbOfCandidateRoles = 0;
		for (Expansion expansion: rolesExpansions) {
			nbOfCandidateRoles += expansion.getRoles().size();
		}
		if (nbOfCandidateRoles < modelConfig.getNbOfRoles()) {
			lblRolesWarning.setVisible(true);
			return;
		}
		// validation OK

		modelConfig.setRolesExpansions(rolesExpansions);

		modelConfig.setDifficultyLevel((DifficultyLevel)comboBoxDifficultyLevel.getSelectedItem());
		modelConfig.setFiveEvents(rbEventsAlwaysFive.isSelected());

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

		chckbxRolesCore.setSelected(modelConfig.getRolesExpansions().contains(CORE));
		chckbxRolesOnTheBrink.setSelected(modelConfig.getEventCardsExpansions().contains(ON_THE_BRINK));
		chckbxRolesInTheLab.setSelected(modelConfig.getEventCardsExpansions().contains(IN_THE_LAB));
		chckbxRolesStateOfEmergency.setSelected(modelConfig.getEventCardsExpansions().contains(STATE_OF_EMERGENCY));

		comboBoxDifficultyLevel.setSelectedItem(modelConfig.getDifficultyLevel());
		rbEventsTwoPerPlayers.setSelected(!modelConfig.isFiveEvents());
		rbEventsAlwaysFive.setSelected(modelConfig.isFiveEvents());

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
		lblNumberOfRoles.setBounds(116, 95, 110, 16);
		contentPanel.add(lblNumberOfRoles);

		JLabel lblRolesFrom = new JLabel("from :");
		lblRolesFrom.setBounds(116, 120, 110, 16);
		contentPanel.add(lblRolesFrom);

		lblRolesWarning.setBounds(170, 120, 500, 16);
		lblRolesWarning.setForeground(Color.RED);
		lblRolesWarning.setVisible(false);
		contentPanel.add(lblRolesWarning);

		comboBoxNbOfRoles.setBounds(231, 90, 64, 27);
		contentPanel.add(comboBoxNbOfRoles);

		final ActionListener hideWarningActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (((JCheckBox) e.getSource()).isSelected()) {
					lblRolesWarning.setVisible(false);
				}
			}
		};

		chckbxRolesCore.setText(CORE.getLabel());
		chckbxRolesCore.setBounds(116, 140, 80, 23);
		chckbxRolesCore.addActionListener(hideWarningActionListener);
		contentPanel.add(chckbxRolesCore);

		chckbxRolesOnTheBrink.setText(ON_THE_BRINK.getLabel());
		chckbxRolesOnTheBrink.setBounds(200, 140, 100, 23);
		chckbxRolesOnTheBrink.addActionListener(hideWarningActionListener);
		contentPanel.add(chckbxRolesOnTheBrink);

		chckbxRolesInTheLab.setText(IN_THE_LAB.getLabel());
		chckbxRolesInTheLab.setBounds(320, 140, 100, 23);
		chckbxRolesInTheLab.addActionListener(hideWarningActionListener);
		contentPanel.add(chckbxRolesInTheLab);

		chckbxRolesStateOfEmergency.setText(STATE_OF_EMERGENCY.getLabel());
		chckbxRolesStateOfEmergency.setBounds(420, 140, 150, 23);
		chckbxRolesStateOfEmergency.addActionListener(hideWarningActionListener);
		contentPanel.add(chckbxRolesStateOfEmergency);

		// Difficulty

		JLabel lblDifficultyLevel = new JLabel("Difficulty level :");
		lblDifficultyLevel.setBounds(116, 194, 110, 16);
		contentPanel.add(lblDifficultyLevel);

		comboBoxDifficultyLevel.setBounds(231, 189, 251, 27);
		contentPanel.add(comboBoxDifficultyLevel);

		// Event Cards

		JLabel lblEventCards = new JLabel("Event Cards :");
		lblEventCards.setBounds(116, 245, 110, 16);
		contentPanel.add(lblEventCards);

		rbEventsTwoPerPlayers.setText("2 cards per player");
		rbEventsTwoPerPlayers.setBounds(210, 242, 130, 23);
		contentPanel.add(rbEventsTwoPerPlayers);

		rbEventsAlwaysFive.setText("Always 5 cards");
		rbEventsAlwaysFive.setBounds(350, 242, 130, 23);
		contentPanel.add(rbEventsAlwaysFive);

		ButtonGroup groupNbEventCards = new ButtonGroup();
		groupNbEventCards.add(rbEventsTwoPerPlayers);
		groupNbEventCards.add(rbEventsAlwaysFive);

		JLabel lblEventsFrom = new JLabel("from :");
		lblEventsFrom.setBounds(116, 265, 110, 16);
		contentPanel.add(lblEventsFrom);

		chckbxEventsCore.setText(CORE.getLabel());
		chckbxEventsCore.setBounds(116, 285, 80, 23);
		contentPanel.add(chckbxEventsCore);

		chckbxEventsOnTheBrink.setText(ON_THE_BRINK.getLabel());
		chckbxEventsOnTheBrink.setBounds(200, 285, 100, 23);
		contentPanel.add(chckbxEventsOnTheBrink);

		chckbxEventsInTheLab.setText(IN_THE_LAB.getLabel());
		chckbxEventsInTheLab.setBounds(320, 285, 100, 23);
		contentPanel.add(chckbxEventsInTheLab);

		chckbxEventsStateOfEmergency.setText(STATE_OF_EMERGENCY.getLabel());
		chckbxEventsStateOfEmergency.setBounds(420, 285, 150, 23);
		contentPanel.add(chckbxEventsStateOfEmergency);

		chckbxSurvivalMode.setText("Survival Mode (exclude events that could influence the roles or the decks)");
		chckbxSurvivalMode.setBounds(130, 310, 500, 23);
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

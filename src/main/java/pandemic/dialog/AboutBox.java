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

import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import pandemic.GameManager;
import pandemic.PandemicSolo;
import pandemic.util.ResourceProvider;

/**
 * About box
 * 
 * @author manur
 * @since v2.7
 */
public class AboutBox extends JDialog {
	private static final long serialVersionUID = 28L;

	private GameManager gameManager;
	
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public AboutBox(Frame owner) {
		super(owner, true); // Modal dialog
	}
	
	/**
	 * Display the graphical components
	 * @param resourceProvider The ResourceProvider that will be queried to get the icons
	 */
	public void createComponents(ResourceProvider resourceProvider) {
		setBounds(0, 0, 520, 520);
		setLocationRelativeTo(getOwner());

		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 480, 420);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		getContentPane().add(contentPanel);

		ImageIcon iconCube = resourceProvider.getIcon("cube_blue.jpg");
		JLabel labelCube1 = new JLabel(iconCube);
		labelCube1.setBounds(36, 58, iconCube.getIconWidth(), iconCube.getIconHeight());
		JLabel labelCube2 = new JLabel(iconCube);
		labelCube2.setBounds(26, 74, iconCube.getIconWidth(), iconCube.getIconHeight());
		JLabel labelCube3 = new JLabel(iconCube);
		labelCube3.setBounds(46, 74, iconCube.getIconWidth(), iconCube.getIconHeight());

		contentPanel.add(labelCube1);
		contentPanel.add(labelCube2);
		contentPanel.add(labelCube3);

		ImageIcon iconPawn = resourceProvider.getIcon("pawn_troubleshooter.jpg");
		JLabel labelPawn = new JLabel(iconPawn);
		labelPawn.setBounds(120, 60, iconPawn.getIconWidth(), iconPawn.getIconHeight());

		contentPanel.add(labelPawn);

		ImageIcon iconRemedy = resourceProvider.getIcon("cure_yellow.jpg");
		JLabel labelRemedy = new JLabel(iconRemedy);
		labelRemedy.setBounds(15, 15, iconRemedy.getIconWidth(), iconRemedy.getIconHeight());

		contentPanel.add(labelRemedy);

		ImageIcon iconInfection = resourceProvider.getIcon("infection_token.jpg");
		JLabel labelInfection = new JLabel(iconInfection);
		labelInfection.setBounds(80, 60, iconInfection.getIconWidth(), iconInfection.getIconHeight());

		contentPanel.add(labelInfection);

		ImageIcon iconEpidemic = resourceProvider.getIcon("card0.jpg");
		JLabel labelEpidemic = new JLabel(iconEpidemic);
		labelEpidemic.setBounds(10, 35, iconEpidemic.getIconWidth(), iconEpidemic.getIconHeight());

		contentPanel.add(labelEpidemic);

		ImageIcon iconParis = resourceProvider.getIcon("card35.jpg");
		JLabel labelParis = new JLabel(iconParis);
		labelParis.setBounds(30, 10, iconParis.getIconWidth(), iconParis.getIconHeight());

		contentPanel.add(labelParis);

		JLabel lblPandemicSolitaire = new JLabel("Pandemic Solitaire");
		lblPandemicSolitaire.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		lblPandemicSolitaire.setBounds(222, 47, 180, 16);
		lblPandemicSolitaire.setHorizontalAlignment(JLabel.CENTER);
		contentPanel.add(lblPandemicSolitaire);

		JLabel lblVersion = new JLabel("version " + PandemicSolo.VERSION);
		lblVersion.setBounds(222, 70, 180, 16);
		lblVersion.setHorizontalAlignment(JLabel.CENTER);
		contentPanel.add(lblVersion);

		Font lucida12 = new Font("Lucida Grande", Font.PLAIN, 12);
		
		JTextPane txtpnPandemicAGame = new JTextPane();
		txtpnPandemicAGame.setFont(lucida12);
		txtpnPandemicAGame.setText("Computer adaptation by Emmanuel \"manur\" Bizieau\nInitial adaptation by Andras \"jancsoo\" Domian\n\nPandemic, a game by Matt Leacock\nPandemic: On the Brink by Matt Leacock and Thomas Lehmann\nArt by Josh Cappel and R\u00E9gis Moulun\nPublished by Z-Man Games");
		txtpnPandemicAGame.setBackground(UIManager.getColor("Panel.background"));
		txtpnPandemicAGame.setEditable(false);
		txtpnPandemicAGame.setBounds(46, 122, 374, 120);
		contentPanel.add(txtpnPandemicAGame);
		
		JTextPane txtpnCurrentConfig = new JTextPane();
		txtpnCurrentConfig.setFont(lucida12);
		String[] configDetails = gameManager.getCurrentModel().getConfig().giveDetails();
		String config = "Current configuration :\n";
		for (String line : configDetails) {
			config += line + "\n";
		}
		txtpnCurrentConfig.setText(config);
		txtpnCurrentConfig.setBackground(UIManager.getColor("Panel.background"));
		txtpnCurrentConfig.setEditable(false);
		// get height dynamically depending on the content text
		txtpnCurrentConfig.setBounds(46, 245, 450, txtpnCurrentConfig.getPreferredSize().height);

		SimpleAttributeSet styleBold = new SimpleAttributeSet();
		StyleConstants.setBold(styleBold, true);
		txtpnCurrentConfig.getStyledDocument().setCharacterAttributes(0, 23, styleBold, false);

		contentPanel.add(txtpnCurrentConfig);

		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(0, 420, 480, 39);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AboutBox.this.dispose(); // Close the box
			}
		});

		JButton btnReadmetxt = new JButton("README.txt");
		btnReadmetxt.setActionCommand("OK");
		btnReadmetxt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File readmeFile;
					if (PandemicSolo.IS_WINDOWS) {
						// on windows, change line endings to CRLF
						BufferedReader reader = new BufferedReader(new FileReader("README.txt"));
						readmeFile = File.createTempFile("README_tmp", ".txt");
						BufferedWriter writer = new BufferedWriter(new FileWriter(readmeFile));

						String line;
						while ((line = reader.readLine()) != null) {
							writer.write(line);
							writer.newLine();
						}
						writer.close();
						reader.close();
					} else {
						readmeFile = new File("README.txt");
					}
					Desktop.getDesktop().edit(readmeFile);
				} catch (IOException exception) {
					JOptionPane
							.showMessageDialog(
									null,
									"Sorry, it doesn't work on your system. Please open the README.txt file from your file explorer.",
									"Failed to open README.txt",
									JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonPane.add(btnReadmetxt);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
}

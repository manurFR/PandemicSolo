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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pandemic.util.ResourceProvider;

/**
 * ActionListener for buttons which should display large frames over the game panel : instructions and appendix.
 * 
 * @author manur
 * @since v2.6
 */
public class OverframeButtonListener implements ActionListener {

	private String frameName;
	private String frameContentPath;
	
	private ResourceProvider resourceProvider;
	
	public OverframeButtonListener(String name, String contentPath, ResourceProvider resourceProvider) {
		this.frameName = name;
		this.frameContentPath = contentPath;
		this.resourceProvider = resourceProvider;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// A label for the image content
		ImageIcon content = resourceProvider.getIcon(frameContentPath);
		JLabel contentLabel = new JLabel(content);
		
		// A panel to put the JLabel
		JPanel contentPanel = new JPanel();
		contentPanel.add(contentLabel);

		// A ScrollPane to add scroll bars
		JScrollPane scrollPane = new JScrollPane(contentPanel);
		
		// At least, the frame, where we place the ScrollPane
		JFrame overFrame = new JFrame(frameName);
		overFrame.add(scrollPane);

		// Let's set common properties of the frame and display it
		overFrame.setSize(new Dimension(740, 600));
		overFrame.setLocation(168, 48);
		overFrame.setVisible(true);
		overFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

}

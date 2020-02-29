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

import java.awt.*;
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

	public static final int OVERFRAME_X_LOCATION = 168;

	public enum Overframe {
		INSTRUCTIONS("Quick Instructions", "instructions.jpg", 920),
		APPENDIX("Appendix", "appendix.jpg", 1500);

		private final String name;
		private final String image;
		private final int width;

		Overframe(String name, String image, int width) {
			this.name = name;
			this.image = image;
			this.width = width;
		}
	}

	private final Overframe overframe;
	private ResourceProvider resourceProvider;
	
	public OverframeButtonListener(Overframe overframe, ResourceProvider resourceProvider) {
		this.overframe = overframe;
		this.resourceProvider = resourceProvider;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// A label for the image content
		ImageIcon content = resourceProvider.getIcon(overframe.image);
		JLabel contentLabel = new JLabel(content);
		
		// A panel to put the JLabel
		JPanel contentPanel = new JPanel();
		contentPanel.add(contentLabel);

		// A ScrollPane to add scroll bars
		JScrollPane scrollPane = new JScrollPane(contentPanel);
		
		// At least, the frame, where we place the ScrollPane
		JFrame overFrame = new JFrame(overframe.name);
		overFrame.add(scrollPane);

		// If necessary, resize the theorical width of the frame so that it doesn't exceed the screen size
		int effectiveWidth = Math.min(overframe.width,
				new Double(Toolkit.getDefaultToolkit().getScreenSize().getWidth()).intValue() - OVERFRAME_X_LOCATION);
		overFrame.setSize(new Dimension(effectiveWidth, 800));
		overFrame.setLocation(OVERFRAME_X_LOCATION, 48);
		overFrame.setVisible(true);
		overFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

}

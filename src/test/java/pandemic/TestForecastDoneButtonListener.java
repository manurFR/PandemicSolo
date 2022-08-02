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
package pandemic;

import org.junit.Before;
import org.junit.Test;
import pandemic.dialog.DialogsManager;
import pandemic.view.listener.ForecastDoneButtonListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import static org.mockito.Mockito.*;

/** 
* @author manur
* @since v2.6
*/
public class TestForecastDoneButtonListener {

	private JTextField mockField;
	private BoardController mockController;
	private DialogsManager mockAlert;
	private ActionEvent mockEvent;
	
	@Before
	public void setUp() {
		mockField = mock(JTextField.class);
		mockController = mock(BoardController.class);
		mockAlert = mock(DialogsManager.class);
		mockEvent = mock(ActionEvent.class);
	}
	
	@Test
	public void testActionPerformedEmptyField()
	{
		// given
		ForecastDoneButtonListener listener = new ForecastDoneButtonListener(mockField, 6, mockController, mockAlert);
		
		when(mockField.getText()).thenReturn("");
		
		// when
		listener.actionPerformed(mockEvent);
		
		// then
		verify(mockAlert).showAlert(anyString());
	}

	@Test
	public void testActionPerformedWrongLetter()
	{
		// given
		ForecastDoneButtonListener listener = new ForecastDoneButtonListener(mockField, 6, mockController, mockAlert);
		
		when(mockField.getText()).thenReturn("BFAGED");
		
		// when
		listener.actionPerformed(mockEvent);
		
		// then
		verify(mockAlert).showAlert(anyString());
	}

	@Test
	public void testActionPerformedDuplicateLetter()
	{
		// given
		ForecastDoneButtonListener listener = new ForecastDoneButtonListener(mockField, 4, mockController, mockAlert);
		
		when(mockField.getText()).thenReturn("ABCB");
		
		// when
		listener.actionPerformed(mockEvent);
		
		// then
		verify(mockAlert).showAlert(anyString());
	}

	@Test
	public void testActionPerformed()
	{
		// given
		ForecastDoneButtonListener listener = new ForecastDoneButtonListener(mockField, 5, mockController, mockAlert);
		
		when(mockField.getText()).thenReturn("BEADC");
		
		// when
		listener.actionPerformed(mockEvent);
		
		// then
		verify(mockController).rearrangeInfectionDeck(Arrays.asList(1, 4, 0, 3, 2));
	}

}

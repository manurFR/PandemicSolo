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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentMatchers;
import pandemic.dialog.DialogsManager;
import pandemic.model.BoardZone;
import pandemic.model.PandemicModel;
import pandemic.model.objects.PandemicObject;
import pandemic.model.objects.Role;
import pandemic.view.listener.NewAssignmentListener;

/** 
* @author manur
* @since v2.6
*/
public class TestNewAssignmentListener {

	private BoardController mockController;
	private PandemicModel mockModel;
	private DialogsManager mockManager;
	private ActionEvent mockEvent;
	
	private NewAssignmentListener listener;
	
	private List<PandemicObject> listRoles;
	
	@Before
	public void setUp() {
		mockController = mock(BoardController.class);
		mockModel = mock(PandemicModel.class);
		mockManager = mock(DialogsManager.class);
		
		when(mockModel.getNbOfRoles()).thenReturn(3);
		
		// Make model.getCountersLibrary return a List that contains only 11 roles
		listRoles = new ArrayList<PandemicObject>();
		for (int i=0; i<11; i++) {
			Role role = new Role(i, "R"+i, null, 0, 0, BoardZone.RESERVE);
			listRoles.add(role);
		}
		when(mockModel.getCountersLibrary()).thenReturn(listRoles);
		
		// Make model.getAffectedRoles return an array of three roles
		List<Role> affectedRoles = new ArrayList<Role>();
		affectedRoles.add((Role)listRoles.get(1));
		affectedRoles.add((Role)listRoles.get(3));
		affectedRoles.add((Role)listRoles.get(8));
		when(mockModel.getAffectedRoles()).thenReturn(affectedRoles);
		
		listener = new NewAssignmentListener(mockController, mockModel, mockManager);
		
		mockEvent = mock(ActionEvent.class);
	}
	
	@Test
	public void testActionPerformedDuplicateRole()
	{
		// First call 
		when(mockManager.chooseFromStrings(
				eq("New Assignment"),
				eq("Change the following role..."),
				any(String[].class),
				anyInt(),
				String.valueOf(ArgumentMatchers.<Boolean>isNull()))
		).thenReturn("Role 3");
		
		// The second call returns a duplicate
		when(mockManager.chooseFromStrings(
				eq("New Assignment"),
				eq("Change it to..."),
				any(String[].class),
				anyInt(),
				String.valueOf(ArgumentMatchers.<Boolean>isNull()))
		).thenReturn("R1"); // already affected
		
		listener.actionPerformed(mockEvent);
		verify(mockManager).showAlert("This role is already in use!");
	}

	@Test
	public void testActionPerformed()
	{
		// First call 
		when(mockManager.chooseFromStrings(
				eq("New Assignment"),
				eq("Change the following role..."),
				any(String[].class),
				anyInt(),
				String.valueOf(ArgumentMatchers.<Boolean>isNull()))
		).thenReturn("Role 3");
		
		// The second call returns a new role
		when(mockManager.chooseFromStrings(
				eq("New Assignment"),
				eq("Change it to..."),
				any(String[].class),
				anyInt(),
				String.valueOf(ArgumentMatchers.<Boolean>isNull()))
		).thenReturn("R4");
		
		listener.actionPerformed(mockEvent);
		
		verify(mockController).changeRole(2, (Role)listRoles.get(4)); // "Role 3" is index 2, and we replace it with "R4" which has an id of 4
	}

}

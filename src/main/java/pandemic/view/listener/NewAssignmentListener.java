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
package pandemic.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pandemic.BoardController;
import pandemic.dialog.DialogsManager;
import pandemic.model.PandemicModel;
import pandemic.model.objects.PandemicObject;
import pandemic.model.objects.Role;
import pandemic.util.RolesObserver;

/**
 * Listener for the New Assignment button, useful for the New Assignment special event.
 *  It queries the player for the role to change and the new role, performs controls
 *  and propagate the change to the controler.
 * 
 * @author jancsoo
 * @author manur
 */
public class NewAssignmentListener implements ActionListener {

    private static final Logger logger = LoggerFactory.getLogger(NewAssignmentListener.class);
    
	private BoardController controller;
	private PandemicModel model;
	
	private DialogsManager dialogsManager;
	
	private List<RolesObserver> rolesObservers = new ArrayList<RolesObserver>();
	
	public NewAssignmentListener(BoardController controller, PandemicModel model, DialogsManager dialogsManager) {
		this.controller = controller;
		this.model = model;
		
		this.dialogsManager = dialogsManager;
		//decksObservers.add(controller.getSoundsObserver());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	    for (RolesObserver rObs : rolesObservers) {
	        rObs.roleWillChange(-1);
	    }
	    
		int nbOfRoles = model.getNbOfRoles();
		
		// Select the role to change
		String[] roles = new String[nbOfRoles];
		for (int roleIndex=0; roleIndex<nbOfRoles; roleIndex++) {
			roles[roleIndex] = "Role " + (roleIndex+1);
		}
		
		String roleToChange = dialogsManager.chooseFromStrings("New Assignment", "Change the following role...", roles, 0, null);
		if (roleToChange == null) {
			return;
		}
		
		int indexOfRoleToChange = Arrays.binarySearch(roles, roleToChange);
		
		// Select the new role
		List<PandemicObject> allCounters = model.getCountersLibrary();
		List<Role> allRoles = new ArrayList<Role>();
		List<String> roleNames = new ArrayList<String>();
		int index = 0;
		for (PandemicObject counter : allCounters) {
			if (counter.getType().equals(PandemicObject.Type.PAWN)) {
				allRoles.add((Role)counter);
				roleNames.add(counter.getName());
			}
		}

		String newRoleName = dialogsManager.chooseFromStrings(
				"New Assignment", "Change it to...", roleNames.toArray(new String[0]), 0, null);
		if (newRoleName == null) {
			return;
		}
		
		// Get the corresponding id
		Role newRole = null;
		for (Role role : allRoles) {
			if (role.getName().equals(newRoleName)) {
				newRole = role;
				break;
			}
		}

		// Check if it's a role already affected
		List<Role> roleList = model.getAffectedRoles();
		for (Role role : roleList) {
			if (role.equals(newRole)) {
				dialogsManager.showAlert("This role is already in use!");
				return;
			}
		}
		
		logger.debug("Changed role #{} to {}", indexOfRoleToChange, newRoleName);

		// We can change the role
		controller.changeRole(indexOfRoleToChange, newRole);
		
        for (RolesObserver rObs : rolesObservers) {
            rObs.roleHasChanged(indexOfRoleToChange);
        }
	}
}
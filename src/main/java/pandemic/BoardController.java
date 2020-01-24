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

import java.util.List;

import pandemic.model.objects.Role;
import pandemic.util.ResourceProvider;
import pandemic.util.sounds.SoundsManager;
import pandemic.view.ViewFactory;

/**
 * Interface for the controller
 * 
 * @author manur
 * @since v2.6
 */
public interface BoardController {
	public void setUpModel();
	public void setUpView();
	
	public void drawPlayerCard();
	public void drawInfectionCard();
	public void drawBottomInfectionCard();
	public void reshuffleInfectionCards();
	public void showDiscardPile();
	public void removeDiscardedCard(int discardedCard);
	public void showForecastCards();
	public void rearrangeInfectionDeck(List<Integer> newCardOrder);
	public void showTroubleshooterCards();
	public void flipNextCard();
	public void changeRole(int roleIndex, Role newRole);
	
	public void switchMute();
	public boolean isSoundOn();
	
	public void setViewFactory(ViewFactory viewFactory);
	public void setResourceProvider(ResourceProvider resourceProvider);
	public void setSoundsManager(SoundsManager soundsManager);
}

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

import pandemic.model.PandemicModel;
import pandemic.model.objects.Role;
import pandemic.util.ResourceProvider;
import pandemic.util.sounds.SoundsManager;
import pandemic.view.BoardView;
import pandemic.view.DiscardPileView;
import pandemic.view.ForecastView;
import pandemic.view.TroubleshooterView;
import pandemic.view.ViewFactory;

/**
 * Actual controller
 * 
 * @author manur
 * @since v2.6
 */
public class DefaultBoardController implements BoardController {
	
	private BoardView view;
	private DiscardPileView discardView;
	private ForecastView swingForecastView;
	private TroubleshooterView swingTroubleshooterView;
	
	private PandemicModel model;
	
	private ViewFactory viewFactory;
    private ResourceProvider resourceProvider;
    private SoundsManager soundsManager;
	
	// ** Set up **
	
	public DefaultBoardController(PandemicModel model) {
		this.model = model;
	}
	
    @Override
	public void setUpModel() {
        // Turn off all sound before setting all elements of the game
        soundsManager.setSettingUp(true);
		model.initialize(resourceProvider);
		// Turn sound back on
		soundsManager.setSettingUp(false);
	}
	
	@Override
	public void setUpView() {
		view = viewFactory.createBoardView(this, model);
		model.registerDecksObserver(view);
		model.registerRolesObserver(view);
		view.createBoard();
		view.setTopDiscardCard();
	}
	
	// ** Sound management **
	
    @Override
    public void switchMute() {
        soundsManager.switchMute();        
    }

    @Override
    public boolean isSoundOn() {
        return soundsManager.isSoundOn();
    }
	
	// ** User operations **

	@Override
	public void drawPlayerCard() {
		if (model.getNbOfPlayerCards() == 0) {
			return;
		}
		model.drawPlayerCard();
	}

	@Override
	public void drawInfectionCard() {
		model.drawInfectionCard(); // and let the cough sound be heard
	}

	@Override
	public void drawBottomInfectionCard() {
		model.drawBottomInfectionCard();
	}

	@Override
	public void reshuffleInfectionCards() {
		model.reshuffleInfectionCards();

	}

	@Override
	public void showDiscardPile() {
		discardView = viewFactory.createDiscardPileView(this, model);
		discardView.createBoard();
	}

	@Override
	public void removeDiscardedCard(int discardedCardIndex) {
		if (discardedCardIndex < 0 || discardedCardIndex >= model.getDiscardPile().size()) {
			return;
		}
		
		boolean removalDone = model.removeDiscardedCard(discardedCardIndex);
		if (removalDone) {
			discardView.closeView();
			discardView = null;
		}
	}
	
	@Override
	public void showForecastCards() {
		swingForecastView = viewFactory.createForecastView(this, model);
		swingForecastView.createBoard();
	}
	
	@Override
	public void rearrangeInfectionDeck(List<Integer> newCardOrder) {
		model.rearrangeInfectionDeck(newCardOrder);
		swingForecastView.closeView();
		swingForecastView = null;
	}
	
	@Override
	public void showTroubleshooterCards() {
	    soundsManager.infectionDeckRearranged();
	    
		swingTroubleshooterView = viewFactory.createTroubleshooterView(this, model);
		swingTroubleshooterView.createBoard();
	}

	@Override
	public void flipNextCard() {
		// The "flipping" of a card does not change the model. It's only visual, so we ask it to the view.
		if (swingTroubleshooterView != null) {
			swingTroubleshooterView.flipNextCard();
		}		
	}

	@Override
	public void changeRole(int roleIndex, Role newRole) {
		model.changeRole(roleIndex, newRole);
	}

    // ** Accessors **
    
    @Override
    public void setResourceProvider(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    public void setSoundsManager(SoundsManager soundsManager) {
        this.soundsManager = soundsManager;
    }

    @Override
    public void setViewFactory(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;        
    }

}

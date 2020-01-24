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
package pandemic.view;

import pandemic.BoardController;
import pandemic.model.PandemicModel;
import pandemic.util.ResourceProvider;

/**
 * Factory for building View instances so core can remain platform neutral.
 *
 * @author Barrie Treloar
 */
public interface ViewFactory {

    /**
     * Create the board view.
     *
     * @param controller
     *            listens for the onBoardView events TODO: Create sub-interface
     * @param model
     *            the model for the view.
     * @return the view
     */
    public BoardView createBoardView(BoardController controller, PandemicModel model);

    /**
     * Create the discard pile view.
     *
     * @param controller
     *            listens for the onDiscardPile events TODO: Create
     *            sub-interface
     * @param model
     *            the model for the view.
     * @return the view
     */
    public DiscardPileView createDiscardPileView(BoardController controller, PandemicModel model);

    /**
     * Create the forecast view.
     *
     * @param controller
     *            listens for the onForecast events TODO: Create sub-interface
     * @param model
     *            the model for the view.
     * @return the view
     */
    public ForecastView createForecastView(BoardController controller, PandemicModel model);

    /**
     * Create the troubleshooter view.
     *
     * @param controller
     *            listens for the onTroubleshooter events TODO: Create
     *            sub-interface
     * @param model
     *            the model for the view.
     * @return the view
     */
    public TroubleshooterView createTroubleshooterView(BoardController controller, PandemicModel model);

    /**
     * Create the new assignment view.
     *
     * @param controller
     *            listens for the onNewAssignment events TODO: Create
     *            sub-interface
     * @param model
     *            the model for the view.
     * @return the view
     */
    //public NewAssignmentView createNewAssignmentView(BoardController controller, PandemicModel model);

    /**
     * Set the resource provider that will be used by the views
     * @param resourceProvider
     */
    public void setResourceProvider(ResourceProvider resourceProvider);
    
}

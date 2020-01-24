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

/**
 * View for the Forecast special event, that permits a player to reorder the
 * first 6 cards of the Infection deck.
 *
 * @author jancsoo
 * @author ebizieau
 */
public interface ForecastView {

    /**
     * Create the view displaying the first 6 cards of the Infection deck (or
     * all cards of the deck if there are less than 6).
     */
    public void createBoard();

    /**
     * Close the view.
     */
    public void closeView();
}
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

import pandemic.util.DecksObserver;
import pandemic.util.RolesObserver;

/**
 * The main "view" (from the MVC pattern) for the application. It displays the
 * board, knows how to set it up, and is the observer for decks and roles
 * modifications, to which it will react.
 *
 * @author jancsoo
 * @author manur
 */
public interface BoardView extends DecksObserver, RolesObserver {
    /**
     * Create the board
     */
    public void createBoard();

    /**
     * Change the card shown at the top of the discard pile with the new card on that pile.
     */
    public void setTopDiscardCard();
    
}
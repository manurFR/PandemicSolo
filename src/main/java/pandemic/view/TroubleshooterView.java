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
 * View for the Troubleshooter role to peek at the first few cards of the
 * Infection deck (without changing their order). The number of cards to reveal
 * is equal to the current Infection Rate, thus a maximum of 4.
 *
 * @author jancsoo
 * @author manur
 */
public interface TroubleshooterView {

    /**
     * Create the view displaying the first 4 cards of the Infection deck, face
     * down.
     */
    public void createBoard();

    /**
     * Turn the next unflipped card.
     */
    public void flipNextCard();
}

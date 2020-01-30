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
package pandemic.view.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pandemic.BoardController;
import pandemic.dialog.ModalDialogsManager;
import pandemic.model.ComponentsFactory;
import pandemic.model.Expansion;
import pandemic.model.PandemicModel;
import pandemic.model.objects.Card;
import pandemic.model.objects.PandemicObject;
import pandemic.model.objects.Role;
import pandemic.view.BoardView;
import pandemic.view.listener.NewAssignmentListener;
import pandemic.view.listener.OverframeButtonListener;

import static pandemic.model.objects.Role.TROUBLESHOOTER_ROLE_ID;

/**
 * The main "view" (from the MVC pattern) for the application. This class owns
 * the main graphical panel, displaying the board, knows how to set it up, and
 * is the observer for decks and roles modifications, to which it will react.
 * 
 * @author jancsoo
 * @author manur
 */
public class SwingBoardView extends SwingView implements BoardView {

    private static final int ROLEBOX_WIDTH = 130;
    private static final int ROLEBOX_HEIGHT = 30;

    private JScrollPane container;
    private JPanel board = null;

    private JButton muteButton = null;
    private ImageIcon soundOnIcon;
    private ImageIcon soundOffIcon;

    private JLabel remainingCards = null;
    private JLabel discardPile = null;

    private JButton reshuffleButton = null;
    private JButton discardButton = null;
    private JButton troubleshooterButton = null;

    /**
     * Constructor We follow the MVC paradigm, and thus the view gets access to
     * the model (to fetch data) and to the controller (to notify it of
     * requests)
     */
    public SwingBoardView(JScrollPane container, BoardController controller, PandemicModel model) {
        super(controller, model);
                
        this.container = container;
    }

    /**
     * Create the graphical board
     */
    @Override
    public void createBoard() {
        board = new JPanel();
        board.setPreferredSize(new Dimension(1000, 920));

        board.setLayout(null);
        board.setBackground(new Color(215, 201, 166));

        container.setViewportView(board);

        // We HAVE to put the higher components first, and the components behind others next.
        // So, the background image (ie the board) must be put last.
        createButtons();

        createGameComponents();

        createRoles();

        createBackgroundBoard();
    }

    /* ********************************************************* *
     * Events pushed by changes in the model *
     * *********************************************************
     */

    public void muteIconChanged() {
        // Get the new state
        ImageIcon newIcon = getController().isSoundOn() ? soundOnIcon : soundOffIcon;
        // Change the icon
        muteButton.setIcon(newIcon);
    }

    /**
     * Change the card shown at the top of the discard pile with the new card on
     * that pile.
     */
    @Override
    public void setTopDiscardCard() {
        List<Integer> discardDeck = getModel().getDiscardPile();

        int newCard;
        if (discardDeck.isEmpty()) {
            newCard = 300; // No discarded card !
            
            // If there are no Infection cards on the Discard Pile, disable the
            // Reshuffle and DiscardPile buttons
            reshuffleButton.setEnabled(false);
            discardButton.setEnabled(false);
        } else {
            newCard = discardDeck.get(discardDeck.size() - 1);

            // If there are Infection cards on the Discard Pile, enable the
            // Reshuffle and DiscardPile buttons
            reshuffleButton.setEnabled(true);
            discardButton.setEnabled(true);
        }

        ImageIcon infectionCard = getResourceProvider().getIcon(("cardinf" + newCard + ".jpg"));
        discardPile.setIcon(infectionCard);

        logger.debug("Set top infection card : {}", infectionCard.getDescription());
    }

    /* **** interface DecksObserver is implemented to perform the view's display reactions 
     *  to deck events fired by the model **** */

    @Override
    public void playerCardDrawn(Card newCard) {
        // bring the newly drawn card to the player card zone
        newCard.move(648, 418);
        // small hack to put each newly drawn card on TOP of former cards
        board.setComponentZOrder(newCard.getSwingComponent(), 0);
        // update the display of the number of remaining player cards
        remainingCards.setText(Integer.toString(getModel().getNbOfPlayerCards()));
    }

    @Override
    public void infectionCardDrawn(boolean isDrawnFromTop) {
        setTopDiscardCard();
    }

    @Override
    public void infectionDeckShuffled() {
        // The view doesn't have anything to display here        
    }

    @Override
    public void infectionDeckCleared() {
        setTopDiscardCard();        
    }

    @Override
    public void infectionDeckCardRemoved() {
        setTopDiscardCard();        
    }

    @Override
    public void infectionDeckRearranged() {
        setTopDiscardCard();        
    }
    
    /* **** from interface RolesObserver **** */

    @Override
    public void roleWillChange(int roleIndex) {
        // The role will change right after this method.
        // If the old role was the Troubleshooter, remove its special button.
        if (getModel().getAffectedRoles().get(roleIndex).isTroubleshooter()) {
            removeTroubleshooterButton();
        }
    }

    @Override
    public void roleHasChanged(int roleIndex) {
        // A role has changed. We must replace its rolebox.
        JLabel roleLabel = (JLabel) board.getComponentAt(10 + 157 * roleIndex, 408);
        roleLabel.setIcon(getResourceProvider().getIcon(buildRoleFilename(getModel().getAffectedRoles().get(roleIndex))));

        // If the new role is the Troubleshooter, place the corresponding button on its rolebox
        if (getModel().getAffectedRoles().get(roleIndex).isTroubleshooter()) {
            addTroubleshooterButton(roleIndex);
        }
    }

    /* ********************************************************* *
     * Graphical components creation *
     * *********************************************************
     */

    /**
     * Put the buttons on the board
     */
    private void createButtons() {
        // ImageIcon backInfectionCard =
        // getResourceProvider().getIcon("cardinf300.jpg");
        discardPile = new JLabel(/* backInfectionCard */);

        discardPile.setBounds(889, 421, 100, 71);
        board.add(discardPile);

        // ****** Mute button ********
        soundOnIcon = getResourceProvider().getIcon("soundon.jpg");
        soundOffIcon = getResourceProvider().getIcon("soundoff.jpg");

        muteButton = new JButton();
        muteButton.setBounds(162, 15, 35, 30);
        muteIconChanged(); // Display the initial state of the mute button

        muteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getController().switchMute();
                muteIconChanged();
            }
        });

        board.add(muteButton);

        // **** READ INSTRUCTIONS BUTTON ********************

        JButton instructionsButton = new JButton("Read Instructions!");
        instructionsButton.setBounds(15, 15, 140, 25);

        instructionsButton.addActionListener(new OverframeButtonListener("Quick Instructions", "instructions.jpg", getResourceProvider()));

        board.add(instructionsButton);

        // **** APPENDIX BUTTON *****************************

        JButton appendixButton = new JButton("Appendix");
        appendixButton.setBounds(15, 45, 140, 25);

        appendixButton.addActionListener(new OverframeButtonListener("Appendix", "appendix.jpg", getResourceProvider()));

        board.add(appendixButton);

        // **** NEW ASSIGNMENT BUTTON *************** (only if the new Special Event cards with "New Assignment" are used)
        if (getModel().getConfig().getEventCardsExpansions().contains(Expansion.ON_THE_BRINK)) {
            JButton newAssignmentButton = new JButton();
            ImageIcon newAssignmentImage = getResourceProvider().getIcon("newassignment_button.jpg");
            newAssignmentButton.setIcon(newAssignmentImage);
            newAssignmentButton.setBounds(937, 362, 47, 32);
            newAssignmentButton
                    .addActionListener(new NewAssignmentListener(getController(), getModel(), new ModalDialogsManager(board, getResourceProvider(), null)));
            board.add(newAssignmentButton);
        }

        // **** OTHERS BUTTONS **********************

        JButton drawButton = new JButton("Draw!");
        JButton infectionButton = new JButton("Infect!");
        JButton bottomInfectionButton = new JButton("Bottom...");
        reshuffleButton = new JButton("Reshuffle!");
        discardButton = new JButton("DiscardPile");
        JButton forecastButton = new JButton("Forecast");

        // Draw button
        drawButton.setBounds(783, 418, 75, 30);
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getController().drawPlayerCard();
            }
        });
        board.add(drawButton);

        // Infect button
        infectionButton.setBounds(890, 500, 100, 35);
        infectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getController().drawInfectionCard();
            }
        });
        board.add(infectionButton);

        // Bottom button
        bottomInfectionButton.setBounds(890, 550, 100, 25);
        bottomInfectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getController().drawBottomInfectionCard();
            }
        });
        board.add(bottomInfectionButton);

        // Reshuffle button
        reshuffleButton.setBounds(890, 580, 100, 25);
        reshuffleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getController().reshuffleInfectionCards();
            }
        });
        board.add(reshuffleButton);

        // DiscardPile button
        discardButton.setBounds(890, 610, 100, 25);
        discardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getController().showDiscardPile();
            }
        });
        board.add(discardButton);

        // Forecast button
        forecastButton.setBounds(890, 640, 100, 25);
        forecastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getController().showForecastCards();
            }
        });
        board.add(forecastButton);

        // Troubleshooter button (if there's a troubleshooter drawn)
        List<Role> roleList = getModel().getAffectedRoles();
        for (int roleIndex = 0; roleIndex < roleList.size(); roleIndex++) {
            if (roleList.get(roleIndex).getId() == TROUBLESHOOTER_ROLE_ID) {
                addTroubleshooterButton(roleIndex);
                break;
            }
        }

        // ** displaying remaining playingcards.. ***********************
        remainingCards = new JLabel(Integer.toString(getModel().getNbOfPlayerCards()));
        remainingCards.setForeground(new Color(16, 70, 80));
        remainingCards.setFont(new Font("Sanserif", Font.BOLD, 17));
        remainingCards.setBounds(622, 410, 25, 15);
        board.add(remainingCards);
    }

    /**
     * Add the troubleshooter button above the Troubleshooter rolebox, if the
     * Troubleshooter role is in use
     * 
     * @param roleIndex
     *            Index of the Troubleshooter role
     */
    private void addTroubleshooterButton(int roleIndex) {
        troubleshooterButton = new JButton();
        ImageIcon troubleshooterImage = getResourceProvider().getIcon("troubleshooter_button.jpg");
        troubleshooterButton.setIcon(troubleshooterImage);

        troubleshooterButton.setBounds(8 + roleIndex * 158, 424, 15, 20);
        troubleshooterButton.setEnabled(true);
        troubleshooterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getController().showTroubleshooterCards();
            }
        });
        board.add(troubleshooterButton, 0); // Above the rolebox
        troubleshooterButton.repaint();
    }

    /**
     * Delete the troubleshooter button from the board
     */
    private void removeTroubleshooterButton() {
        board.remove(troubleshooterButton);
        board.repaint();
        troubleshooterButton = null;
    }

    /**
     * Add the game components (counters and cards) to the board, and set the
     * corresponding PandemicObjects as their listeners for mouse drag & drop
     */
    private void createGameComponents() {
        // Place counters
        for (PandemicObject counter : getModel().getCountersLibrary()) {
            JLabel component = new JLabel(counter.getImage());
            component.setBounds(counter.getX(), counter.getY(), counter.getImage().getIconWidth(), counter.getImage().getIconHeight());
            component.addMouseListener(counter);
            component.addMouseMotionListener(counter);
            counter.setSwingComponent(component);
            board.add(component);
        }

        // Place cards
        for (Card card : getModel().getCardsLibrary()) {
            JLabel component = new JLabel(card.getImage());
            component.setBounds(card.getX(), card.getY(), card.getImage().getIconWidth(), card.getImage().getIconHeight());
            component.addMouseListener(card);
            component.addMouseMotionListener(card);
            card.setSwingComponent(component);
            board.add(component);
        }
    }

    /**
     * The role boxes to put on the board
     */
    private void createRoles() {
        List<Role> roleList = getModel().getAffectedRoles();

        int i = 0;
        for (Role pawn : roleList) {
            ImageIcon img_role = getResourceProvider().getIcon(buildRoleFilename(pawn));
            JLabel roleLabel = new JLabel(img_role);
            roleLabel.setBounds(10 + 157 * i, 408, ROLEBOX_WIDTH, ROLEBOX_HEIGHT);
            board.add(roleLabel);
            i++;
        }
    }

    private String buildRoleFilename(Role pawn) {
        StringBuilder sb = new StringBuilder("rolebox");
        sb.append(pawn.getId());
        sb.append(".jpg");
        return sb.toString();
    }

    /**
     * Add the board background image to the JPanel
     */
    private void createBackgroundBoard() {
        ImageIcon backgroundBoardImage = getResourceProvider().getIcon("board2.jpg");
        JLabel backgroundBoardLabel = new JLabel(backgroundBoardImage);

        backgroundBoardLabel.setBounds(0, 0, 1025, 920);

        board.add(backgroundBoardLabel);
    }

}
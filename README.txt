************************************************************************************************************************

                                PANDEMIC Solitaire version 2.8
                                
************************************************************************************************************************

1. Introduction
2. Requirements
3. Starting the application
4. Troubleshooting
5. Credits
6. Version history
7. License

1.Introduction    
*******************
  Hello ! You are about to start a Java-application called Pandemic Solitaire. With this little program,
 you can play Matt Leacock's brilliant co-operative boardgame "Pandemic" on your own, just like a solitaire game.
 A selection of optional elements and variants from the 3 expansions are also provided.

 The application assumes that you are already familiar with the rules of the boardgame. 

 Principles:
 - The game will randomly draw a starting situation for you, placing tokens and cards on the board as appropriate.
 - After that, you will have to do all the changes, dragging elements around the board as you see fit. The game will
   only manage the Player deck and the Infection deck for you.
 - No rules of play are enforced beyond the dispatch of game elements at the start of a play, similarly to other tools
   like VASSAL.
 - The application allows you to save a current game, and to load it in a future session where you'll find your tokens
   and cards at the exact same place, and the decks (including the Infection discard pile) in the same state.

2. Requirements
********************
 This application can run on most systems that support Java. That includes all forms of Windows, Mac OS X,
 all forms of Linuxes and Unixes, and many more.

 All you need is the Java Runtime Environment 6 (or higher) installed on your computer. 
 
 If you don't already have Java, you can get it here : <http://www.java.com/download/>.
 Alternatively, make a search for "Java Runtime Environment".
  
3. Starting the application
******************************** 
 1. To start the game, simply double-click the file PandemicSolo.jar !
 
 2. If it doesn't work, please check that the "java" executable is in your PATH environment variable.
    This page may help : <http://www.java.com/en/download/help/path.xml>.
    The string you want to find or to add to the PATH is the absolute path of the "java" or "java.exe"
    executable on your hard drive. Do a search for that name if you don't know where it is installed. 
    When the PATH is updated, try again step 1.
    
 3. If it doesn't work, I have included scripts that may help. Please try to launch the script relevant
    to your operating system : "PandemicSolo.bat" on Windows, "PandemicSolo.sh" on Mac, Linux and Unix.
    
 4. If that doesn't work either, please try to open a command line window ("cmd" on Windows, "Terminal" 
    on Mac, "bash" on Linux, etc.).
    Then set the current directory where you have unzipped the downloaded archive, including the file 
    PandemicSolo.jar. 
    Finally, on Windows type : "java.exe -jar PandemicSolo.jar", or on Mac or Linux type : 
    "java -jar PandemicSolo.jar &" (without the quotes).
    Please note that the case is important !
 
 For further instructions on the gaming interface itself, you can find a "Read Instructions!" button
 once you have started the program.

 Good Luck!
 
4. Troubleshooting
********************

If one or more of the following behaviors are reported :
 - No image showing in the software
 - Error message notifying the jar file "can't be launched" or something similar
 - Error message "java.lang.NoClassDefFoundError: org/slf4j/LoggerFactory" or 
    "java.lang.ClassNotFoundException: org.slf4j.LoggerFactory"
 THEN : please check that the PandemicSolo.jar is accompanied by a "lib" directory,
   at the same place, containing at least the following files : slf4j-api-1.6.1.jar, 
   AppleJavaExtensions.jar, logback-core-0.9.28.jar, logback-classic-0.9.28.jar.
 If not, please download again the zip file (which contains this directory).

5. Credits
***************

 For the boardgame :

    Pandemic by Matt Leacock
    On the Brink, In The Lab and State of Emergency by Matt Leacock and Thomas Lehmann
    Art by Josh Cappel, Régis Moulun, Christian Hanisch, Chris Quilliams, Tom Thiel, Hans-Georg Schneider
    Published by Z-Man Games

 For this application :

    From v2.6, all developments by : manur (Emmanuel Bizieau) <manur@manur.org>

    Initial development by jancsoo (Andras Domian) <http://boardgamegeek.com/user/jancsoo>

6. Version history
***********************

v2.8   - 02/13/2020 - Added the event cards from In The Lab (3 events) and State of Emergency (6 events)
                    - Added the roles from In The Lab (4 roles) and State of Emergency (3 roles)
                    - Introduced a Survival mode that removes some event cards from the pool before their random draw
                      (New Assignment, One Quiet Night, Resilient Population, Commercial Travel Ban, Forecast,
                       Resource Planning, Infection Rumor are banned)
                    - Copies of the same saved game file will always produce the same reshuffle of the infection deck
                      (as long as they happen for the same number of infection cards to be shuffled)
                    - Removed the notion of classic or advanced Operations Expert
                    - Added the roles of Quarantine Specialist and Contingency Planner to the core set
                      (they didn't exist at the time of the last release)
                    - Added the explicit choice of including either 5 event cards or 2*NbOfPlayers event cards,
                       whatever expansion sets are used
                    - Added 2 new Virulent Strain cards from In The Lab
                    - Added Emergency Event challenge from State of Emergency
                    - Added Worldwide Panic challenge from In The Lab, as an alternative to the Mutation challenge
                    - Added Quarantine mode with its markers, along with the Colonel role and the Local Initiative
                       special event, that will both be added to the pool of available elements before draw
                    - Redesign of the Configuration window. One can now choose by expansion which set of roles
                       and special events are included.

v2.7.1 - 12/10/2011 - Some internal refactoring - More log messages - Bugfix release :
                       - the Containment Specialist's pawn is not beige (not grey like the Generalist)
                       - rigorous distribution of epidemics cards at setup, following more closely 
                         the rulebook
                       - subfolders of the current folder now shown in the Load/Save selection window

v2.7   - 08/19/2011 - New features :
                       - single dialog box for game configuration
                       - saving and loading 
                       - the "classic" Operations Expert card
                       - a menu bar
                       - an About box, featuring the details of the current game configuration
                       - start of a new game without closing and re-starting 
                       - the counters not used for the current game configuration are not shown
                      Bug fixes :
                       - Clicking Cancel during game configuration doesn't throw a 
                         NullPointerException anymore
                       - Player cards distribution is now correct (2 players : 4 cards each,
                         3p : 3 cards, 4p : 2 cards)

v2.6.1 - 03/12/2011 - Bugfix release - The JAR packaging wasn't working.

v2.6   - 02/17/2011 - Internal refactoring, with no new functionalities. 
		  			  (^ First version by Emmanuel "manur" Bizieau ^ )

v2.5 - 06/23/2010 - Sound effects added ; Initial playing cards and infections distributed automatically ;
					Pawns placed at Atlanta ; Cards can be dragged from clicking on whatever pixel ;
					Infection cards can be discarded (Resilient Population) ; Program packaged as a JAR.
                    (Last version by Andras "jancsoo" Damian)

v2.4 - ??/??/2010 - Added "On the Brink" : new roles, Virulent Strain and Mutation variants, purple cubes,
                    new Special Events and Legendary difficulty level (7 epidemics).
                    
v2.3 - 03/11/2010 - Added variable difficulty (Introductory and Heroic) and an instructions panel. 

v2.2 - 02/26/2010 - Working version with only base game and Normal difficulty (5 epidemics).                   

7. License
***************

 This application is released under the GPL v3 license.
 It means you can run it whenever you want and on any system.
 It also means that the source code is included (inside the src/ directory) and that you can look at it, 
 modify it, compile it, and redistribute the whole application at will.
 The most important constraint is that you CANNOT redistribute any part of the application with a different 
 license (for example, it is forbidden to release a derivative work without the full source code or the 
 right to redistribute it freely).

 The legalese is in the LICENSE.txt accompanying file.
                             
 Now, the rigorous way to put it is : 

   PandemicSolo is free software: you can redistribute it and/or modify it under the terms of the 
   GNU General Public License as published by the Free Software Foundation, either version 3 of the 
   License, or (at your option) any later version.
 
   PandemicSolo is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
   without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
   See the GNU General Public License for more details.
 
   You should have received a copy of the GNU General Public License along with PandemicSolo.
   If not, see <http://www.gnu.org/licenses/>.

=======================================================================================================================

 This file was last modified on 13/02/2020.
 Copyright (C) 2020 Emmanuel Bizieau <manur@manur.org>, (C) 2010 Andras Damian <http://boardgamegeek.com/user/jancsoo>

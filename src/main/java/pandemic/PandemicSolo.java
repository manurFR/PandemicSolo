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
package pandemic;

import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

import pandemic.configuration.ConfigBuilder;
import pandemic.configuration.ConfigDialogBuilder;
import pandemic.configuration.DefaultConfigBuilder;
import pandemic.dialog.ModalDialogsManager;
import pandemic.util.GenericResourceProvider;
import pandemic.util.ResourceProvider;
import pandemic.util.sounds.SoundsManager;
import pandemic.view.ViewFactory;
import pandemic.view.menubar.MacOsPandemicMenuBar;
import pandemic.view.menubar.PandemicMenuBar;
import pandemic.view.menubar.StandardPandemicMenuBar;
import pandemic.view.swing.SwingViewFactory;

/**
 * Pandemic Solitaire
 * Main method
 * 
 * @author jancsoo
 * @author manur
 */
public class PandemicSolo {

	public static final String VERSION = "2.7.1";
	
	public static final boolean IS_MACOS = System.getProperty("os.name").startsWith("Mac OS X");
	public static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");
	
	private static final Logger logger = LoggerFactory.getLogger(PandemicSolo.class);
	private static final String LOGBACK_FILE_APPENDER = "logFile";
	private static final String LOGBACK_CONSOLE_APPENDER = "systemOut";
	
	private static final String[] disclaimer = {
		"===============================================================",
		"          Pandemic Solitaire - version " + VERSION,
		"===============================================================",
		"Copyright (C) 2011 Emmanuel Bizieau, (C) 2010 Andras Domian",
		"This program comes with ABSOLUTELY NO WARRANTY.",
		"This is free software, and you are welcome to redistribute it",
		"under certain conditions.",
		"Please read the LICENSE.txt file for all details.",
		"==============================================================="
	};

    /**
     * Main method. Everything starts here. It creates the graphical windows and
     * MVC objects
     */
    public static void main(String[] args) {
        // Beautify for Mac OS X
        if (IS_MACOS) {
            setUpMacSystemProperties();
        }

        // Print the disclaimer both on the log and on System.out
        for (String line : disclaimer) {
            logger.trace(line);
            System.out.println(line);
        }

        // ** Prepare the main window

        JFrame window = new JFrame();
        window.setTitle("Pandemic Solitaire  version " + VERSION);
        window.setSize(1027, 728);
        
        // Set up the scroll pane for the game to take place
        JScrollPane gameContainer = new JScrollPane();
        window.setContentPane(gameContainer);

        // ** Prepare the resource provider

        ResourceProvider resourceProvider = new GenericResourceProvider();

        // ** Prepare the file chooser dialog

        JFileChooser fileChooser = new JFileChooser();

        // ** Prepare the game configuration builder

        ConfigBuilder configBuilder;

        // ** Parse the command line arguments

        List<String> argsList = Arrays.asList(args);

        // A "--noconfig" command line argument allows to skip the dialog
        // configuration
        if (argsList.contains("--noconfig")) {
            configBuilder = new DefaultConfigBuilder();
        } else {
            configBuilder = new ConfigDialogBuilder(window);
        }

        // Determine the output of logging messages
        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        
        /* Note : I wasn't able to find how to add to the rootLogger an appender defined in the logback.xml configuration file
         *  (rootLogger.getAppender(...) returns null if the appender is not already added to the logger in the config file).
         *  So the appenders are pre-associated to the root logger in the configuration file, and the root logger level is set 
         *  to OFF by default. Here, we change the level if a log output is needed, while we detach each appender when the 
         *  corresponding output is NOT needed. 
         */
        
        boolean logFile = argsList.contains("--log");
        boolean systemOut = argsList.contains("--console");
        
        if (logFile || systemOut) {
        	rootLogger.setLevel(Level.DEBUG);
        }
        
        // If there's no "--log" command line argument, do not output to file
        if (!logFile) {
            rootLogger.detachAppender(LOGBACK_FILE_APPENDER);
        }        

        // If there's no "--console" command line argument, do not output to console (System.out)
        if (!systemOut) {
        	rootLogger.detachAppender(LOGBACK_CONSOLE_APPENDER);
        }        

        // ** Prepare the ViewFactory (Swing for this app)
        
        ViewFactory viewFactory = new SwingViewFactory(gameContainer);
        viewFactory.setResourceProvider(resourceProvider);
        
        // ** Prepare the GameManager that will be in charge of setting up main operations

        GameManager gameManager = new GameManager();
        gameManager.setViewFactory(viewFactory);
        gameManager.setConfigBuilder(configBuilder);
        gameManager.setDialogsManager(new ModalDialogsManager(window, resourceProvider, fileChooser));
        gameManager.setMainWindow(window);
        gameManager.setSoundsManager(new SoundsManager());
        
        // TODO see if one could not pass the ResourceProvider to GameManager and BoardController, 
        //  since it's injected in the ViewFactory that those beans will know
        gameManager.setResourceProvider(resourceProvider);

        // ** Prepare the menubar, depending on the current OS, feeding it the
        // GameManager that it will call

        PandemicMenuBar menuBuilder;
        if (IS_MACOS) {
            menuBuilder = new MacOsPandemicMenuBar();
        } else {
            menuBuilder = new StandardPandemicMenuBar();
        }

        window.setJMenuBar(menuBuilder.constructMenuBar(window, gameManager, resourceProvider));

        // *** display starting screen
        // *******************************************************

        JPanel startingPanel = new JPanel();
        JLabel startingLabel = new JLabel(resourceProvider.getIcon("startingboard.jpg"));
        startingPanel.add(startingLabel);
        window.add(startingPanel);

        // *********************************************************************

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        // ****************************************************************************

        gameManager.launchNewGame();
    }

    /**
     * These properties integrate this application into the Mac OS GUI system.
     * Sadly, it doesn't work if these properties are not set as the first
     * command in the program or in another class... Strange. I don't like it,
     * but I have to put these commands here, in a static method.
     */
    public static void setUpMacSystemProperties() {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "PandemicSolo");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Do nothing special here
        }
    }
	
}
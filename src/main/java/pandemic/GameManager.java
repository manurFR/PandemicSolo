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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pandemic.configuration.ConfigBuilder;
import pandemic.dialog.DialogsManager;
import pandemic.model.PandemicModel;
import pandemic.model.Variant;
import pandemic.util.GameConfig;
import pandemic.util.ResourceProvider;
import pandemic.util.sounds.SoundsManager;
import pandemic.view.ViewFactory;

import static pandemic.PandemicSolo.MAINWINDOW_HEIGHT;
import static pandemic.PandemicSolo.MAINWINDOW_WIDTH;

/**
 * Main operations to manage a game : create a new game, saving, restoring, etc.
 * 
 * @author manur
 * @since v2.7
 */
public class GameManager {

	public static final String SAVEFILE_EXTENSION = "sav";

	private static final Logger logger = LoggerFactory.getLogger(PandemicSolo.class);
	
	private ConfigBuilder configBuilder;
	private JFrame mainWindow;

	private ViewFactory viewFactory;
	private DialogsManager dialogsManager;
	private ResourceProvider resourceProvider;
	private SoundsManager soundsManager;
	
    private PandemicModel currentModel = null;
		
	/**
	 * Launch a new game by querying the user for the configuration 
	 * and, provided she replied, display it in the main window.
	 */
	public void launchNewGame() {
        // If a game is in progress, check for user confirmation
        if (currentModel != null) {
            if (!dialogsManager.confirm("Abort current game ?", "Are you sure you want to abort the current game and create a new one ?")) {
                return;
            }
        }
	    
	    // Query configuration
		GameConfig config = configBuilder.queryGameConfig(resourceProvider);
		
		// If the user pressed 'Cancel', do nothing
		if (config == null) {
			return;
		}
		
		currentModel = new PandemicModel(config);
		startGame();
	}
	
	/**
	 * Start the game, requires a valid model.
	 */
	private void startGame() {
        logger.debug("");
        logger.debug("*#*#*#*#*#*#*#*#*#*#  Starting new game *#*#*#*#*#*#*#*#*#*#");
        logger.debug("");

        currentModel.getConfig().log();

        // Set window title
        mainWindow.setTitle(constructMainTitle(currentModel.getConfig()));
        
        // Set up the controller and its model
        BoardController controller = new DefaultBoardController(currentModel);
        controller.setViewFactory(viewFactory);
        controller.setSoundsManager(soundsManager);
        currentModel.registerDecksObserver(soundsManager);
        currentModel.registerRolesObserver(soundsManager);
        controller.setResourceProvider(resourceProvider);
                
        if (currentModel.getCityList().isEmpty()) {
            // Model not initialized yet (ie it's a new game, not a loading)
            controller.setUpModel();
        }
                
        // Create the view and place all the components
        controller.setUpView();
        
        mainWindow.pack();
        mainWindow.setSize(MAINWINDOW_WIDTH, MAINWINDOW_HEIGHT + ((PandemicSolo.IS_MACOS) ? 0 : 8));
        
        logger.debug("----- Game ready to play -----");
	}
	
	/**
	 * Build the String to use as a title to the game window
	 * @param config Class owning the set up details
	 * @return Title built 
	 */
	public String constructMainTitle(GameConfig config) {
		StringBuilder sb = new StringBuilder();
		sb.append("Pandemic Solitaire  version ");
		sb.append(PandemicSolo.VERSION);
		sb.append("  -  Difficulty level:  ");
		sb.append(config.getDifficultyLevel());
		for (Variant variant: config.getVariants()) {
			sb.append(" + ");
			sb.append(variant.getLabel());
		}
		return sb.toString();
	}
	
	/**
	 * Save the current game to a file.
	 */
	public void saveGame() {
		if (currentModel == null) {
			return;
		}
		
		// Select the file
		File fileToSave = dialogsManager.selectFile("Save game...", "Save");
		
		if (fileToSave == null) {
			return;
		}
		
		if (fileToSave.exists()) {
			if (!dialogsManager.confirm("Replace existing save file ?", "File \"" + fileToSave.getName() + "\" already exists.\nAre you sure you want to replace the current file ?")) {
				return;
			}
		}
		
		// If there's no extension, we add the default one
		if (fileToSave.getName().indexOf('.') < 0) {
			fileToSave = new File(fileToSave.getAbsolutePath() + "." + SAVEFILE_EXTENSION);
		}
		
		// Open a stream
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(fileToSave);
		} catch (FileNotFoundException e) {
		    logger.error("Failed to open output stream to save {}", fileToSave, e);
		    return;
		}

		// Serialize and save
		try {
			ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
			
			objectStream.writeObject(currentModel);
			objectStream.flush();
			
			logger.info("File saved - size: {} Ko", outputStream.getChannel().size()/1000);
			
			objectStream.close();
		} catch (IOException e) {
			dialogsManager.showAlert("Error. Saving file \"" + fileToSave.getName() + "\" failed.");
			logger.error("Failed to write save file {}", fileToSave, e);
			return;
		}
	}
	
	public void loadGame() {
		// If a game is in progress, check for user confirmation
		if (currentModel != null) {
			if (!dialogsManager.confirm("Abort current game ?", "Are you sure you want to abort the current game and create a new one ?")) {
				return;
			}
		}
		
		// Select the file
		File fileToLoad = dialogsManager.selectFile("Load game...", "Load");
		
		if (fileToLoad == null) {
			return;
		}
		
		if (!fileToLoad.exists() || !fileToLoad.isFile() || !fileToLoad.canRead()) {
			dialogsManager.showAlert("File \"" + fileToLoad.getName() + "\" not found or not accessible.");
			return;
		}
		
		// Open a stream
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(fileToLoad);
		} catch (FileNotFoundException e) {
		    logger.error("Failed to open input stream to read {}", fileToLoad, e);
            return;
		}
		
		// Unzerialize and load
		try {
			ObjectInputStream objectStream = new ObjectInputStream(inputStream);
			
			currentModel = (PandemicModel) objectStream.readObject();
			logger.info("File loaded - size: {} Ko", inputStream.getChannel().size()/1000);
	        
			inputStream.close();
		} catch (IOException e) {
			dialogsManager.showAlert("File \"" + fileToLoad.getName() + "\" is not an acceptable PandemicSolo savegame file.\n(It may have been saved with an older version of this program.)");
			logger.error("Failed to read save file {}", fileToLoad, e);
			return;
		} catch (ClassNotFoundException e) {
			dialogsManager.showAlert("File \"" + fileToLoad.getName() + "\" is not an acceptable PandemicSolo savegame file.\n(It may have been saved with an older version of this program.)");
			logger.error("Failed to read save file {}", fileToLoad, e);
			return;
		}
		
		startGame();
	}

	// ******************************************************
	
	public void setConfigBuilder(ConfigBuilder configBuilder) {
		this.configBuilder = configBuilder;
	}

	public void setMainWindow(JFrame mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void setDialogsManager(DialogsManager dialogsManager) {
		this.dialogsManager = dialogsManager;
	}

	public void setResourceProvider(ResourceProvider resourceProvider) {
		this.resourceProvider = resourceProvider;
	}

	public PandemicModel getCurrentModel() {
		return currentModel;
	}
	
    public void setSoundsManager(SoundsManager soundsManager) {
        this.soundsManager = soundsManager;
    }

    public void setViewFactory(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
    }
	
}

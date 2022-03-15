package application.view;

import java.util.prefs.Preferences;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class RootController {
	
    // Reference to the main application
    private Main mainApp;
    
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
    
    
    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
    	mainApp.showAboutDialog();
    }

    /**
     * Opens the preferences dialog.
     */
    @FXML
    private void handlePreferences() {

    	
    	mainApp.preferences();
    }


    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
    	mainApp.closeDown();
        System.exit(0);
    }
    
    
    
    /**
     * Saves the experiment file.
     */
    @FXML
    private void handleResults() {
    	mainApp.saveResults();
    }


}

package application.view;

import application.model.Settings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PrefController {
    private Stage dialogStage;
    private boolean okClicked = false;

    
    
	@FXML
	private Button btnOK;

	@FXML
	private Button btnCancel;
    
	@FXML
	private TextField txtPRISMLocation;

	
	public Settings settings;


    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	loadSettings();
    	
    }
    
    
    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
            okClicked = true;
        	String s= txtPRISMLocation.getText();
        	settings.add("PRISMLocation", s);
        	settings.write();
            dialogStage.close();
    }
    
    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
            okClicked = false;
            dialogStage.close();
    }
    
    
    
    public void loadSettings(){
    	System.out.println("initialising dialog");
    	if (settings == null) settings = new Settings();
    	settings.read();
    	
    	if (settings.get("PRISMLocation") != null){
    		txtPRISMLocation.setText(settings.get("PRISMLocation"));
    	}
    }

}

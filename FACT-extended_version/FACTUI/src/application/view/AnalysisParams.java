package application.view;

import java.io.File;

import application.model.Settings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AnalysisParams {

    private Stage dialogStage;
    private boolean okClicked = false;

	@FXML
	private Button btnOK;

	@FXML
	private Button btnCancel;

	@FXML
	private Button btnFile;

	@FXML
	private TextField txtCIUpper;
	@FXML
	private TextField txtCILower;
	@FXML
	private TextField txtCIStep;
	@FXML
	private TextField txtHillClimbing;


	@FXML
	private Label lblStatus;
	
	private Float alphaL;
	private Float alphaU;
	private Float alphaS;
	
	private Integer HillClimbingMax;
	
	public Settings settings;
	
	public Float getAlphaL() {
		return alphaL;
	}
	public Float getAlphaU() {
		return alphaU;
	}
	public Float getAlphaS() {
		return alphaS;
	}
	public Integer getHillClimbingMax(){
		return HillClimbingMax;
	}
	

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
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
            if (txtCIUpper.getText().equals("")){
            	lblStatus.setText("Invalid Interval");
            	return;
            }
            if (txtCILower.getText().equals("")){
            	lblStatus.setText("Invalid Interval");
            	return;
            }
        	alphaU = Float.parseFloat(txtCIUpper.getText());
        	alphaL = Float.parseFloat(txtCILower.getText());
        	alphaS = Float.parseFloat(txtCIStep.getText());
        	
        	if (alphaU <= alphaL) {
        		lblStatus.setText("Lower bound must be less than upper bound");
        		return;
        	}
        	
        	if ((alphaU <85) || (alphaL < 85)){
        		lblStatus.setText("Minimum confidence = 85%");
        		return;
        	}
        		
        	HillClimbingMax = Integer.parseInt(txtHillClimbing.getText());
        	
        	
        	dialogStage.close();
    }
    
    
    @FXML
    private void handleNumberL(KeyEvent evt){
    	String character = evt.getCharacter();
    	String valid="0123456789";
		lblStatus.setText("");

		if (!valid.contains(character)){
    		evt.consume();
    		lblStatus.setText("Numbers only");
    		return;
    	}
    	
    	String s = txtCILower.getText() + character;
    	if (!validateCI(s)){
    		evt.consume();
    		lblStatus.setText("Invalid Interval");
    	}
    }

    
    @FXML
    private void handleNumberU(KeyEvent evt){
    	String character = evt.getCharacter();
    	String valid="0123456789";
		lblStatus.setText("");

		if (!valid.contains(character)){
    		evt.consume();
    		lblStatus.setText("Numbers only");
    		return;
    	}
    	
    	String s = txtCIUpper.getText() + character;
    	if (!validateCI(s)){
    		evt.consume();
    		lblStatus.setText("Invalid Interval");
    	}
    }
    
    @FXML
    private void handleNumberS(KeyEvent evt){
    	String character = evt.getCharacter();
    	String valid="0123456789.";
		lblStatus.setText("");

		if (!valid.contains(character)){
    		evt.consume();
    		lblStatus.setText("Numbers only");
    		return;
    	}
    	
    	String s = txtCIStep.getText() + character;
    	if (!validateCI(s)){
    		evt.consume();
    		lblStatus.setText("Invalid Interval");
    	}
    }
    
    @FXML
    private void handleNumberHC(KeyEvent evt){
    	String character = evt.getCharacter();
    	String valid="0123456789";
		lblStatus.setText("");

		if (!valid.contains(character)){
    		evt.consume();
    		lblStatus.setText("Numbers only");
    		return;
    	}
    	
    	String s = txtCIStep.getText() + character;
    	if (!validateHC(s)){
    		evt.consume();
    		lblStatus.setText("Invalid Limit");
    	}
    }

    private Boolean validateHC(String s){
    	
    	try{
    		Float v = Float.parseFloat(s);
    		
    		if (v>99) return false;
    		
    		return true;
    		
    	} catch(Exception e){
    		return false;
    	}    	
    }
    
    
    private Boolean validateCI(String s){
    	
    	try{
    		Float v = Float.parseFloat(s);
    		
    		if (v>99) return false;
    		
    		return true;
    		
    	} catch(Exception e){
    		return false;
    	}    	
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
    	settings.read();
    	
    	if (settings.get("lowerCI") != null){
    		txtCILower.setText(settings.get("lowerCI"));
    	}
    	if (settings.get("upperCI") != null){
    		txtCIUpper.setText(settings.get("upperCI"));
    	}
    	if (settings.get("stepCI") != null){
    		txtCIStep.setText(settings.get("stepCI"));
    	}
    	if (settings.get("hillClimbingMax") != null){
    		txtHillClimbing.setText(settings.get("hillClimbingMax"));
    	}

    }
 
}

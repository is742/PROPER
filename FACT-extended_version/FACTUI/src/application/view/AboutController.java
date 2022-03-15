/*
 * FACT: A Probabilistic Model Checker for Quantitative Verification with Confidence Intervals
 * Copyright (C) 2015 
 * Radu Calinescu  : Department of Computer Science, University of York, United Kingdom
 * Kenneth Johnson : School of Computer and Mathematical Sciences, Auckland University of Technology, New Zealand
 * Colin Paterson  : Department of Computer Science, University of York, United Kingdom
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of  MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * A copy of the GNU General Public License is available from  <http://www.gnu.org/licenses/>.
 */

package application.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Controller class for the about dialog.
 * <p>
 * This simply displays the JavaFX panel with information.
 * A single OK button is handled to dismiss the dialog.
 * @author ColinPaterson
 *
 */
public class AboutController {
    private Stage dialogStage;
    private boolean okClicked = false;
    
	@FXML
    private TextArea txtAbout;


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	
    	txtAbout.setText("FACT provides a framework for the formal verification of quality of "
    			+ "service properties with confidence intervals.\n\n" 
    			+ "For more information, updates and supporting materials visit our "
    			+ "website at:\n"
    			+ "http://www-users.cs.york.ac.uk/cap508/FACT/\n\n"
    			+ "You may contact the authors at:\n"
    			+ "Radu Calinescu: \tradu.calinescu@york.ac.uk\n"
    			+ "Kenneth Johnson: \tkenneth.johnson@aut.ac.nz\n"
    			+ "Colin Paterson: \tcap508@york.ac.uk");
    }

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage The stage with which the about controller is associated.
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return returns true is OK was clicked.
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
            dialogStage.close();
    }

}
package application.view;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import application.Main;
import application.model.ExperimentData;
import application.model.FactExecution;
import application.model.ModelProperty;
import application.model.Settings;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import logging.FACTLogger;
import mathEngine.MatLabEngine;

public class MainViewController {

	@FXML
    private Label lbStatus;

	@FXML
    private TextArea txtModel;

	@FXML
    private TableView<ModelProperty> tblProperties;
	
	@FXML
	private TableColumn<ModelProperty, String> propertyDetailColumn;
	@FXML
	private TableColumn<ModelProperty, String> propertyActionColumn;


	@FXML
    public LineChart<Number, Number> chtOutput;
		
	@FXML
	private Button btnLoadModel;

	@FXML
	private Button btnSaveModel;

	@FXML
	private Button btnLoadProperties;

	@FXML
	private Button btnAddProperty;

	@FXML
	private Button btnSaveProps;


	  // Reference to the main application.
    public Main mainApp;

    // This is the data object that we are displaying in this view
    public ExperimentData data = new ExperimentData();
    
    public static Settings settings = new Settings();

    
	public MainViewController(){
		data.output.setValue("");
	    data.mVC = this;
	}
	
	

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

    	// This should be getting data from the model layer
    	lbStatus.setText("Status: Initializing");
    	txtModel.setText(data.getModelCode());
    	
    	propertyDetailColumn.setCellValueFactory(cellData -> cellData.getValue().detailProperty());
    	propertyDetailColumn.setCellFactory(TextFieldTableCell.forTableColumn());

    	chtOutput.getData().add(data.chartSeries0);
        chtOutput.getData().add(data.chartSeries1);

        setContextMenu();        

    }
	    
        
    @FXML
    private void handleLoadModelButtonAction(ActionEvent event) {
        lbStatus.setText("Loading Model ...");
        
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open Model File");
        fileChooser.getExtensionFilters().addAll(
        	    new FileChooser.ExtensionFilter("Model", "*.pm")
        	);
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (file != null) {   
        	if (file.length() <  5120){
	        	data.loadModel(file);
	        	txtModel.setText(data.getModelCode());
	        	
		        btnSaveModel.setDisable(false);
	        	lbStatus.setText(data.getStatus());
        	}
        	else lbStatus.setText("Model file exceeds 5K");
	     }
        else 
        	lbStatus.setText("Cancelled");
    }
    
    @FXML
    private void handleSaveModelButtonAction(ActionEvent event) {
    	 lbStatus.setText("Saving Model ...");
         
         FileChooser fileChooser = new FileChooser();

         fileChooser.setTitle("Save Model File");
         fileChooser.getExtensionFilters().addAll(
         	    new FileChooser.ExtensionFilter("Model", "*.pm")
         	);
         File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
         if (file != null) {   
            data.setModel(txtModel.getText());
         	data.saveModel(file);
         	
 	        btnSaveModel.setDisable(true);
         	lbStatus.setText(data.getStatus());
 	     }
         else 
         	lbStatus.setText("Cancelled");
    }
    
    @FXML
    private void handleSavePropertyButtonAction(ActionEvent event) {
    	 lbStatus.setText("Saving Properties ...");
         
         FileChooser fileChooser = new FileChooser();

         fileChooser.setTitle("Save Property File");
         fileChooser.getExtensionFilters().addAll(
         	    new FileChooser.ExtensionFilter("Property","*.pctl")
         	);
         File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
         if (file != null) {   
         	data.saveProps(file);
         	
 	        btnSaveProps.setDisable(true);
         	lbStatus.setText(data.getStatus());
 	     }
         else 
         	lbStatus.setText("Cancelled");
    }
    

    
    @FXML
    private void handleLoadPropertiesButtonAction(ActionEvent event) {
       lbStatus.setText("Loading Properties ...");
        
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open Properties File");
        fileChooser.getExtensionFilters().addAll(
        	    new FileChooser.ExtensionFilter("Property", "*.pctl")
        	);
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (file != null) {   
        	if (file.length() < 1024){
	        	data.loadProps(file);
	        	
		        btnSaveProps.setDisable(false);
	        	lbStatus.setText(data.getStatus());
        	} else lbStatus.setText("Property file exceeds 1K");
	     }
        else 
        	lbStatus.setText("Cancelled");

    }


    
    @FXML
    private void handleModelChanged(){
		btnSaveModel.setDisable(false);
    }
    
    
	public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        tblProperties.setItems(data.getPropertyData());
        
        
     }
	
	
	public void saveResults(){
		lbStatus.setText("Saving Results ...");
		
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Save results");
        fileChooser.getExtensionFilters().addAll(
        	    new FileChooser.ExtensionFilter("Results", "*.csv"),
        	    new FileChooser.ExtensionFilter("All Files", "*.*")
        	);
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        if (file != null) {   
        	data.exportResults(file);
        	
        	lbStatus.setText(data.getStatus());
        	data.resultsUnsaved = false;
	     }
        else 
        	lbStatus.setText("Cancelled");

	}
	
	
	public void showPreferencesDialog(){
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main.class.getResource("view/Preferences.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Preferences");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(mainApp.getPrimaryStage());
	        dialogStage.setResizable(false);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        
	        // Set the person into the controller.
	        PrefController controller = loader.getController();
	        controller.settings = settings;
	        controller.setDialogStage(dialogStage);

	        // Show the dialog and wait until the user closes it
	        dialogStage.showAndWait();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	/*
	 * This function is called when the value in the TableView
	 * containing the properties is edited.
	 */
    public void handlePropertyEdit(CellEditEvent<ModelProperty, String> t) {
        ((ModelProperty) t.getTableView().getItems().get(
            t.getTablePosition().getRow())
            ).setDetail(t.getNewValue());
        
    	btnSaveProps.setDisable(false);
    	}
    
    @FXML
    private void handleProperyAddButtonAction(ActionEvent event) {
    	data.getPropertyData().add(new ModelProperty("<new property>"));
    	int listSize = data.getPropertyData().size() - 1;
    	tblProperties.requestFocus();
    	tblProperties.getSelectionModel().select(listSize);
    	tblProperties.getFocusModel().focus(listSize);
    }
 

    
    private void setContextMenu(){
    	tblProperties.setRowFactory(
    		    new Callback<TableView<ModelProperty>, TableRow<ModelProperty>>() {
    		  @Override
    		  public TableRow<ModelProperty> call(TableView<ModelProperty> tableView) {
    		    final TableRow<ModelProperty> row = new TableRow<>();
    		    final ContextMenu rowMenu = new ContextMenu();
    		    MenuItem evalItem = new MenuItem("Evaluate");
    		    
    		    evalItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						
						if (data.resultsUnsaved){
							Alert alert = new Alert(AlertType.CONFIRMATION);
							alert.setTitle("Confirmation Dialog");
							alert.setHeaderText("Unsaved results.");
							alert.setContentText("You have unsaved results from the previous experiment\n click OK to continue.");
	
							Optional<ButtonType> result = alert.showAndWait();
							if (result.get() != ButtonType.OK){
								return;
							}			
						}
						
						Boolean retVal = showAnalysisDialog();
						
						if (retVal){
							// get the property to evaluate from the table.
							data.setEvalProp(row.getItem().detailProperty().getValue());

							data.clearChartData();
							chtOutput.setAnimated(false);
							
							data.setModel(txtModel.getText());
							
							lbStatus.setText("Evaluating: " + row.getItem().detailProperty().getValue());
			 	      	    FactExecution fe = new FactExecution(data);
			 	      	    
			 	      	    //Now I have the CI I can set the graph X range;
			 	      	    data.graphXMin = (float) ((data.cnfLower-1)/100.0d); 
			 	      	    data.graphXMax = (float) ((data.cnfUpper+1)/100.0d);
			 	      	    
			 	      	    NumberAxis nx = (NumberAxis) chtOutput.getXAxis();
			 	      	    nx.setLowerBound(data.graphXMin);
			 	      	    nx.setUpperBound(data.graphXMax);
			 	      	    
			 	      	    nx.setTickUnit(0.005);
			 	      	    nx.setMinorTickCount(2);

			 	      	    NumberAxis ny = (NumberAxis) chtOutput.getYAxis();
			 	      	    ny.setLowerBound(0.0);
			 	      	    ny.setUpperBound(1.0);
			 	      	    ny.setTickUnit(0.25);
			 	      	    ny.setMinorTickCount(5);
						    fe.lbStatus = lbStatus;
						    String s = settings.get("hillClimbingMax");
						    data.fInput.config.MAX_ITERATIONS  = Integer.parseInt(s);
						    fe.run();   
				      	    lbStatus.setText(data.output.get());
						}
						
					}
				});
    		    
    		    MenuItem removeItem = new MenuItem("Delete");
    		    
    		    removeItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						lbStatus.setText("Deleting");
						tblProperties.getItems().remove(row.getItem());
						lbStatus.setText("Done");
					}
				});
    		    
    		    rowMenu.getItems().addAll(evalItem, removeItem);

    		    // only display context menu for non-null items:
    		    row.contextMenuProperty().bind(
    		      Bindings.when(Bindings.isNotNull(row.itemProperty()))
    		      .then(rowMenu)
    		      .otherwise((ContextMenu)null));
    		    return row;
    		  }
    		});
    }
  
    
	public Boolean showAnalysisDialog() {
		Boolean retVal = false;
		
		// You can't start a new run if there is one running.
		if (data.running)
			return false;
		
		try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main.class.getResource("view/AnalysisParams.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Analysis Parameters");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(mainApp.getPrimaryStage());
	        dialogStage.setResizable(false);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the person into the controller.
	        AnalysisParams controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.settings = settings; // pass the settings through to the dialog
	        controller.loadSettings();
	        
	        // Show the dialog and wait until the user closes it
	        dialogStage.showAndWait();

	        if (controller.isOkClicked()){
	        	data.setconfidenceValue(controller.getAlphaL(), controller.getAlphaU(), controller.getAlphaS());
	        	
	        	Integer iLCI = Math.round(controller.getAlphaL());
	        	Integer iUCI = Math.round(controller.getAlphaU());
	        	
	        	
	        	settings.add("lowerCI", iLCI.toString());
	        	settings.add("upperCI", iUCI.toString());
	        	settings.add("stepCI", controller.getAlphaS().toString());
	        	settings.add("hillClimbingMax", controller.getHillClimbingMax().toString());
	        	settings.write();

	        	retVal = true;
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
		return retVal;
	}
	
	public void setStatus(String msg){
		lbStatus.setText(msg);
	}
	
}




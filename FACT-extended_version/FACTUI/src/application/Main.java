package application;
	
import java.io.IOException;

import application.view.AboutController;
import application.view.AnalysisParams;
import application.view.MainViewController;
import application.view.PrefController;
import application.view.RootController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;
	private MainViewController controller;
	

	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("FACT");
		System.setProperty("logFilename", "logs/l4j.txt");
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		          public void handle(WindowEvent we) {
		              closeDown();
		          }
			}); 
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		showMainView();
	}
	
	public void closeDown(){
		controller.data.fact.terminate();
	}
	
	public Stage getPrimaryStage(){
		return primaryStage;
	}
	
	public void showMainView(){
		try{
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/MainView.fxml"));
			AnchorPane  overviewPage = (AnchorPane)loader.load();
			initRootLayout();
			rootLayout.setCenter(overviewPage);
			
	        // Give the controller access to the main app.
	        controller = loader.getController();
	        controller.setMainApp(this);

		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void initRootLayout() {
	    try {
	        // Load root layout from fxml file.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main.class
	                .getResource("view/RootLayout.fxml"));
	        rootLayout = (BorderPane) loader.load();

	        // Show the scene containing the root layout.
	        Scene scene = new Scene(rootLayout);
	        primaryStage.setScene(scene);

	        // Give the controller access to the main app.
	        RootController controller = loader.getController();
	        controller.setMainApp(this);

	        primaryStage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	}
	
	public void showAboutDialog() {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main.class.getResource("view/About.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("About FACT");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        dialogStage.setResizable(false);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the person into the controller.
	        AboutController controller = loader.getController();
	        controller.setDialogStage(dialogStage);

	        // Show the dialog and wait until the user closes it
	        dialogStage.showAndWait();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public Main(){
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void preferences(){
		controller.showPreferencesDialog();
	}


	public void saveResults() {
		controller.saveResults();
		
	}

}

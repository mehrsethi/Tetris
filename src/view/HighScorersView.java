//InstructionsView - contains the instructions and corresponding videos on how to play the game
package view;

import java.util.ResourceBundle;

import controller.TetrisApp;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HighScorersView {
	private TetrisApp controller;  //access to the controller app
	
	//Panes and scene
	private VBox mainPane;
	private Scene scene;
	
	//other attributes
	private int mainPaneHeight;
	private int mainPaneWidth;
	
	//UI elements
	private Label titleLabel;
	private Button back;
	
	//internationalization
	private ResourceBundle bundle;
	
	//constructor - set up the scene
	public HighScorersView(TetrisApp controller, int height, int width, ResourceBundle bundle) {
		this.controller = controller;
		this.mainPaneHeight = height;
		this.mainPaneWidth = width;
		this.bundle = bundle;
		
		mainPane = new VBox();
		mainPane.setId("mainPane_highScorers");
		
		scene = new Scene(mainPane, mainPaneWidth, mainPaneHeight);
		scene.getStylesheets().add("css/highScorers.css"); //$NON-NLS-1$
		
		//call the relevant methods to set up the UI
		setupPane();
		setupEvents();
		
	}
	
	
	//set up the UI elements within the scene
	private void setupPane() {
		//title label
		titleLabel = new Label(bundle.getString("HighScorersLabel").toUpperCase()); //$NON-NLS-1$
		titleLabel.setId("title_highScorers"); //$NON-NLS-1$
		mainPane.getChildren().addAll(titleLabel);
		
		//make the other labels
		makeHighScorers();
		
		//add the back button
		back = new Button(bundle.getString("back").toUpperCase()); //$NON-NLS-1$
		mainPane.getChildren().addAll(back);
	}

	private void makeHighScorers() {
		String[][] highScorers = controller.getHighScorers();
		Label nameTitle = new Label(bundle.getString("name").toUpperCase()); //$NON-NLS-1$
		Label scoreTitle = new Label(bundle.getString("scoreTitle").toUpperCase()); //$NON-NLS-1$
		Label timeTitle = new Label(bundle.getString("timeTitle").toUpperCase()); //$NON-NLS-1$
		HBox lineTitle = new HBox();
		lineTitle.getChildren().addAll(nameTitle, scoreTitle, timeTitle);
		nameTitle.setId("nameTitle");
		scoreTitle.setId("scoreTitle");
		timeTitle.setId("timeTitle");
		
		mainPane.getChildren().add(lineTitle);
		for (String[] scorer: highScorers) {
			Label name = new Label(scorer[0]);
			Label score = new Label(scorer[1]);
			Label time = new Label(scorer[2]);
			HBox line = new HBox();
			line.getChildren().addAll(name, score, time);
			mainPane.getChildren().add(line);
		}
	}


	//set up the action listeners
	private void setupEvents() {
		//go back to the startview if the back button is clicked
		back.setOnAction(e -> {
			controller.showStartScene();
		});
	}
	
	//getters 
	public Scene getScene() {
		return scene;
	}
}

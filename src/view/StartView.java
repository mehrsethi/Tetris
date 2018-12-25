//StartView - shows the starting scene with the title and buttons to navigate to the other scenes

package view;

import java.util.ResourceBundle;

import controller.TetrisApp;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class StartView {
	private TetrisApp controller; //access to the controller app
	
	//pane
	private Pane mainPane;

	//UI elements
	private Button startButton;
	private Button instructionsButton;
	private Button settingsButton;
	private Button highScorersButton;
	
	//other attributes
	private Scene startScene;
	private int mainPaneHeight;
	private int mainPaneWidth;
	
	//internationalization
	private ResourceBundle bundle;

	//constructor
	public StartView(TetrisApp controller, int height, int width, ResourceBundle bundle) {
		this.controller = controller;
		this.mainPaneHeight = height;
		this.mainPaneWidth = width;
		this.bundle = bundle;
		
		//make the pane and scene
		mainPane = new Pane();
		startScene = new Scene(mainPane, mainPaneWidth, mainPaneHeight, Color.rgb(5, 11, 17));
	
		//adding css
		mainPane.setId("mainPane_start"); //$NON-NLS-1$
		startScene.getStylesheets().add("css/start.css"); //$NON-NLS-1$

		
		//call the relevant methods to set up the UI and add listeners for events
		setupPane();
		setupEvents();
	}

	//set up UI elements
	private void setupPane() {
		//add start, instructions, settings, and highScorers buttons
		startButton = new Button(bundle.getString("StartLabel").toUpperCase()); //$NON-NLS-1$
		startButton.setLayoutX((mainPaneWidth/2)-90);
		startButton.setLayoutY((mainPaneHeight/2)+50);

		instructionsButton = new Button(bundle.getString("InstructionsLabel").toUpperCase()); //$NON-NLS-1$
		instructionsButton.setLayoutX((mainPaneWidth/2)-90);
		instructionsButton.setLayoutY((mainPaneHeight/2)+85);
		
		settingsButton = new Button(bundle.getString("SettingsLabel").toUpperCase()); //$NON-NLS-1$
		settingsButton.setLayoutX((mainPaneWidth/2)-90);
		settingsButton.setLayoutY((mainPaneHeight/2)+120);
		
		highScorersButton = new Button(bundle.getString("HighScorersLabel").toUpperCase()); //$NON-NLS-1$
		highScorersButton.setLayoutX((mainPaneWidth/2)-90);
		highScorersButton.setLayoutY((mainPaneHeight/2)+155);
		
		//add all the UI elements to the mainPane
		mainPane.getChildren().addAll(startButton, instructionsButton, settingsButton, highScorersButton);
	}
	
	//set up action listeners
	public void setupEvents() {
		//if the start button is clicked, go to the game view
		startButton.setOnAction(e -> {
			controller.showGameScene();
		});
		
		//if the instructions button is clicked, go to the instructions view
		instructionsButton.setOnAction(e -> {
			controller.showInstructionsScene(startScene);
		});
		
		//if the settings button is clicked, go to the settings view
		settingsButton.setOnAction(e -> {
			controller.showSettingsScene();
		});
		
		//if the highScorers button is clicked, go to the high scorers view
		highScorersButton.setOnAction(e -> {
			controller.showHighScorersScene();
		});
	}
	
	//getters
	public Scene getScene() {
		return startScene;
	}
}

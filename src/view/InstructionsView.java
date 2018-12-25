//InstructionsView - contains the instructions and corresponding videos on how to play the game
package view;

import java.util.ResourceBundle;

import controller.TetrisApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class InstructionsView {
	private TetrisApp controller;  //access to the controller app
	
	//Panes and scene
	private VBox mainPane;
	private HBox root;
	private Scene scene;
	private Scene previous;
	
	//other attributes
	private int mainPaneHeight;
	private int mainPaneWidth;
	
	//UI elements
	private Label instructions;
	private Label instructions2;
	private Label instructions3;
	private Label instructions4;
	private Label instructions5;
	private Label instructions6;
	private Label titleLabel;
	private Button back;
	private ScrollBar sc;
	
	//URLS for the videos
	private static final String MOVE_URL = "../media/Move.mp4"; //$NON-NLS-1$
	private static final String ROTATE_URL = "../media/Rotate.mp4"; //$NON-NLS-1$
	private static final String DROP_URL = "../media/Drop.mp4"; //$NON-NLS-1$
	private static final String HOLD_URL = "../media/Hold.mp4"; //$NON-NLS-1$

	//mediaplayers and mediaviews for each video
	private MediaPlayer mediaPlayerMove;
	private MediaView mediaViewMove;
	private MediaPlayer mediaPlayerRotate;
	private MediaView mediaViewRotate;
	private MediaPlayer mediaPlayerDrop;
	private MediaView mediaViewDrop;
	private MediaPlayer mediaPlayerHold;
	private MediaView mediaViewHold;
	
	//internationalization
	private ResourceBundle bundle;
	
	//constructor - set up the scene
	public InstructionsView(TetrisApp controller, int height, int width, ResourceBundle bundle, Scene previous) {
		this.controller = controller;
		this.mainPaneHeight = height;
		this.mainPaneWidth = width;
		this.bundle = bundle;
		this.previous = previous;
		
		root = new HBox();
		mainPane = new VBox();
		mainPane.setAlignment(Pos.CENTER);
		mainPane.setSpacing(30);
		mainPane.setId("mainPane_instructions"); //$NON-NLS-1$
		mainPane.setMinHeight(mainPaneHeight*5);
		
		scene = new Scene(root, mainPaneWidth, mainPaneHeight);
		scene.getStylesheets().add("css/instructions.css"); //$NON-NLS-1$
		
		//call the relevant methods to set up the UI
		setUpMedia();
		setupPane();
		setupEvents();
		
		root.getChildren().addAll(mainPane, sc);
	}
	
	//sets up the media elements/videos on the screen
	private void setUpMedia() {
		Media mediaMove = new Media(getClass().getResource(MOVE_URL).toString());
		mediaPlayerMove = new MediaPlayer(mediaMove);
		mediaViewMove = new MediaView(mediaPlayerMove);
		mediaViewMove.setFitWidth(350);
		mediaViewMove.setFitHeight(300);
		mediaPlayerMove.setVolume(0);
		
		Media mediaRotate = new Media(getClass().getResource(ROTATE_URL).toString());
		mediaPlayerRotate = new MediaPlayer(mediaRotate);
		mediaViewRotate = new MediaView(mediaPlayerRotate);
		mediaViewRotate.setFitWidth(350);
		mediaViewRotate.setFitHeight(300);
		mediaPlayerRotate.setVolume(0);
		
		Media mediaDrop = new Media(getClass().getResource(DROP_URL).toString());
		mediaPlayerDrop = new MediaPlayer(mediaDrop);
		mediaViewDrop = new MediaView(mediaPlayerDrop);
		mediaViewDrop.setFitWidth(350);
		mediaViewDrop.setFitHeight(300);
		mediaPlayerDrop.setVolume(0);
		
		Media mediaHold = new Media(getClass().getResource(HOLD_URL).toString());
		mediaPlayerHold = new MediaPlayer(mediaHold);
		mediaViewHold = new MediaView(mediaPlayerHold);
		mediaViewHold.setFitWidth(350);
		mediaViewHold.setFitHeight(300);
		mediaPlayerHold.setVolume(0);
		
		//start all the videos
		mediaPlayerMove.play();
		mediaPlayerRotate.play();
		mediaPlayerDrop.play();
		mediaPlayerHold.play();
	}
	
	//set up the UI elements within the scene
	private void setupPane() {
		//title label
		titleLabel = new Label(bundle.getString("InstructionsLabel").toUpperCase()); //$NON-NLS-1$
		titleLabel.setId("title_instructions"); //$NON-NLS-1$
		
		//call method to make the instruction labels
		makeInstructions();
		
		//add the back button
		back = new Button(bundle.getString("back").toUpperCase()); //$NON-NLS-1$
		
		//add the scrollbar
		sc = new ScrollBar();
        sc.setMin(0);
        sc.setOrientation(Orientation.VERTICAL);
        sc.setMinHeight(mainPaneHeight*5);
        sc.setMax(mainPaneHeight*4);
		
        //add all the elements to the main pane
		mainPane.getChildren().addAll(titleLabel, instructions, instructions2, mediaViewMove, instructions3, mediaViewRotate, instructions4, mediaViewDrop, instructions5, mediaViewHold, instructions6, back);
	}

	//makes the 6 labels that have the instructions on how to play the game
	private void makeInstructions() {
		//text for the instructions labels
		String text = new String(bundle.getString("instructions1")); //$NON-NLS-1$
		text += bundle.getString("instructions2"); //$NON-NLS-1$
		text += bundle.getString("instructions3"); //$NON-NLS-1$
		text += bundle.getString("instructions4"); //$NON-NLS-1$
		text += bundle.getString("instructions5"); //$NON-NLS-1$
		text += bundle.getString("instructions6"); //$NON-NLS-1$
		text += bundle.getString("instructions7"); //$NON-NLS-1$
		text += bundle.getString("instructions8"); //$NON-NLS-1$
		
		String text2 = new String(bundle.getString("instructions9")); //$NON-NLS-1$
		String text3 = new String(bundle.getString("instructions10")); //$NON-NLS-1$
		String text4 = new String(bundle.getString("instructions11")); //$NON-NLS-1$
		String text5 = new String(bundle.getString("instructions12")); //$NON-NLS-1$
		String text6 = new String(bundle.getString("instructions13")); //$NON-NLS-1$

		//make the labels, add the relevant text
		instructions = new Label(text);
		instructions2 = new Label(text2);
		instructions3 = new Label(text3);
		instructions4 = new Label(text4);
		instructions5 = new Label(text5);
		instructions6 = new Label(text6);
	}

	
	//set up the action listeners
	private void setupEvents() {
		//go back to the startview if the back button is clicked
		back.setOnAction(e -> {
			controller.showScene(previous);
		});
		
		//scrolling using the scrollbar
        sc.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                        root.setLayoutY(-new_val.doubleValue());
                }
        });
        
        //make all the mediaplayers autorepeat
		mediaPlayerMove.setOnEndOfMedia(new Runnable() {
		       public void run() {
		    	   mediaPlayerMove.seek(Duration.ZERO);
		       }
		});
		mediaPlayerRotate.setOnEndOfMedia(new Runnable() {
		       public void run() {
		    	   mediaPlayerRotate.seek(Duration.ZERO);
		       }
		});
		mediaPlayerDrop.setOnEndOfMedia(new Runnable() {
		       public void run() {
		    	   mediaPlayerDrop.seek(Duration.ZERO);
		       }
		});
		mediaPlayerHold.setOnEndOfMedia(new Runnable() {
		       public void run() {
		    	   mediaPlayerHold.seek(Duration.ZERO);
		       }
		});
	}
	
	//getters 
	public Scene getScene() {
		return scene;
	}
}

//GameView - the view that contains the actual playing field/board for the game
package view;

import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class GameView {
	
	//panes
	private Scene gameScene;
	private Pane mainPane;
	private BorderPane root;
	private StackPane spR;
	private StackPane spL;
	private Pane paneR;
	private Pane paneL;
	
	//visual/GUI elements
	private Rectangle[] blockGUI;
	private Rectangle[] nextBlockGUI;
	private ArrayList<Rectangle> blockBoxArray;
	private Label scoreLabel;
	private Button reset;
	private Button back;
	private Button instructions;
	private Label timeLabel;
	private Button enterButton;
	private Button newGameButton;
	private TextField nameTF;
	
	//internationalization
	private ResourceBundle bundle;
	
	//other attributes
	private static final int BOX_SIZE = 20;
	private int height;
	
	//constructor
	public GameView(int height, int width, ResourceBundle bundle) {
		//set up the attributes that were passed in as input to the constructor
		this.height = height;
		this.bundle = bundle;

		mainPane = new Pane();
		mainPane.setMinHeight(height);
		mainPane.setMinWidth(width);
		
		//make all the panes/GUI elements
		makePanes();
		makeLeft();
		makeRight();
		
		//set up other attributes
		blockBoxArray = new ArrayList<Rectangle>();
		gameScene = new Scene(root, width+400, height);
		gameScene.getStylesheets().add("css/game.css"); //$NON-NLS-1$
		

	}

	//make all the panes
	private void makePanes() {
		root = new BorderPane();
		paneL = new Pane();
		paneR = new Pane();
		
		paneL.setId("paneL"); //$NON-NLS-1$
		paneR.setId("paneR"); //$NON-NLS-1$
		
		root.setCenter(mainPane);
	}
	
	//makes the left part of the borderpane
	//VBox with a pane for the held block, the current score, and the time
	private void makeLeft() {
		//vbox for the left side
		VBox vb = new VBox();
		vb.setId("left"); //$NON-NLS-1$
		
		//necessary labels
		Label hold = new Label(bundle.getString("hold").toUpperCase()); //$NON-NLS-1$
		scoreLabel = new Label(bundle.getString("scoreStart").toUpperCase()); //$NON-NLS-1$
		timeLabel = new Label(bundle.getString("timeStart").toUpperCase()); //$NON-NLS-1$
		
		//add everything to the relevant containers
		spL = new StackPane();
		vb.getChildren().addAll(hold, spL, scoreLabel, timeLabel);
		root.setLeft(vb);
		spL.getChildren().addAll(paneL);
	}

	//makes the right part of the borderpane
	//VBox with a pane for previewing the upcoming block and buttons to navigate to other scenes
	private void makeRight() {
		//vbox for the right side
		VBox vb = new VBox();
		vb.setId("right"); //$NON-NLS-1$
		vb.setMinHeight(height);

		//preview label and stackpane for the top
		Label preview = new Label(bundle.getString("preview").toUpperCase()); //$NON-NLS-1$
		spR = new StackPane();
		spR.getChildren().addAll(paneR);
		
		//Buttons
		reset = new Button(bundle.getString("reset").toUpperCase()); //$NON-NLS-1$
		back = new Button(bundle.getString("back").toUpperCase()); //$NON-NLS-1$
		instructions = new Button(bundle.getString("InstructionsLabel").toUpperCase()); //$NON-NLS-1$

		//VBox for buttons
		VBox buttonBox = new VBox();
		buttonBox.setId("buttonbox");
		buttonBox.getChildren().addAll(reset, back, instructions);
		buttonBox.setPrefHeight(height-paneR.getHeight());
		
		//add everything to the bigger vbox
		vb.getChildren().addAll(preview, spR, buttonBox);
		root.setRight(vb);
	}
	
	
	//rotate methods simulates the rotation of a block on the screen
	//inputs are the x and y coordinates of all boxes except the pivot
	public void rotate(double x1, double y1, double x2, double y2, double x3, double y3) {
		blockGUI[1].setX(x1);
		blockGUI[1].setY(y1);
		
		blockGUI[2].setX(x2);
		blockGUI[2].setY(y2);
		
		blockGUI[3].setX(x3);
		blockGUI[3].setY(y3);
	}
	
	//simulates moving the block down in the UI
	public void moveBlockDown() {
		blockGUI[0].setY(blockGUI[0].getY()+BOX_SIZE);
		blockGUI[1].setY(blockGUI[1].getY()+BOX_SIZE);
		blockGUI[2].setY(blockGUI[2].getY()+BOX_SIZE);
		blockGUI[3].setY(blockGUI[3].getY()+BOX_SIZE);
	}
	
	//simulates moving the block left in the UI
	public void moveBlockLeft() {
		blockGUI[0].setX(blockGUI[0].getX()-BOX_SIZE);
		blockGUI[1].setX(blockGUI[1].getX()-BOX_SIZE);
		blockGUI[2].setX(blockGUI[2].getX()-BOX_SIZE);
		blockGUI[3].setX(blockGUI[3].getX()-BOX_SIZE);
	}
	
	//simulates moving the block right in the UI
	public void moveBlockRight() {
		blockGUI[0].setX(blockGUI[0].getX()+BOX_SIZE);
		blockGUI[1].setX(blockGUI[1].getX()+BOX_SIZE);
		blockGUI[2].setX(blockGUI[2].getX()+BOX_SIZE);
		blockGUI[3].setX(blockGUI[3].getX()+BOX_SIZE);
	}

	
	//updates the time label based on the hour, minute, and second input
	public void updateTimeLabel(int hour, int minute, int second) {
		timeLabel.setText(bundle.getString("time").toUpperCase() + hour + ":" + minute + ":" + second); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	//updates the score label based on the score input
	public void updateScoreLabel(int score) {
		scoreLabel.setText(bundle.getString("score").toUpperCase() + score); //$NON-NLS-1$
	}

	//updates the blockboxarray based on the blockGUI
	public void updateBlockArray() {
		for (int i=0; i<blockGUI.length;i++) {
			blockBoxArray.add(blockGUI[i]);
		}
	}
	
	//getters
	public Node getRoot() {
		return root;
	}
	
	public Scene getGameScene() {
		return gameScene;
	}

	public Node getResetButton() {
		return reset;
	}

	public Node getBackButton() {
		return back;
	}

	public Node getInstructionsButton() {
		return instructions;
	}
	
	public ArrayList<Rectangle> getBlockBoxArray() {
		return blockBoxArray;
	}
	
	public Pane getPaneL() {
		return paneL;
	}
	
	public double getPivotX() {
		return blockGUI[0].getX();
	}
	
	public double getPivotY() {
		return blockGUI[0].getY();
	}
	
	public Button getEnterButton() {
		return enterButton;
	}
	
	public Button getNewGameButton() {
		return newGameButton;
	}
	
	public TextField getNameTF() {
		return nameTF;
	}
	
	//sets the nextBlockGUI array (to be out of the playing field and in the preview area) and updates the UI accordingly
	public void setNextBlockGUI(Rectangle[] newBlockGUI) {
		nextBlockGUI = newBlockGUI;
		paneR.getChildren().addAll(nextBlockGUI);
	}
	
	//updates the variables and UI to represent the block moving into the playing field and no longer being a preview
	public void shiftBlockIntoField(int x) {
		paneR.getChildren().removeAll(nextBlockGUI);
		blockGUI = nextBlockGUI;
		blockGUI[0].setX(x);
		blockGUI[0].setY(0);
		mainPane.getChildren().addAll(blockGUI);
	}
	
	//if this is the first block being held, update the UI accordingly
	public double[][] moveBlockToHeldFirst(int x, int y) {
		double[][] toReturn = new double[4][2];
		
		double pivotX = blockGUI[0].getX();
		double pivotY = blockGUI[0].getY();
		int count = 0;
		for (Rectangle r: blockGUI) {
			toReturn[count][0] = r.getY();
			toReturn[count][1] = r.getX();
			r.setX(x+r.getX()-pivotX);
			r.setY(y+r.getY()-pivotY);
			count += 1;
		}
		paneL.getChildren().addAll(blockGUI);
		mainPane.getChildren().removeAll(blockGUI);
		
		return toReturn;
	}
	
	//if there is already a block that is held, simulate a visual switch between the two blocks
	public Rectangle[] moveBlockToHeldExisting(int x, int y) {
		Rectangle pivot = (Rectangle)paneL.getChildren().get(0);
		Rectangle box1 = (Rectangle)paneL.getChildren().get(1);
		Rectangle box2 = (Rectangle)paneL.getChildren().get(2);
		Rectangle box3 = (Rectangle)paneL.getChildren().get(3);
		Rectangle[] tempBlockGUI = {pivot, box1, box2, box3};
		
		Rectangle[] toReturn = blockGUI;
		
		double pivotX = blockGUI[0].getX();
		double pivotY = blockGUI[0].getY();
		for (Rectangle r: blockGUI) {
			r.setX(x+r.getX()-pivotX);
			r.setY(y+r.getY()-pivotY);
		}

		paneL.getChildren().addAll(blockGUI);
		mainPane.getChildren().removeAll(blockGUI);
		
		tempBlockGUI[0].setX(pivotX);
		tempBlockGUI[0].setY(pivotY);

		blockGUI = tempBlockGUI;
		mainPane.getChildren().addAll(blockGUI);
		paneL.getChildren().removeAll(blockGUI);

		return toReturn;
	}

	//checks if the block is exceeding the bounds on the left
	public boolean checkLeft() {
		return blockGUI[1].getX()<0||blockGUI[2].getX()<0||blockGUI[3].getX()<0;
	}
	
	//checks if the block is exceeding the bounds on the right
	public boolean checkRight() {
		return blockGUI[1].getX()>=mainPane.getWidth()||blockGUI[2].getX()>=mainPane.getWidth()||blockGUI[3].getX()>=mainPane.getWidth();
	}
	
	//removes a child from the mainPane
	public void removeFromMainPane(Rectangle c) {
		mainPane.getChildren().remove(c);
	}
	

	//shows a pop up dialog when the user loses or wants to go back to the main menu
	//boolean input to distinguish between the two cases
	public void namePopUp(boolean lost) {
		//make custom dialog that returns a string representing the player's name
		Dialog<String> dialog = new Dialog<>();
		
		if (lost) {
			dialog.setTitle(bundle.getString("loser")); //$NON-NLS-1$
			dialog.setHeaderText(bundle.getString("lostMessage")); //$NON-NLS-1$
		}
		else {
			dialog.setTitle(bundle.getString("HighScorersLabel")); //$NON-NLS-1$
			dialog.setHeaderText(bundle.getString("namePrompt")); //$NON-NLS-1$
		}
		
		// Set the button types.
		ButtonType enterButtonType = new ButtonType("Enter", ButtonData.APPLY);
		ButtonType newGameButtonType = new ButtonType("New Game", ButtonData.OK_DONE);
		ButtonType closeButtonType = new ButtonType("Close", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(enterButtonType, newGameButtonType, closeButtonType);

		//add text field to the content field of the dialog
		nameTF = new TextField();
		Label nameL = new Label(bundle.getString("name"));
		
		HBox box = new HBox();
		box.getChildren().addAll(nameL, nameTF);
		box.setAlignment(Pos.CENTER);
		
		dialog.getDialogPane().setContent(box);
		
		//get button reference to add action listeners
		enterButton = (Button)dialog.getDialogPane().lookupButton(enterButtonType);
		newGameButton = (Button)dialog.getDialogPane().lookupButton(newGameButtonType);
		enterButton.setDisable(true);
		
		//convert result to a string (name)
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == enterButtonType) {
		        return nameTF.getText();
		    }
		    return null;
		});
		
		//call the showAndWait method to execute dialog
		//make sure that it is consistent with the animation timer by using runLater
		Platform.runLater(() -> {
			dialog.showAndWait();
		});
		
		//add a listener to the name text field to only enable the enter button when the user enters a name
		nameTF.textProperty().addListener((observable, oldVal, newVal) -> {
			enterButton.setDisable(newVal.trim().isEmpty());
		});
	}
}

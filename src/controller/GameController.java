//Controller for the main game scene - ensures separation of concerns by connecting the model and the view
package controller;

import java.util.Iterator;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.Block;
import model.Box;
import model.GameGrid;
import model.LeftFour;
import model.LeftL;
import model.Line;
import model.RightFour;
import model.RightL;
import model.T;
import view.BlockFactory;
import view.GameView;

public class GameController {
	private GameView gameView; //access to the visual/UI elements through the gameView
	private TetrisApp controller; //access to the controller app
	
	
	private AnimationTimer timer; //AnimationTimer for animating block dropping

	//access to model elements
	private GameGrid grid;
	private Block nextBlock;
	private Block block;
	private Block heldBlock;
	private BlockFactory factory;
	
	//other attributes
	private Random r;
	private static final int BOX_SIZE = 20;
	private float difficultyMultiplier;
	private boolean music;
	private boolean sound;
	private int score;
	private int timeCounter;
	private int width;
	private boolean playing;

	//the urls for all the sounds in the game
	private static final String MOVE_URL = "../media/TetrisMove.wav"; //$NON-NLS-1$
	private static final String ROTATE_URL = "../media/TetrisRotate.wav"; //$NON-NLS-1$
	private static final String DROP_URL = "../media/TetrisDrop.wav"; //$NON-NLS-1$
	private static final String DISAPPEAR_URL = "../media/TetrisDisappear.wav"; //$NON-NLS-1$
	private static final String BACKGROUND_URL = "../media/TetrisBackground.mp3"; //$NON-NLS-1$
	//mediaPlayers for all the sounds that need to be played during the game
	private MediaPlayer moveSoundPlayer;
	private MediaPlayer rotateSoundPlayer;
	private MediaPlayer dropSoundPlayer;
	private MediaPlayer disappearSoundPlayer;
	private MediaPlayer backgroundSoundPlayer;

	
	//constructor
	public GameController(TetrisApp tetrisApp, String difficulty, int height, int width, boolean music, boolean sound, ResourceBundle bundle) {
		//set the main attributes
		this.controller = tetrisApp;
		gameView = new GameView(height, width, bundle);
		this.music = music;
		this.sound = sound;
		this.width = width;
		
		if (difficulty.equals(bundle.getString("hard"))) { //$NON-NLS-1$
			this.difficultyMultiplier = 5;
		}
		else if (difficulty.equals(bundle.getString("medium"))) { //$NON-NLS-1$
			this.difficultyMultiplier = 3;
		}
		else {
			this.difficultyMultiplier = 1;
		}
		
		r = new Random();
		score = 0;
		grid = new GameGrid((int)height/BOX_SIZE, (int)width/BOX_SIZE);
		factory = new BlockFactory(BOX_SIZE);
		timeCounter = 0;
		playing = true;
		
		//animate the blocks
		animate();
		
		//set up the events
		setUpKeys();
		setUpButtonEvents();
		
		//set up everything for music/sound effects
		addMusic();
	}
	
	//getters
	public GameView getGameView() {
		return gameView;
	}
	
	//make the media objects for all the sounds/music in the view
	//make the relevant mediaplayers to add to the scene
	private void addMusic() {
		try {
			Media move_sound = new Media(getClass().getResource(MOVE_URL).toString());
			Media rotate_sound = new Media(getClass().getResource(ROTATE_URL).toString());
			Media drop_sound = new Media(getClass().getResource(DROP_URL).toString());
			Media disappear_sound = new Media(getClass().getResource(DISAPPEAR_URL).toString());
			Media background_sound = new Media(getClass().getResource(BACKGROUND_URL).toString());
			moveSoundPlayer = new MediaPlayer(move_sound);
			rotateSoundPlayer = new MediaPlayer(rotate_sound);
			dropSoundPlayer = new MediaPlayer(drop_sound);
			disappearSoundPlayer = new MediaPlayer(disappear_sound);
			backgroundSoundPlayer = new MediaPlayer(background_sound);
		}
		catch(Exception e){
			System.err.println("Error getting media file: " + e); //$NON-NLS-1$
			System.exit(0);
		}
		
		//background music repeats indefinitely
		backgroundSoundPlayer.setOnEndOfMedia(new Runnable() {
		       public void run() {
		    	   backgroundSoundPlayer.seek(Duration.ZERO);
		       }
		});
	}
	
	//animate the falling blocks
	private void animate() {
		timer = new AnimationTimer() {
			long last = 0;
			@Override
			public void handle(long now) {
				//determine speed of the block using difficultyMultiplier
				//move the block after a certain amount of time has passed
				if (now-last>=1_000_000_000/difficultyMultiplier) {
					downKeyPressed(false);
					//if the user lost, show the pop up and stop the timer
					if (lost()) {
						timer.stop();
						playing = false;
						namePopUp(true);
					}
					timeCounter += 1;
					int hour = (int)(timeCounter/3600);
					int minute = (int)(timeCounter/60) - hour;
					int second = timeCounter - minute*60;
					gameView.updateTimeLabel(hour, minute, second);
					last = now;
				}
			}
		};
	}
	
	//set up keys adds the relevant key pressed events for each key
	//the up arrow rotates the block
	//the other arrow keys move the block
	//the spacebar drop the block to the bottom
	//the esc key pauses the game
	//the H key moves a block into the held position
	private void setUpKeys() {
		gameView.getRoot().setOnKeyPressed(kpe -> {
			KeyCode kc = kpe.getCode();
			if (playing) {
				switch(kc) {
					case UP: //using up key to rotate
						upKeyPressed();
						break;
					case DOWN: //move down
						downKeyPressed(true);
						break;
					case LEFT: //move left
						leftKeyPressed();
						break;
					case RIGHT: //move right
						rightKeyPressed();
						break;
					case ESCAPE: //pause the game
						timer.stop();
						backgroundSoundPlayer.stop();
						break;
					case SPACE: //hard drop the block
						spacebarPressed();
						break;
					case H: //move a block to the held position
						hKeyPressed();
						break;
					default:
						break;
				}
			}
			if (lost()) {
				timer.stop();
				playing = false;
				namePopUp(true);
			}
		});
	}
	
	//helper methods to process key press events
	//up key pressed - rotate the block and play the relevant sound
	private void upKeyPressed() {
		timer.start();
		if (music&&backgroundSoundPlayer.getStatus()==Status.STOPPED) {
			backgroundSoundPlayer.seek(Duration.ZERO);
			backgroundSoundPlayer.play();
		}
		rotate();
		if (sound) {
			rotateSoundPlayer.play();
			rotateSoundPlayer.seek(Duration.ZERO);
		}
	}
	
	//down key pressed/animation timer update - move the block down if possible and play the relevant sound
	//also check if the user cleared a row
	//the boolean keyPress represents whether we're moving the block down based on a key click or just through the game loop
	private void downKeyPressed(boolean keyPress) {
		timer.start();
		if (music&&backgroundSoundPlayer.getStatus()==Status.STOPPED) {
			backgroundSoundPlayer.seek(Duration.ZERO);
			backgroundSoundPlayer.play();
		}
		//check again if the block has space to move down - if not, play the relevant sound
		boolean touching = grid.moveBlock(block, "downC"); //only checking not moving in the model //$NON-NLS-1$
		if (touching) {
			if (sound) {
				dropSoundPlayer.play();
				dropSoundPlayer.seek(Duration.ZERO);							}
			gameView.updateBlockArray();
			deleteRow();
			makeBlock();
		}
		
		//if the block can move down
		touching = grid.moveBlock(block, "down"); //$NON-NLS-1$
		if (!touching) {
			//move both the model and the UI
			block.incrementPivotY1();
			gameView.moveBlockDown();
			if (sound&&keyPress) {
				moveSoundPlayer.play();
				moveSoundPlayer.seek(Duration.ZERO);
			}
		}
	}
	
	//left key pressed - move the block leftward if possible and play the relevant sound
	private void leftKeyPressed() {
		timer.start();
		if (music&&backgroundSoundPlayer.getStatus()==Status.STOPPED) {
			backgroundSoundPlayer.seek(Duration.ZERO);
			backgroundSoundPlayer.play();
		}
		boolean touching = grid.moveBlock(block, "left"); //$NON-NLS-1$
		if (!touching) {
			gameView.moveBlockLeft();
			block.decrementPivotX1();
			if (sound) {
				moveSoundPlayer.play();
				moveSoundPlayer.seek(Duration.ZERO);
			}
		}
	}
	
	//right key pressed - move the block rightward if possible and play the relevant sound
	private void rightKeyPressed() {
		if (music&&backgroundSoundPlayer.getStatus()==Status.STOPPED) {
			backgroundSoundPlayer.seek(Duration.ZERO);
			backgroundSoundPlayer.play();
		}
		timer.start();
		boolean touching = grid.moveBlock(block, "right"); //$NON-NLS-1$
		if (!touching) {
			gameView.moveBlockRight();
			block.incrementPivotX1();
			if (sound) {
				moveSoundPlayer.play();
				moveSoundPlayer.seek(Duration.ZERO);
			}
		}
	}
	
	//space key pressed - hard drop the block all the way to the bottom play the relevant sound
	//check if the user cleared a row
	private void spacebarPressed() {
		boolean touching = grid.moveBlock(block, "down"); //$NON-NLS-1$
		while (!touching) {
			block.incrementPivotY1();
			gameView.moveBlockDown();
			touching = grid.moveBlock(block, "down"); //$NON-NLS-1$
		}
		if (sound) {
			dropSoundPlayer.play();
			dropSoundPlayer.seek(Duration.ZERO);
		}
		gameView.updateBlockArray();
		deleteRow();
		makeBlock();
	}
	
	//H key pressed - move the block to the left outside the playing field to represent being held
	private void hKeyPressed() {
		//if this is the first time the user is a holding a block, generate a new block to take the place of the held block
		if (gameView.getPaneL().getChildren().size()==0) {
			int[] centerHelperArray = block.getCenterPivotCoordinates(100, 180);
			double[][] arr = gameView.moveBlockToHeldFirst(centerHelperArray[0], centerHelperArray[1]);
			for (double[] r: arr) {
				grid.switchOff((int)(r[0]/BOX_SIZE), (int)(r[1]/BOX_SIZE));
			}
			heldBlock = block;
			makeBlock();
		}
		//if the held section already has a block, switch the two blocks
		else {
			int[] centerHelperArray = block.getCenterPivotCoordinates(100, 180);
			Rectangle[] arr = gameView.moveBlockToHeldExisting(centerHelperArray[0], centerHelperArray[1]);
			for (Rectangle r: arr) {
				grid.switchOff((int)(r.getY()/BOX_SIZE), (int)(r.getX()/BOX_SIZE));
			}

			Block tempBlock = heldBlock;
			heldBlock = block;
			block = tempBlock;
			block.setPivotX1(gameView.getPivotX());
			block.setPivotY1(gameView.getPivotY());
			
			for (int i=0; i<4; i++) {
				rotate(); //rotating four times allows for the other blocks to orient themselves according to the pivot without having explicit code 
			}
		}
	}
	
	//set up the button events for the four buttons
	private void setUpButtonEvents() {
		gameView.getResetButton().setOnMouseClicked(me -> {
			backgroundSoundPlayer.stop();
			controller.showGameScene();
		});
		gameView.getBackButton().setOnMouseClicked(me -> {
			backgroundSoundPlayer.stop();
			timer.stop();
			namePopUp(false);
		});
		gameView.getInstructionsButton().setOnMouseClicked(me -> {
			backgroundSoundPlayer.stop();
			timer.stop();
			controller.showInstructionsScene(gameView.getGameScene());
		});
	}
	
	//public method to start the game by making the blocks and starting the music and the timer
	public void start() {
		makeBlock();
		makeBlock();
		if (music) {
			backgroundSoundPlayer.play();
		}
		timer.start();
		gameView.getRoot().requestFocus();
	}
	
	//makes a block in the model and the UI randomly using the factory method
	private void makeBlock() {
		if (nextBlock!=null) {
			block = nextBlock;
			int x = (r.nextInt((int)((width-BOX_SIZE)/BOX_SIZE))*BOX_SIZE)+BOX_SIZE;
			gameView.shiftBlockIntoField(x);
			block.setPivotX1(x);
			block.setPivotY1(0);
			randomizeDirection();
		}
		int num = r.nextInt(7);
		if (num==0) { //make a box
			gameView.setNextBlockGUI(factory.makeBox(90, 30));
			nextBlock = new Box(90, 30, BOX_SIZE);
		}
		else if (num==1) { //make a line
			gameView.setNextBlockGUI(factory.makeLine(80, 50));
			nextBlock = new Line(80, 50, BOX_SIZE);
		}
		else if (num==2) { //make a T
			gameView.setNextBlockGUI(factory.makeT(80, 30));
			nextBlock = new T(80, 30, BOX_SIZE);
		}
		else if (num==3) { //make a leftL
			gameView.setNextBlockGUI(factory.makeLeftL(60, 50));
			nextBlock = new LeftL(60, 50, BOX_SIZE);
		}
		else if (num==4) { //make a rightL
			gameView.setNextBlockGUI(factory.makeRightL(60, 30));
			nextBlock = new RightL(60, 30, BOX_SIZE);
		}
		else if (num==5) { //make a leftFour
			gameView.setNextBlockGUI(factory.makeLeftFour(80, 30));
			nextBlock = new LeftFour(80, 30, BOX_SIZE);
		}
		else if (num==6) { //make a rightFour
			gameView.setNextBlockGUI(factory.makeRightFour(80, 30));
			nextBlock = new RightFour(80, 30, BOX_SIZE);
		}
	}
	
	//randomize the direction that the current block is facing
	private void randomizeDirection() {
		int num = r.nextInt(4);
		for (int i=-1; i<num; i++) {
			rotate();
		}
	}
	
	//rotate the block by using the logic from the model
	private void rotate() {
		double x1 = block.getBox1Coordinates().get(0);
		double y1 = block.getBox1Coordinates().get(1);
		
		double x2 = block.getBox2Coordinates().get(0);
		double y2 = block.getBox2Coordinates().get(1);

		double x3 = block.getBox3Coordinates().get(0);
		double y3 = block.getBox3Coordinates().get(1);
	
		gameView.rotate(x1, y1, x2, y2, x3, y3);
		
		block.changeSide();
		grid.changeSides(block);
	
		checkBoundary();
	}
	
	//checks if the current block has moved past the boundary on any direction, and moves it to be back within the playing field
	private void checkBoundary() {
		//checking left
		while (gameView.checkLeft()) {
			gameView.moveBlockRight();
			grid.moveBlock(block, "right"); //$NON-NLS-1$
			block.incrementPivotX1();
		}
		//checking right
		while (gameView.checkRight()) {
			gameView.moveBlockLeft();
			grid.moveBlock(block, "left"); //$NON-NLS-1$
			block.decrementPivotX1();
		}
	}
	
	//delete a row from the model and the view and move all the relevant blocks down
	//if the space below a block from the row moving down is empty, drop more than just one row
	private void deleteRow() {
		int r = grid.checkFullRow();
		int y = r*BOX_SIZE;
		while (r!=-1) {
			score +=1;
			gameView.updateScoreLabel(score);
			//increase speed of the falling blocks for every 5 rows cleared
			if (score%2==5 && score<100) {
				difficultyMultiplier *= 2;
			}

			grid.deleteRow(r);
			Iterator<Rectangle> it = gameView.getBlockBoxArray().iterator();
			while (it.hasNext()) {
				Rectangle c = it.next();
				//remove the box of a block that was part of the row being deleted
				if (c.getY()==(double)y) {
					it.remove();
					gameView.removeFromMainPane(c);
				}
				//move all the blocks higher than the row being deleted down by one row
				else if (c.getY()<(double)y) {
					c.setY(c.getY()+BOX_SIZE);
					int y1 = (int)c.getY()/BOX_SIZE;
					int x1 = (int)c.getX()/BOX_SIZE;
					boolean isOn = grid.isOn(y1+1, x1);
					while (!isOn) {
						c.setY(c.getY()+BOX_SIZE);
						grid.switchOff(y1, x1);
						grid.switchOn(y1+1, x1);
						y1 = (int)c.getY()/BOX_SIZE;
						x1 = (int)c.getX()/BOX_SIZE;
						isOn = grid.isOn(y1+1, x1);
					}
				}
			}
			//play the relevant sound
			if (sound) {
				disappearSoundPlayer.play();
				disappearSoundPlayer.seek(Duration.ZERO);
			}
			r = grid.checkFullRow();
			y = r*BOX_SIZE;
		}
	}
	
	//checks if the player has lost the game or not
	//a game is lost if any of the boxes touches the top of the screen
	private boolean lost() {
		for (Rectangle r: gameView.getBlockBoxArray()) {
			if (r.getY()<0) {
				return true;
			}
		}
		return false;
	}
	
	
	//shows a pop up when the user loses or wants to go back to the main menu by calling the relevant method from the view
	//add listeners to the relevant buttons from the dialog
	private void namePopUp(boolean lost) {
		gameView.namePopUp(lost);

		gameView.getEnterButton().setOnAction(me -> {
			controller.updateHighScorers(gameView.getNameTF().getText(), Integer.toString(score), Integer.toString(timeCounter));
			if (lost) {
				controller.showHighScorersScene();
			}
			else {
				controller.showStartScene();
			}
		});
		
		gameView.getNewGameButton().setOnAction(me -> {
			controller.showGameScene();
		});
	}
}

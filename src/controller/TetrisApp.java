//TetrisApp extends from Application and runs the main program of the game
//It switches between the windows and sets the preferences of the game as necessary
package controller;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.GameView;
import view.HighScorersView;
import view.InstructionsView;
import view.SettingsView;
import view.StartView;

public class TetrisApp extends Application {
	
	//stage and all the views/controllers
	private Stage primaryStage;
	private StartView startView;
	private GameController gameController;
	private InstructionsView instructionsView;
	private SettingsView settingsView;
	private HighScorersView highScorersView;
	
	//the other attributes
	private boolean music;
	private boolean sound;
	private String difficulty;
	private int height;
	private int width;
	private String[][] highScorers;
	private String language;
	private ResourceBundle bundle;
	
	//constants
	private static final int HEIGHT = 500;
	private static final int WIDTH = 1000;

	
	//main method to actually run the program
	public static void main(String[] args){
		launch(args);
	}
	
	//override the start method
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		
		//default values
		music = true;
		sound = true;
		difficulty = "Easy";
		height = 600;
		width = 360;
		bundle = ResourceBundle.getBundle("text", Locale.getDefault());
		language = "en";
		
		highScorers = new String[5][3];
		makeHighScorers();
		
		//start by showing the start scene
		showStartScene();
		primaryStage.show();
		primaryStage.setResizable(false);
	}
	
	// return the window's stage
	public Stage getStage(){
		return primaryStage;
	}
	
	// display the starting scene
	public void showStartScene(){
		startView = new StartView(this, HEIGHT, WIDTH, bundle);
		primaryStage.setScene(startView.getScene());
	}
	
	// display the game scene
	public void showGameScene() {
		gameController = new GameController(this, difficulty, height, width, music, sound, bundle);
		GameView gameView = gameController.getGameView();
		primaryStage.setScene(gameView.getGameScene());
		gameController.start();
	}
	
	//display the instructions scene
	public void showInstructionsScene(Scene s) {
		instructionsView = new InstructionsView(this, HEIGHT, WIDTH, bundle, s);
		primaryStage.setScene(instructionsView.getScene());
	}
	
	//display the settings scene
	//for persisting the preferences, there is an if/else for wheter to make a new scene or not 
	public void showSettingsScene() {
		if (settingsView==null) {
			settingsView = new SettingsView(this, HEIGHT, WIDTH, bundle);
		}
		else {
			settingsView.setPrefs(music, sound, difficulty, height, width, language);
		}
		primaryStage.setScene(settingsView.getScene());
	}
	
	//display the high scorers scene
	public void showHighScorersScene() {
		highScorersView = new HighScorersView(this, HEIGHT, WIDTH, bundle);
		primaryStage.setScene(highScorersView.getScene());
	}
	
	//show a specific scene passed as input
	public void showScene(Scene s) {
		primaryStage.setScene(s);
	}
	
	//setters for settings to modify
	public void setDifficulty(String string) {
		difficulty = string;
	}

	public void setMusic(boolean selected) {
		music = selected;	
	}

	public void setSound(boolean selected) {
		sound = selected;		
	}

	public void setGridSize(Integer h, Integer w) { 
		height = h;
		width = w;
	}
	
	//change the language of the program if the user changes it in settings
	public void changeLanguage(String lang) {
		language = lang;
		bundle = ResourceBundle.getBundle("text", Locale.forLanguageTag(lang));
		this.showSettingsScene();
	}

	
	//read the JSON file to make the list of high scorers
	private void makeHighScorers() {
		JSONParser parser = new JSONParser();
		try {
            JSONArray arr = (JSONArray)parser.parse(new FileReader("highScorers.json"));
            int row = 0;
            for (Object array : arr ) {
            	int col = 0;
            	JSONArray jsonArray = (JSONArray) array;
            	for (Object o : jsonArray) {
            		String s = (String) o;
            		highScorers[row][col] = s;
            		col+=1;
            	}
            	row+=1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	//write to the JSON file once a game ends and the user has been entered into the high scorers list
	public void updateHighScorers(String name, String score, String time) {
		//figure out if the user fits into the top 5 scores
		int row = 0;
		for (String[] scorer : highScorers) {
			if (scorer[2].equals("∞") || (Integer.parseInt(score) > Integer.parseInt(scorer[1])) || (Integer.parseInt(score) == Integer.parseInt(scorer[1]) && Integer.parseInt(time) <= Integer.parseInt(scorer[2]))) {
				for (int i=highScorers.length-1; i>=row+1; i--) {
					highScorers[i][0] = highScorers[i-1][0];
					highScorers[i][1] = highScorers[i-1][1];
					highScorers[i][2] = highScorers[i-1][2];
				}
				highScorers[row][0] = name;
				highScorers[row][1] = score;
				highScorers[row][2] = time;
				break;
			}
			row += 1;
		}
		
		//write to file
        JSONArray bigList = new JSONArray();
        
        for (String[] scorer : highScorers) {
        	JSONArray list = new JSONArray();
        	list.add(scorer[0]);
            list.add(scorer[1]);
            list.add(scorer[2]);
            bigList.add(list);
        }

        try (FileWriter file = new FileWriter("highScorers.json")) {

            file.write(bigList.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	//getters
	public String[][] getHighScorers() {
		return highScorers;
	}
	
	public Scene getStartScene() {
		return startView.getScene();
	}
 }


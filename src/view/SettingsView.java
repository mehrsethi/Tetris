//SettingsView - has the UI to allow modification of settings of the game
//Settings that can be edited include - turning the music on and off, turning sound effects on and off,
//choosing a custom grid size, choosing out of the given grid sizes,
//choosing difficulty level (which changes the speed of the falling blocks)

package view;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import controller.TetrisApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SettingsView {
		private TetrisApp controller; //access to the controller app
		private int mainPaneHeight;
		private int mainPaneWidth;
		
		//visual/UI elements
		private VBox root;
		private Scene scene;
		
		private Label titleLabel;
		private Label music;
		private Label sound;
		private Label gridSize;
		private Label difficultyLevel;
		private Label language;
		
		private CheckBox musicCB;
		private CheckBox soundCB;
		private ComboBox<Integer> heightCB;
		private ComboBox<Integer> widthCB;
		private RadioButton difficultyEasy;
		private RadioButton difficultyMedium;
		private RadioButton difficultyHard;
		private ToggleGroup difficultyTG;
		private ComboBox<String> languageCB;
		private Button back;
		
		//internationalization
		private ResourceBundle bundle;
		
		//constructor - set up the scene
		public SettingsView(TetrisApp controller, int height, int width, ResourceBundle bundle) {
			this.controller = controller;
			this.mainPaneHeight = height;
			this.mainPaneWidth = width;
			this.bundle = bundle;
			
			root = new VBox();
			root.setAlignment(Pos.CENTER);
			root.setSpacing(30);
			
			setupPane();
			setupEvents();
			
			scene = new Scene(root, mainPaneWidth, mainPaneHeight);
			scene.getStylesheets().add("css/settings.css");
		}
		
		//set up the elements within the scene
		private void setupPane() {
			makeElements();

			//make and format the labels
			Label height = new Label(bundle.getString("numRows").toUpperCase()); //$NON-NLS-1$
			Label width = new Label(bundle.getString("numCols").toUpperCase()); //$NON-NLS-1$
			Label difficultyEasyL = new Label(bundle.getString("easy").toUpperCase()); //$NON-NLS-1$
			Label difficultyMediumL = new Label(bundle.getString("medium").toUpperCase()); //$NON-NLS-1$
			Label difficultyHardL = new Label(bundle.getString("hard").toUpperCase()); //$NON-NLS-1$
			
			//add HBoxes for each line of settings to make it visually pleasing
			//(following the gestalt idea of grouping to plan the layout)
			HBox h1 = new HBox();
			HBox h2 = new HBox();
			HBox h3 = new HBox();
			HBox h4 = new HBox();
			
			//add the relevant elements to their respective HBox
			h1.getChildren().addAll(music, musicCB, sound, soundCB);
			h2.getChildren().addAll(gridSize, heightCB, height, widthCB, width);
			h3.getChildren().addAll(difficultyLevel, difficultyEasy, difficultyEasyL, difficultyMedium, difficultyMediumL, difficultyHard, difficultyHardL);
			h4.getChildren().addAll(language, languageCB);
			
			//add the HBoxes and the other UI elements to the root pane
			root.getChildren().addAll(titleLabel, h1, h2, h3, h4, back);
		}
		
		//helper method to construct UI elements
		private void makeElements() {
			//title label
			titleLabel = new Label(bundle.getString("SettingsLabel").toUpperCase()); //$NON-NLS-1$
			titleLabel.setId("title_settings"); //$NON-NLS-1$

			//labels for the settings
			music = new Label(bundle.getString("music").toUpperCase()); //$NON-NLS-1$
			sound = new Label(bundle.getString("sound").toUpperCase()); //$NON-NLS-1$
			gridSize = new Label(bundle.getString("size").toUpperCase()); //$NON-NLS-1$
			difficultyLevel = new Label(bundle.getString("difficulty").toUpperCase()); //$NON-NLS-1$
			language = new Label(bundle.getString("language").toUpperCase()); //$NON-NLS-1$
		
			//checkboxes for the sound and music
			musicCB = new CheckBox();
			soundCB = new CheckBox();
			
			//comboboxes for the grid size (height and width)
			heightCB = new ComboBox<Integer>();
			widthCB = new ComboBox<Integer>();
			List<Integer> range = new ArrayList<Integer>();
			for (int i=20; i<=35; i+=1) {
				range.add(new Integer(i));
			}
			List<Integer> range2 = new ArrayList<Integer>();
			for (int i=5; i<=60; i+=1) {
				range2.add(new Integer(i));
			}
			heightCB.getItems().addAll(range);
			widthCB.getItems().addAll(range2);
			
			//radiobuttons for the difficulty level
			difficultyEasy = new RadioButton();
			difficultyMedium = new RadioButton();
			difficultyHard = new RadioButton();
			difficultyTG = new ToggleGroup();
			difficultyEasy.setToggleGroup(difficultyTG);
			difficultyMedium.setToggleGroup(difficultyTG);
			difficultyHard.setToggleGroup(difficultyTG);
			
			//combobox for the language
			languageCB = new ComboBox<String>();
			List<String> langList = new ArrayList<String>();
			langList.add("English");
			langList.add("French");
			languageCB.getItems().addAll(langList);
			
			//default values for all the settings
			musicCB.setSelected(true);
			soundCB.setSelected(true);
			difficultyTG.selectToggle(difficultyEasy);
			heightCB.getSelectionModel().select(Integer.valueOf(30));
			widthCB.getSelectionModel().select(Integer.valueOf(18));
			
			//format the UI based on the language
			if (bundle.getLocale().getLanguage().equals("en")) {
				languageCB.getSelectionModel().select("English");
				sound.setPadding(new Insets(0, 0, 0, 334));
				gridSize.setPadding(new Insets(0, 70, 0, 0));
				difficultyLevel.setPadding(new Insets(0, 224, 0, 0));
				language.setPadding(new Insets(0, 466, 0, 0));
			}
			else if (bundle.getLocale().getLanguage().equals("fr")) {
				languageCB.getSelectionModel().select("French");
				sound.setPadding(new Insets(0, 0, 0, 270));
				gridSize.setPadding(new Insets(0, 38, 0, 0));
				difficultyLevel.setPadding(new Insets(0, 242, 0, 0));
				language.setPadding(new Insets(0, 556, 0, 0));
			}
			
			//back button to go back to the start view
			back = new Button(bundle.getString("back").toUpperCase()); //$NON-NLS-1$
		}

		//set up the action listener for the back button
		//no events associated with the settings because the relevant info is just passed to the controller to pass to the gameview
		private void setupEvents() {
			back.setOnAction(e -> {
				//passing the info to the controller
				if (difficultyTG.getSelectedToggle()==difficultyEasy) {
					controller.setDifficulty(bundle.getString("easy").toUpperCase()); //$NON-NLS-1$
				}
				else if (difficultyTG.getSelectedToggle()==difficultyMedium) {
					controller.setDifficulty(bundle.getString("medium").toUpperCase()); //$NON-NLS-1$
				}
				else if (difficultyTG.getSelectedToggle()==difficultyHard) {
					controller.setDifficulty(bundle.getString("hard").toUpperCase()); //$NON-NLS-1$
				}
				controller.setMusic(musicCB.isSelected());
				controller.setSound(soundCB.isSelected());
				controller.setGridSize(heightCB.getValue()*20, widthCB.getValue()*20);
				
				//go back to the previous scene
				controller.showStartScene();
			});
			
			//change the language of the entire application through the controller if the user changes the language
			languageCB.valueProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue.equalsIgnoreCase("english")) {
						controller.changeLanguage("en");
					}
					else if (newValue.equalsIgnoreCase("french")) {
						controller.changeLanguage("fr");
					}
				}
				
			});
		}
		
		//getters 
		public Scene getScene() {
			return scene;
		}

		public boolean getMusic() {
			return musicCB.isSelected();
		}

		public boolean getSound() {
			return soundCB.isSelected();
		}
		
		public String getLanguage() {
			return languageCB.getSelectionModel().getSelectedItem();
		}
		
		public String getDifficulty() {
			if (difficultyTG.getSelectedToggle()==difficultyEasy) {
				return "easy";
			}
			else if (difficultyTG.getSelectedToggle()==difficultyMedium) {
				return "medium";
			}
			else {
				return "hard";
			}
		}

		//persistence of preferences
		public void setPrefs(boolean musicOn, boolean soundOn, String difficulty, int height, int width,
				String languageSel) {
			musicCB.setSelected(musicOn);
			soundCB.setSelected(soundOn);
			
			if (difficulty.equalsIgnoreCase("easy")) {
				difficultyTG.selectToggle(difficultyEasy);
			}
			else if (difficulty.equalsIgnoreCase("medium")) {
				difficultyTG.selectToggle(difficultyMedium);
			}
			else {
				difficultyTG.selectToggle(difficultyHard);
			}
			
			heightCB.getSelectionModel().select(Integer.valueOf(height/20));
			widthCB.getSelectionModel().select(Integer.valueOf(width/20));
			if (languageSel.equalsIgnoreCase("en")) {
				languageCB.getSelectionModel().select(0);
			}
			else {
				languageCB.getSelectionModel().select(1);
			}
		}
}

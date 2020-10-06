package DoodleJump;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Pane Organizer to organize the initial Menu Pane.
 */
public class MenuOrganizer {

	private Stage _stage;
	private BorderPane _rootPane;

	/**
	 * Constructor for the Menu Pane Organizer.
	 */
	public MenuOrganizer(Stage stage) {
		_stage = stage;

		// Creates Root Pane
		_rootPane = new BorderPane();

		// Sets Background to Black
		BackgroundFill background_fill = new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY);

		_rootPane.setBackground(new Background(background_fill));
		
		Text topText = new Text("DoodleJump:Halloween");
		topText.setFont(Font.loadFont("file:./DoodleJump/ScreamReal.ttf", 40));
		topText.setWrappingWidth(Constants.SCENE_WIDTH);
		topText.setTextAlignment(TextAlignment.CENTER);;
		topText.setFill(Color.web("eb6123"));
		VBox top = new VBox(topText);
		top.setAlignment(Pos.BASELINE_CENTER);
		top.setTranslateY(50);
		_rootPane.setTop(top);
		this.createMenus();
	}

	/**
	 * Accessor Method to return the rootpane
	 */
	public Pane getRootPane() {
		return _rootPane;
	}

	/**
	 * Creates the menu
	 */
	private void createMenus() {

		// Creates the Single Player Button. On click it calls the playSinglePlayerGame
		// method.
		Button singleplayer = new Button("Single Player");

		singleplayer.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 20));

		singleplayer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MenuOrganizer.this.playSinglePlayerGame();

			}
		});

		// Creates the Multi Player Button. On click it calls the playMultiPlayerGame
		// method.

		Button multiplayer = new Button("MultiPlayer");

		multiplayer.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 20));

		multiplayer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					MenuOrganizer.this.playMultiPlayerGame();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		// Creates the Quit Button. On click it calls System.exit(0);

		Button quit = new Button("Quit");

		quit.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 20));

		quit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});

		// Adds the buttons to a vbox
		VBox menu = new VBox(singleplayer, multiplayer, quit);

		menu.setAlignment(Pos.CENTER);

		menu.setSpacing(10);

		// Adds the buttons vbox to the rootpane
		_rootPane.setCenter(menu);

	}

	/**
	 * Called when user clicks on Single Player Button. Creates the Game Organizer
	 * and sets the Stage's scene to the organizers root.
	 */
	private void playSinglePlayerGame() {
		SinglePlayerGameOrganizer organizer = new SinglePlayerGameOrganizer(_stage);
		Scene gameScene = new Scene(organizer.getRoot(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
		gameScene.getStylesheets().add("file:./DoodleJump/styles.css");
		_stage.setScene(gameScene);
	}

	/**
	 * Called when user clicks on Multi Player Button. Creates A MultiPlayer Menu
	 * Organizer and sets the Stage's scene to the organizers root.
	 */
	private void playMultiPlayerGame() throws IOException {
		MultiPlayerOptions organizer = new MultiPlayerOptions(_stage);
		Scene gameScene = new Scene(organizer.getRoot(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
		gameScene.getStylesheets().add("file:./DoodleJump/styles.css");
		_stage.setScene(gameScene);
	}

}
package DoodleJump;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Abstract GameOrganizer class to organize the elements on the screen for a
 * game.
 *
 */
public abstract class GameOrganizer {

	protected Stage _stage;
	protected Game _game;
	protected BorderPane _rootPane;
	protected HBox _centerBox;
	protected Pane _gamePane;

	/**
	 * Constructor for the GameOrganizer class. Stores the stage and creates a root
	 * pane and center box.
	 */
	public GameOrganizer(Stage stage) {
		_stage = stage;
		// Create the root pane and centerbox
		_rootPane = new BorderPane();
		_centerBox = new HBox();
		// adds center box to the center of the rootpane
		_centerBox.setAlignment(Pos.CENTER);
		_rootPane.setCenter(_centerBox);
		// Sets up the Buttons pane on the left
		this.setupLeftPane();
		// Sets the background of the rootpane to Black
		BackgroundFill background_fill = new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY);
		_rootPane.setBackground(new Background(background_fill));
		// Makes sure focus of the keyhandlers is on the game
		_rootPane.setFocusTraversable(false);
	}

	/**
	 * Abstract method to setup labels on the right side of the gamepane
	 */
	protected abstract void setupRightPane();

	/**
	 * Sets up the buttons on the left side of the game pane
	 */
	private void setupLeftPane() {
		BackgroundFill background_fill = new BackgroundFill(Color.web("eb6123"), CornerRadii.EMPTY, Insets.EMPTY);

		// Creates pause button. On click it pauses the game.
		Button pause = new Button("Pause");

		pause.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				_game.pause(pause);
			}
		});

		pause.setFocusTraversable(false);

		pause.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 15));

		pause.setBackground(new Background(background_fill));

		pause.setTextFill(Color.BLACK);

		// Creates back to home button. On click it returns the user to the home menu
		Button backToHome = new Button("Back To Home");

		backToHome.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				MenuOrganizer organizer = new MenuOrganizer(_stage);
				Scene menu = new Scene(organizer.getRootPane(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
				menu.getStylesheets().add("file:./DoodleJump/styles.css");
				_stage.setScene(menu);
			}
		});

		backToHome.setFocusTraversable(false);

		backToHome.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 15));

		backToHome.setBackground(new Background(background_fill));

		backToHome.setTextFill(Color.BLACK);

		// Creates quit button. On click it calls System.exit(0);
		Button quit = new Button("Quit");

		quit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.exit(0);
			}
		});

		quit.setFocusTraversable(false);

		quit.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 15));

		quit.setBackground(new Background(background_fill));

		quit.setTextFill(Color.BLACK);

		// Creates a VBox with the buttons
		VBox buttons = new VBox(pause, backToHome, quit);
		buttons.setAlignment(Pos.CENTER);
		buttons.setFocusTraversable(false);
		buttons.setSpacing(10);
		buttons.setPadding(new Insets(10, 10, 10, 10));

		// Adds the buttons to the center box
		_centerBox.getChildren().add(buttons);
	}

	/**
	 * Abstract method to update the labels pane on the right of the game pane.
	 */
	protected abstract void updateRightPane();

	/**
	 * Abstract method to restart a game
	 */
	public abstract void restartGame();

	/**
	 * Accessor method to get the root pane.
	 */
	public BorderPane getRoot() {
		return _rootPane;
	}

}
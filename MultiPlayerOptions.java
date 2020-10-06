package DoodleJump;

import java.io.IOException;
import java.util.ArrayList;

import com.sun.net.httpserver.HttpServer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * MultiPlayerOptions coordinates between the server and the Multiplayer game
 * before the game is started.
 */
public class MultiPlayerOptions {
	private Stage _stage;
	private BorderPane _root;
	private TextArea _joined;
	private MultiPlayerGameOrganizer _gameOrganizer;
	private ArrayList<String> _playerNameList;
	private int _numPlayers;
	private VBox _center;
	private HttpServer _server;

	/**
	 * Constructor for MultiPlayerOptions class. Stores the stage and displays
	 * screen elements. Initializes the server.
	 */
	public MultiPlayerOptions(Stage stage) throws IOException {
		_stage = stage;
		// Initializes an arraylist of strings to store the user names
		_playerNameList = new ArrayList<String>();
		_numPlayers = 0;

		_root = new BorderPane();

		_center = new VBox();

		// Adds the Center VBOX to the center of the rootpabe
		_root.setCenter(_center);

		// Set background to BLACK
		BackgroundFill background_fill = new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY);
		_root.setBackground(new Background(background_fill));

		// Initializes the MultiplayerGameOrganizer
		_gameOrganizer = new MultiPlayerGameOrganizer(stage, this);

		// INITIALIZES THE HTTPSERVER
		_server = new Server(_gameOrganizer.getGame(), this).getServer();

		// Sets Up the Buttons
		this.setupButtons();
	}

	/**
	 * Returns the Root Pane
	 */
	public BorderPane getRoot() {
		return _root;
	}

	/**
	 * Sets up the buttons
	 */
	private void setupButtons() {

		// Creates startgame button. On Click it sets the stages scene to the game
		// scene.
		Button startgame = new Button("Start Game!");
		startgame.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (_playerNameList.size() > 0) {
					_gameOrganizer.getGame().addAllPlayers(_playerNameList);
					Scene game = new Scene(_gameOrganizer.getRoot(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
					game.getStylesheets().add("file:./DoodleJump/styles.css");
					_stage.setScene(game);
				} else {
					System.out.println("Error");
				}

			}
		});

		startgame.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 20));

		// Creates backtohome button. On Click it sets the stages scene to the
		// menuorganizers root pane.
		Button backtoHome = new Button("Back to Home");
		backtoHome.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_server.stop(0);
				MenuOrganizer organizer = new MenuOrganizer(_stage);
				Scene menu = new Scene(organizer.getRootPane(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
				menu.getStylesheets().add("file:./DoodleJump/styles.css");
				_stage.setScene(menu);
			}
		});

		backtoHome.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 20));

		// Adds the buttons to the bottom of the center box
		HBox bottom = new HBox(backtoHome, startgame);
		bottom.setSpacing(10);
		bottom.setAlignment(Pos.TOP_CENTER);
		_center.getChildren().add(bottom);

	}

	/**
	 * Called from the Server Class. When the server has been initialized, Show
	 * instructions for how to join the game on the screen.
	 */
	public void setupCenter(String url) {
		
		Label disclaimer = new Label("WARNING: MULTIPLAYER HTTPSERVER DOES NOT WORK ON DEPARTMENT MACHINES");

		disclaimer.setFont(Font.font(23));

		disclaimer.setTextFill(Color.web("white"));

		disclaimer.setAlignment(Pos.CENTER);

		disclaimer.setWrapText(true);

		// Create all the instructions to join the game
		Label joinInstructions1 = new Label("1. Make sure you are on the same Wi-Fi network as this computer. ");

		joinInstructions1.setFont(Font.loadFont("file:./DoodleJump/Metamorphous-Regular.ttf", 17));

		joinInstructions1.setTextFill(Color.web("eb6123"));

		joinInstructions1.setAlignment(Pos.CENTER);

		joinInstructions1.setWrapText(true);

		Label joinInstructions2 = new Label("2. Open your web browser and navigate to: ");

		joinInstructions2.setFont(Font.loadFont("file:./DoodleJump/Metamorphous-Regular.ttf", 17));

		joinInstructions2.setTextFill(Color.web("eb6123"));

		joinInstructions2.setAlignment(Pos.CENTER);

		Label joinInstructions3 = new Label("" + url);

		joinInstructions3.setFont(Font.loadFont("file:./DoodleJump/Metamorphous-Regular.ttf", 40));

		joinInstructions3.setTextFill(Color.web("eb6123"));

		joinInstructions3.setAlignment(Pos.CENTER);

		Label joinInstructions4 = new Label("3. Enter your name and click \"Join\".");

		joinInstructions4.setFont(Font.loadFont("file:./DoodleJump/Metamorphous-Regular.ttf", 17));

		joinInstructions4.setTextFill(Color.web("eb6123"));

		joinInstructions4.setAlignment(Pos.CENTER);

		Label joinInstructions5 = new Label("4.Control your player using the buttons on the browser.");

		joinInstructions5.setFont(Font.loadFont("file:./DoodleJump/Metamorphous-Regular.ttf", 17));

		joinInstructions5.setTextFill(Color.web("eb6123"));

		joinInstructions5.setAlignment(Pos.CENTER);

		// Display the users who have joined in a Scrolling JavaFX TextArea

		Label joinedhead = new Label("Players In This Game:");

		joinedhead.setFont(Font.loadFont("file:./DoodleJump/Metamorphous-Regular.ttf", 17));

		joinedhead.setTextFill(Color.web("eb6123"));

		joinedhead.setAlignment(Pos.CENTER);

		_joined = new TextArea();

		_joined.setFont(Font.loadFont("file:./DoodleJump/Metamorphous-Regular.ttf", 15));

		_joined.setEditable(false);

		_joined.getStyleClass().add("textarea");

		// Add everything to the screen and align it correctly
		VBox labels = new VBox(disclaimer,joinInstructions1, joinInstructions2, joinInstructions3, joinInstructions4,
				joinInstructions5, joinedhead, _joined);

		labels.setAlignment(Pos.CENTER);
		labels.setSpacing(10);
		_center.getChildren().add(labels);

	}

	/**
	 * Get the ID that is to be returned to a new user trying to join the game;
	 */
	public String getNewId() {
		return Integer.valueOf(_numPlayers).toString();

	}

	/**
	 * get the HTTPServer object
	 */
	public HttpServer getServer() {
		return _server;
	}

	/**
	 * Called from the Server. Adds a players name to the ArrayList of names. Then
	 * updates the textarea to show the new player
	 */
	public void addPlayer(String name) {
		_numPlayers += 1;
		_playerNameList.add(name);
		_joined.appendText("\n " + _numPlayers + ". " + name);
	}

}

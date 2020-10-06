package DoodleJump;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * A subclass of GameOrganizer. PaneOrganizer class for multiplayer games.
 *
 */
public class MultiPlayerGameOrganizer extends GameOrganizer {

	private Label _topthreetext;
	private Label _bottomheighttext;
	private VBox _rightPane;
	private MultiPlayerOptions _options;

	/**
	 * Constructor for MultiPlayerGameOrganizer. Stores the stage and the
	 * Multiplayer options; Throws an IOException because the game object is passed
	 * into the server.
	 */
	public MultiPlayerGameOrganizer(Stage stage, MultiPlayerOptions options) throws IOException {
		super(stage);
		_options = options;
		_game = new MultiPlayerGame(this);
		_centerBox.getChildren().add(_game.getGamePane());
		this.setupRightPane();
		_centerBox.getChildren().add(_rightPane);
	}

	/**
	 * Returns the game object, cast to type MultiPlayerGame
	 */
	public MultiPlayerGame getGame() {
		return (MultiPlayerGame) _game;
	}

	/**
	 * Returns the MultiPlayerOptions object associated with this game
	 */
	public MultiPlayerOptions getOptions() {
		return _options;
	}

	/**
	 * Sets up the right pane, which contain labels about the game
	 */
	@Override
	protected void setupRightPane() {
		// Creates a top 3 scoreboard on the right side.
		Label topthreeLbl = new Label("Top 3:");

		topthreeLbl.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 20));

		topthreeLbl.setTextFill(Color.WHITE);

		_topthreetext = new Label("");

		_topthreetext.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 15));

		_topthreetext.setTextFill(Color.web("eb6123"));

		_topthreetext.setWrapText(true);

		// Creates a label to show the height of the bottom of the screen.
		Label bottomHeightLbl = new Label("Bottom Height:");

		bottomHeightLbl.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 20));

		bottomHeightLbl.setTextFill(Color.WHITE);

		_bottomheighttext = new Label("0");

		_bottomheighttext.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 15));

		_bottomheighttext.setTextFill(Color.web("eb6123"));

		// Adds all the labels to a VBOX
		_rightPane = new VBox(topthreeLbl, _topthreetext, bottomHeightLbl, _bottomheighttext);

		_rightPane.setAlignment(Pos.CENTER);
		_rightPane.setPadding(new Insets(10, 10, 10, 10));

	}

	/**
	 * Called whenever the timeline updates in MultiPlayer Game.
	 */
	@Override
	protected void updateRightPane() {

		// An ArrayList is created from the games playerList hashmap and is then sorted
		// by score, so that the 3 highest players can be displayed in the _topthreetext
		// label
		List<Player> list = new ArrayList<Player>(((MultiPlayerGame) (_game)).getPlayers().values());
		Collections.sort(list);
		String text1, text2 = "", text3 = "";
		text1 = " 1. " + list.get(0).getName();
		if (list.size() > 1) {
			text2 = "\n 2. " + list.get(1).getName();
			if (list.size() > 2) {
				text3 = "\n 3. " + list.get(2).getName();
			}
		}

		_topthreetext.setText(text1 + text2 + text3);

		// The Bottom height text is set to the games bottom height
		_bottomheighttext.setText("" + _game.getBottomHeight());

	}

	/**
	 * Called form the play again button. Restarts the game.
	 **/
	@Override
	public void restartGame() {
		// Trys to create a MultiPlayerOptions object (which throws an IOException), and
		// sets the stage to the root of the organizer
		try {
			MultiPlayerOptions organizer = new MultiPlayerOptions(_stage);
			Scene gameScene = new Scene(organizer.getRoot(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
			gameScene.getStylesheets().add("file:./DoodleJump/styles.css");
			_stage.setScene(gameScene);
		} catch (IOException e) {
			// If cannot create MultiPlayerOptions object, set the stage to show the main
			// menu
			MenuOrganizer organizer = new MenuOrganizer(_stage);
			_stage.setTitle("DoodleJump");
			Scene main = new Scene(organizer.getRootPane(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
			main.getStylesheets().add("file:./DoodleJump/styles.css");
			_stage.setScene(main);
			e.printStackTrace();
		}
	}

}
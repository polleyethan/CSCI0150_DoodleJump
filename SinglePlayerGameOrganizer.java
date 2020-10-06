package DoodleJump;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * GameOrganizer for single player game.
 */
public class SinglePlayerGameOrganizer extends GameOrganizer {

	private Label _heighttext;
	private Label _timeText;
	private VBox _rightPane;

	/**
	 * Constructor for SinglePlayerGameOrganizer. Creates the game and sets up the
	 * right pane
	 */
	public SinglePlayerGameOrganizer(Stage stage) {
		super(stage);
		_game = new SinglePlayerGame(this);
		_centerBox.getChildren().add(_game.getGamePane());
		this.setupRightPane();
		_centerBox.getChildren().add(_rightPane);
	}

	/**
	 * Sets up a score and timer label
	 */
	@Override
	protected void setupRightPane() {
		Label heightLbl = new Label("Score:");

		heightLbl.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 20));

		heightLbl.setTextFill(Color.WHITE);

		_heighttext = new Label("");

		_heighttext.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 15));

		_heighttext.setTextFill(Color.web("eb6123"));

		_heighttext.setWrapText(true);

		Label timeLbl = new Label("Timer:");

		timeLbl.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 20));

		timeLbl.setTextFill(Color.WHITE);

		_timeText = new Label("0");

		_timeText.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 15));

		_timeText.setTextFill(Color.web("eb6123"));

		_rightPane = new VBox(heightLbl, _heighttext, timeLbl, _timeText);

		_rightPane.setAlignment(Pos.CENTER);
		_rightPane.setPadding(new Insets(10, 10, 10, 10));

	}

	/**
	 * Updates the score and timer labels
	 */
	@Override
	protected void updateRightPane() {

		_heighttext.setText("" + (int) ((SinglePlayerGame) _game).getPlayer().getY());

		_timeText.setText("" + (int) _game.getTimer());

	}

	/**
	 * Restarts the game. Creates a new singleplayergameorganizer and sets the
	 * stages pane to the root of the organizer.
	 */
	@Override
	public void restartGame() {
		SinglePlayerGameOrganizer organizer = new SinglePlayerGameOrganizer(_stage);
		Scene gameScene = new Scene(organizer.getRoot(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
		gameScene.getStylesheets().add("file:./DoodleJump/styles.css");
		_stage.setScene(gameScene);
	}

}
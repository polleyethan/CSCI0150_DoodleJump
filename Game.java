package DoodleJump;

import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * Abstract Game Class. Controls game elements.
 *
 */
public abstract class Game {

	protected Pane _gamePane;
	protected ArrayList<Platform> _platformList;
	protected double _bottomAltitude;
	protected Timeline _timeline;
	protected Boolean _isStarted;
	protected Boolean _isOver;
	protected Text _gameOverText;
	protected double _fallSeconds;
	protected double _gameOverTextLoc;
	protected VBox _gameOverBox;
	protected Boolean _isPaused;
	protected GameOrganizer _organizer;
	protected double _timer;
	protected Text _startGameText;

	/**
	 * Abstract Constructor for the Game Class. Stores the GameOrganizer.
	 */
	public Game(GameOrganizer organizer) {

		_organizer = organizer;

		// Create the elements that make up the game.
		this.createGame();

		_gamePane.requestFocus();

		// Set the Timer to 0.
		_timer = 0;
	}

	/**
	 * Abstract method to initialize the players in the game.
	 */
	protected abstract void initializePlayers();

	/**
	 * Method that creates the GamePane.
	 */
	private void createGamePane() {
		// Create the Pane
		_gamePane = new Pane();

		// Set the Background as the backdrop.jpg image.
		_gamePane.setBackground(new Background(new BackgroundImage(new Image("file:./DoodleJump/backdrop.jpg"),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true))));

		// Ensure all of the heights and widths are constant
		_gamePane.setPrefHeight(Constants.GAME_PANE_HEIGHT);
		_gamePane.setMinHeight(Constants.GAME_PANE_HEIGHT);
		_gamePane.setMaxHeight(Constants.GAME_PANE_HEIGHT);
		_gamePane.setPrefWidth(Constants.GAME_PANE_WIDTH);
		_gamePane.setMinWidth(Constants.GAME_PANE_WIDTH);
		_gamePane.setMaxWidth(Constants.GAME_PANE_WIDTH);

		// Create Lines on the borders of the Games Pane
		Line top = new Line(0, 0, Constants.GAME_PANE_WIDTH, 0);
		top.setFill(Color.BLACK);
		Line left = new Line(0, 0, 0, Constants.GAME_PANE_HEIGHT);
		left.setFill(Color.BLACK);
		Line right = new Line(Constants.GAME_PANE_WIDTH, 0, Constants.GAME_PANE_WIDTH, Constants.GAME_PANE_HEIGHT);
		right.setFill(Color.BLACK);
		Line bottom = new Line(0, Constants.GAME_PANE_HEIGHT, Constants.GAME_PANE_WIDTH, Constants.GAME_PANE_HEIGHT);
		bottom.setFill(Color.BLACK);
		_gamePane.getChildren().addAll(top, left, right, bottom);
	
		
	}

	/**
	 * Initialize the platforms on the screen.
	 */
	private void initializePlatforms() {
		_platformList = new ArrayList<Platform>();
		_platformList.add(new RegularPlatform(Constants.GAME_PANE_WIDTH / 2, 10, Constants.GAME_PANE_WIDTH / 3, this));
		for (int x = 0; x < (int) Constants.GAME_PANE_HEIGHT / 70; x++) {
			_platformList.add(this.generateRandomPlatform());
		}
	}

	/**
	 * Called when the game is started. Initially bounces all of the players to
	 * start the game.
	 */
	protected abstract void initialBounce();

	/**
	 * Creates the game
	 */
	private void createGame() {
		// Sets the statuses of the game
		_isPaused = false;
		_isStarted = false;
		_isOver = false;

		// Create the pane
		this.createGamePane();

		// Create the players
		this.initializePlayers();

		// Create the platforms
		this.initializePlatforms();
		
		//Create the start game text
		_startGameText = new Text("Press SPACE to begin");
		
		_startGameText.setFont(Font.loadFont("file:./DoodleJump/ScreamReal.ttf", 25));
		_startGameText.setFill(Color.web("white"));
		_startGameText.setY(Constants.GAME_PANE_HEIGHT/4);
		_startGameText.setWrappingWidth(Constants.GAME_PANE_WIDTH);
		_startGameText.setTextAlignment(TextAlignment.CENTER);
		
		_gamePane.getChildren().add(_startGameText);
	}

	/**
	 * Starts the game. Called when user presses SPACE.
	 */
	public void startGame() {
		_isStarted = true;

		_gamePane.getChildren().remove(_startGameText);
		// Creates and starts the timeline
		this.createTimeline();

		// bounces all of the players to start the game
		this.initialBounce();
	}

	/**
	 * Creates the Timeline and keyframes and starts the timeline
	 */
	private void createTimeline() {

		KeyFrame kf = new KeyFrame(Duration.seconds(Constants.TIMELINE_DELAY), new TimeHandler());
		_timeline = new Timeline(kf);
		_timeline.setCycleCount(Animation.INDEFINITE);
		_timeline.play();
	}

	/**
	 * Updates all aspects of the Game. Called whenever the timeline updates.
	 */
	protected void update() {
		// If the Game is not over:
		if (!_isOver) {
			// Update the timer
			_timer += Constants.TIMELINE_DELAY;
			// Update the organizers' labels
			_organizer.updateRightPane();
			// Update the players
			this.updatePlayers();
			// Update the BottomAltitude
			this.updateBackground();
			// Check if any players collided with a platform
			this.checkCollisions();
			// Update the Platforms
			this.updatePlatforms();

		} else {
			// If the game is over, only update according to the gameoverUpdate method
			this.gameOverUpdate();
		}
	}

	/**
	 * Abstract method to update players.
	 */
	protected abstract void updatePlayers();

	/**
	 * Abstract method to update the bottom altitude.
	 */
	protected abstract void updateBackground();

	/**
	 * Abstract method to check for collisions.
	 */
	protected abstract void checkCollisions();

	/**
	 * Updates platforms on the screen.
	 */
	protected void updatePlatforms() {
		for (int i = 0; i < _platformList.size(); i++) {
			// Update each platform
			_platformList.get(i).update();

			if (_platformList.get(i).getY() < _bottomAltitude) {
				// If the platform is off the screen, remove it both Logically and Graphically.
				// Then generate a new platform
				_platformList.get(i).removeFromPane();
				_platformList.remove(i);
				_platformList.add(this.generateRandomPlatform());
			} else if (_platformList.get(i).getY() > _bottomAltitude + Constants.GAME_PANE_HEIGHT) {
				_platformList.get(i).removeFromPane();
			}

		}
	}

	/**
	 * Pause the game.
	 */
	public void pause() {
		_timeline.pause();
	}

	/**
	 * Moves the given player to the right.
	 */
	public void movePlayerRight(Player player) {
		System.out.println("right");
		player.moveRight();
	}

	/**
	 * moves the given player to the left.s
	 */
	public void movePlayerLeft(Player player) {
		player.moveLeft();
	}

	/**
	 * Stops moving the given player
	 */
	public void stopPlayerXMovement(Player player) {
		player.stopMoving();
	}

	/**
	 * Abstract method to initialize the games ending.
	 */
	protected abstract void gameOverInitial();

	/**
	 * Abstract method to be called when the game is over.
	 */
	protected abstract void gameOverUpdate();

	/**
	 * Generates a new Random platform.
	 */
	private Platform generateRandomPlatform() {
		// PlatType is a random value out of 100. This will determine what type of
		// platform it is
		int plattype = (int) (100 * Math.random());

		// Randomly determine the widths and X and Y values, according to where they
		// last platform in the list is located.
		double width = Game.randomNum(Constants.GAME_PANE_WIDTH / 10, Constants.GAME_PANE_WIDTH / 5);
		double x = Game.randomNum(width / 2, Constants.GAME_PANE_WIDTH - width / 2);
		double y = Game.randomNum(_platformList.get(_platformList.size() - 1).getY() + 40,
				_platformList.get(_platformList.size() - 1).getY() + 80);

		// Determine the Platforms Type, depending on the probabilities set in the
		// Constants class.
		if (plattype > 100 - (Constants.TRAMPOLINE_PLAT_PROB * 100)) {
			return new TrampolinePlatform(x, y, width, this);
		} else if (plattype < 100 - (Constants.TRAMPOLINE_PLAT_PROB * 100)
				&& plattype > 100 - ((Constants.TRAMPOLINE_PLAT_PROB + Constants.BREAK_PLAT_PROB) * 100)) {
			return new BreakPlatform(x, y, width, this);
		} else {
			return new RegularPlatform(x, y, width, this);
		}
	}

	/**
	 * Static helper function to determine a random value, given a max and min.
	 */
	private static double randomNum(double min, double max) {
		return ((Math.random() * ((max - min) + 1)) + min);
	}

	/**
	 * takes a Y value and translates it to where it should appear on the screen.
	 */
	public double getTranslateY(double y) {
		return (Constants.GAME_PANE_HEIGHT - (y - _bottomAltitude));
	}

	/**
	 * Accessor that returns the game pane.
	 */
	public Pane getGamePane() {
		return _gamePane;
	}

	/**
	 * Accessor that returns whether the game has started.
	 */
	public Boolean isStarted() {
		return _isStarted;
	}

	/**
	 * Accessor that returns whether the game is over
	 */
	public Boolean isOver() {
		return _isOver;
	}

	/**
	 * Accessor that returns the games timer.
	 */
	public double getTimer() {
		return _timer;
	}

	/**
	 * Accessor that returns the height of the bottom of the screen, in relation to
	 * where the game started.
	 */
	public double getBottomHeight() {
		return _bottomAltitude;
	}

	/**
	 * Called whenever the pause button is pressed. Updates the text on the pause
	 * button depending on whether the game is paused or not.
	 */
	public void pause(Button pauseButton) {
		if (!_isOver) {
			if (_isPaused) {
				_isPaused = false;
				_timeline.play();
				pauseButton.setText("Pause");
			} else {
				_isPaused = true;
				_timeline.pause();
				pauseButton.setText("Resume");
			}
		}
	}

	/**
	 * TimeHandler to handle updates to the Timeline. Calls the Update method
	 * whenever the timeline updates.
	 */
	private class TimeHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			update();
		}
	}

}
package DoodleJump;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Subclass of abstract class Game. Used when playing with only one player
 *
 */
public class SinglePlayerGame extends Game {

	private Player _player;

	/**
	 * Constructor for SinglePlayerGame class. Adds the KeyHandler and calls the
	 * super constructor.
	 */
	public SinglePlayerGame(SinglePlayerGameOrganizer organizer) {
		super(organizer);
		_gamePane.requestFocus();
		_gamePane.setFocusTraversable(true);
		_gamePane.addEventHandler(KeyEvent.ANY, new SinglePlayerKeyHandler());
	}

	/**
	 * Initializes the player
	 */
	protected void initializePlayers() {
		_player = new Player(this, "Player");
	}

	/**
	 * Checks for collisions between the player and any of the platforms on the
	 * platformList
	 */
	protected void checkCollisions() {
		if (0 > _player.getYSpeed()) {
			for (int i = 0; i < _platformList.size(); i++) {
				if (_player.didCollide(_platformList.get(i))) {
					_platformList.get(i).onBounce(_player);
				}
			}
		}
	}

	/**
	 *
	 */
	protected void updateBackground() {
		if (this.getTranslateY(_player.getY()) < Constants.GAME_PANE_HEIGHT / 2 && _player.getYSpeed() > 0) {
			_bottomAltitude += _player.getYSpeed();
		}
	}

	/**
	 * Accessor method for the player
	 */
	public Player getPlayer() {
		return _player;
	}

	/**
	 * Updates the player. If player is above the bottom altitide, update it.
	 * Otherwise, end the game
	 */
	protected void updatePlayers() {
		if (_player.getY() > _bottomAltitude) {
			_player.update();
		} else if (_player.isAlive()) {
			_player.die();
			this.gameOverInitial();
		}
	}

	/**
	 * Move Player Right
	 */
	public void movePlayerRight(Player player) {
		super.movePlayerRight(_player);
	}

	/**
	 * Move Player Left
	 */
	public void movePlayerLeft(Player player) {
		super.movePlayerLeft(_player);
	}

	/**
	 * Stops players horizontal movement
	 */
	public void stopPlayerXMovement(Player player) {
		super.stopPlayerXMovement(_player);
	}

	/**
	 * Initially bounces the player into the air to start the game
	 */
	protected void initialBounce() {
		_player.bounce(20 / Constants.SPEED_FACTOR);

	}

	/**
	 * Called when the game initially ends. Finds the location for where the game
	 * over text will appear
	 */
	protected void gameOverInitial() {
		_fallSeconds = 0;
		_isOver = true;
		double gameOverLocTextTest = _bottomAltitude - 2100 + Constants.GAME_PANE_HEIGHT / 2;

		// If the game over location text is supposed to be below half of the game pane
		// height, set it to three quarters up on the game pane. Otherwise, set it to
		// where the above line says it should be
		if (gameOverLocTextTest < 3 * Constants.GAME_PANE_HEIGHT / 4) {
			_gameOverTextLoc = 3 * Constants.GAME_PANE_HEIGHT / 4;
		} else {
			_gameOverTextLoc = gameOverLocTextTest;
		}

		// Creates text objects to display when the game is over
		String scoreboard = "You Scored " + (int) _player.getScore() + " Points";

		_gameOverText = new Text(scoreboard);
		_gameOverText.setFont(Font.loadFont("file:./DoodleJump/Metamorphous-Regular.ttf", 30));

		_gameOverText.setFill(Color.web("eb6123"));
		_gameOverText.setTextAlignment(TextAlignment.CENTER);
		_gameOverText.setWrappingWidth(Constants.GAME_PANE_WIDTH);

		Text gameover = new Text("Game Over!");
		gameover.setFont(Font.loadFont("file:./DoodleJump/ScreamReal.ttf", 50));
		gameover.setFill(Color.web("eb6123"));
		gameover.setTextAlignment(TextAlignment.CENTER);
		gameover.setWrappingWidth(Constants.GAME_PANE_WIDTH);

		// Create a play again button
		Button playagain = new Button("Play Again");

		playagain.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 20));

		playagain.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_organizer.restartGame();
			}
		});

		_gameOverBox = new VBox(gameover, _gameOverText, playagain);
		_gameOverBox.setAlignment(Pos.CENTER);
		_gameOverBox.setSpacing(10);
		_gamePane.getChildren().add(_gameOverBox);
	}

	/**
	 * Called when the timeline is updating but the game is over
	 */
	protected void gameOverUpdate() {
		// if the player has been falling for less than 3 seconds and the bottom
		// altitude is still above 0, or if the game over text is not at the desired
		// height
		if (_fallSeconds < 3 && _bottomAltitude > 0
				|| this.getTranslateY(_gameOverTextLoc) > Constants.GAME_PANE_HEIGHT / 4) {
			// Continue to update the player
			_player.update();

			// Increase the amount of time the player has been falling
			_fallSeconds += Constants.TIMELINE_DELAY;
			_gameOverBox.setTranslateY(this.getTranslateY(_gameOverTextLoc));

			// If the players Y is above half of the screen, have the winner fall at a pace
			// equal to the screen. Otherwise, have the screen fall faster than the player,
			// in order to catch up.
			if (_player.getY() > _bottomAltitude + Constants.GAME_PANE_HEIGHT / 2) {
				_bottomAltitude += _player.getYSpeed();
			} else {
				_bottomAltitude += 1.3 * _player.getYSpeed();
			}

			// Continue to update all platforms
			this.updatePlatforms();

		} else {
			// If the player has not yet dropped out of frame, continue to have him fall.
			// Otherwise, end the timeline;
			if (_player.getY() > -1 * Constants.GAME_PANE_HEIGHT) {
				_player.update();
			} else {
				_timeline.stop();
			}
		}
	}

	/**
	 * KeyHandler to determine where and how to move the player.
	 *
	 */
	private class SinglePlayerKeyHandler implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent e) {
			KeyCode keyPressed = e.getCode();
			if (e.getEventType() == KeyEvent.KEY_PRESSED) {
				switch (keyPressed) {
				case LEFT:
					movePlayerLeft(null);
					break;
				case RIGHT:
					movePlayerRight(null);
					break;
				case P:
					pause();
					break;
				case SPACE: {
					if (!isStarted() && !isOver()) {
						startGame();
					}
					break;
				}
				default:
					break;
				}
			} else if (e.getEventType() == KeyEvent.KEY_RELEASED
					&& (keyPressed == KeyCode.LEFT || keyPressed == KeyCode.RIGHT)) {
				stopPlayerXMovement(null);
			}
			e.consume();
		}
	}

}
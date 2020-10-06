package DoodleJump;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
 * Subclass of abstract class Game. Used when multiple players are
 * participating.
 *
 */
public class MultiPlayerGame extends Game {

	HashMap<Integer, Player> _playerList;
	private int _playersLeft;
	private Player _winner;

	/**
	 * Constructor for MultiPlayerGame class. Adds the KeyHandler and calls the
	 * super constructor.
	 */
	public MultiPlayerGame(MultiPlayerGameOrganizer organizer) throws IOException {
		super(organizer);
		_gamePane.requestFocus();
		_gamePane.setFocusTraversable(true);
		_gamePane.addEventHandler(KeyEvent.ANY, new MultiPlayerKeyHandler());

	}

	/**
	 * Initializes the HashMap of Players. Each Player corresponds to an integer.
	 */
	protected void initializePlayers() {
		_playerList = new HashMap<Integer, Player>();

	}

	/**
	 * Takes in an ArrayList of player names and adds them to the hashmap.
	 */
	public void addAllPlayers(ArrayList<String> nameList) {
		for (int i = 0; i < nameList.size(); i++) {
			_playerList.put(i, new Player(this, nameList.get(i)));
		}
		_playersLeft = _playerList.size();
	}

	/**
	 * Checks for collisions between any player on the playerList and any of the
	 * platforms on the platformList
	 */
	protected void checkCollisions() {
		for (Player p : _playerList.values()) {
			if (0 > p.getYSpeed()) {
				for (int j = 0; j < _platformList.size(); j++) {
					if (p.didCollide(_platformList.get(j))) {
						_platformList.get(j).onBounce(p);
					}
				}
			}
		}
	}

	/**
	 * Initially bounces each player into the air to start the game
	 */
	protected void initialBounce() {
		for (int x = 0; x < _playerList.size(); x++) {
			_playerList.get(x).bounce(20 / Constants.SPEED_FACTOR);
		}

	}

	/**
	 * Updates the bottom Altitude of the game
	 */
	protected void updateBackground() {
		for (Player p : _playerList.values()) {
			// If any players translated Y value is above half of the game Pane height and
			// the player is moving up, move the bottom altitude up at a speed equal to that
			// of the player.
			if (this.getTranslateY(p.getY()) < Constants.GAME_PANE_HEIGHT / 2 && p.getYSpeed() > 0) {
				_bottomAltitude += p.getYSpeed();
			}
		}

	}

	/**
	 * Update each player in the game
	 */
	protected void updatePlayers() {
		// For each player in the hashmap
		for (Player p : _playerList.values()) {
			// If the player is still supposedly alive:
			if (p.isAlive()) {
				// If the player is below bottom of the screen:
				if (p.getY() < _bottomAltitude) {
					// if the player is the last player, set the player as the winner, kill this
					// player, and end the game.
					if (_playersLeft == 1) {
						this.setWinner();
						p.die();
						this.gameOverInitial();
					}
					// if the player is not the last player, kill this player, and decrease the
					// number of players remaining
					else {
						p.die();
						_playersLeft -= 1;
						p.removeDoodle();
					}
				}
				// Otherwise, update the player
				else {
					p.update();
				}
			}

		}
	}

	/**
	 * Called when a user makes a get request on the server. Returns the player
	 * associated with the given hash key
	 */
	public Player getPlayerById(int id) {
		return _playerList.get(id);
	}

	/**
	 * KeyHandler to handle starting the game.
	 *
	 */
	private class MultiPlayerKeyHandler implements EventHandler<KeyEvent> {
		public void handle(KeyEvent e) {
			KeyCode keyPressed = e.getCode();
			if (e.getEventType() == KeyEvent.KEY_PRESSED && keyPressed == KeyCode.SPACE) {
				if (!isStarted() && !isOver()) {
					// If the game isnt already started or over, start the game on the SPACE key
					// pressed
					startGame();
				}
			}
			e.consume();
		}
	}

	/**
	 * Called when the game initially ends. Finds the location for where the game
	 * over text will appear
	 */
	protected void gameOverInitial() {
		// initializes some game over values
		_fallSeconds = 0;
		_isOver = true;



		// Sets the location for where the game over text will appear.
		double gameOverLocTextTest = _bottomAltitude - 2100 + Constants.GAME_PANE_HEIGHT / 2;

		// If the game over location text is supposed to be below half of the game pane
		// height, set it to three quarters up on the game pane. Otherwise, set it to
		// where the above line says it should be
		if (gameOverLocTextTest < 3 * Constants.GAME_PANE_HEIGHT / 4) {
			_gameOverTextLoc = 3 * Constants.GAME_PANE_HEIGHT / 4;
		} else {
			_gameOverTextLoc = gameOverLocTextTest;
		}

		// Order all of the players in the playerList by making a list of players out of
		// them and sorting the list of players by their scores in a descending order.
		List<Player> list = new ArrayList<Player>(_playerList.values());
		Collections.sort(list);

		// Create a string with a scoreboard of the top 3 players. If there is only 1
		// player, only use 1, and so on and so fourth until there are up to 3 players
		// on the scoreboard;
		String scoreboard = "1. " + list.get(0).getName() + "...................." + (int) list.get(0).getScore();
		if (list.size() > 1) {
			scoreboard += "\n2. " + list.get(1).getName() + "...................." + (int) list.get(1).getScore();
			if (list.size() > 2) {
				scoreboard += "\n3. " + list.get(2).getName() + "...................." + (int) list.get(2).getScore();
			}
		}

		// Create Text objects with the Scoreboard and game ending screen
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

		Text scoreHead = new Text("Leaderboard:");
		scoreHead.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 40));
		scoreHead.setFill(Color.web("eb6123"));
		scoreHead.setTextAlignment(TextAlignment.CENTER);
		scoreHead.setWrappingWidth(Constants.GAME_PANE_WIDTH);

		// Add a play again button to the box
		Button playagain = new Button("Play Again");

		playagain.setFont(Font.loadFont("file:./DoodleJump/Creepster-Regular.ttf", 20));

		playagain.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_organizer.restartGame();
			}
		});

		// Add all the game over text objects to a vbox, and add it to the game pane;
		_gameOverBox = new VBox(gameover, scoreHead, _gameOverText, playagain);
		_gameOverBox.setAlignment(Pos.CENTER);
		_gameOverBox.setSpacing(10);
		_gamePane.getChildren().add(_gameOverBox);
	}

	/**
	 * Called when the timeline is updating but the game is over
	 */
	protected void gameOverUpdate() {
		// if the player has been falling for less than 3 seconds and the bottom
		// altitude is still above 0, Or if the game over text is not at the desired
		// height
		if (_fallSeconds < 3 && _bottomAltitude > 0
				|| this.getTranslateY(_gameOverTextLoc) > Constants.GAME_PANE_HEIGHT / 4) {
			// Continue to update the player
			_winner.update();

			// Increase the amount of time the player has been falling
			_fallSeconds += Constants.TIMELINE_DELAY;
			_gameOverBox.setTranslateY(this.getTranslateY(_gameOverTextLoc));

			// If the winners Y is above half of the screen, have the winner fall at a pace
			// equal to the screen. Otherwise, have the screen fall faster than the winner,
			// in order to catch up.
			if (_winner.getY() > _bottomAltitude + Constants.GAME_PANE_HEIGHT / 2) {
				_bottomAltitude += _winner.getYSpeed();
			} else {
				_bottomAltitude += 1.3 * _winner.getYSpeed();
			}

			// Continue to update all platforms
			this.updatePlatforms();

		} else {
			// If the winner has not yet dropped out of frame, continue to have him fall.
			// Otherwise, end the timeline;
			if (_winner.getY() > -1 * Constants.GAME_PANE_HEIGHT) {
				_winner.update();
			} else {
				// Stops the game server and timeline
				((MultiPlayerGameOrganizer) _organizer).getOptions().getServer().stop(0);
				_timeline.stop();
			}
		}
	}

	/**
	 * Get the only player who is still alive and set them as the winner
	 */
	private void setWinner() {
		for (int i = 0; i < _playerList.size(); i++) {
			if (_playerList.get(i).isAlive()) {
				_winner = _playerList.get(i);
			}
		}
	}

	/**
	 * Accessor method to get A COPY OF the HashMap of Players
	 */
	public HashMap<Integer, Player> getPlayers() {
		return new HashMap<Integer, Player>(_playerList);
	}

}
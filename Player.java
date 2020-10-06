package DoodleJump;

/**
 * Represents a Player in the game. Implements comparable interface, allowing
 * the playerslist to be sorted according to who has the highest score in a
 * multiplayer game.
 *
 */
public class Player implements Comparable<Player> {

	private Doodle _doodle;
	private double _x;
	private double _y;
	private String _name;

	private double _xSpeed;
	private double _ySpeed;

	private double _yAcceleration;

	private double _width;
	private double _height;

	private Boolean _isAlive;

	private double _score;
	private Game _game;
	private Toy _toy;

	/**
	 * Constructor for the player object
	 */
	public Player(Game game, String name) {
		_name = name;
		_game = game;
		this.createPlayer();
	}

	/**
	 * Initializes all the main values associated with the player
	 */
	private void createPlayer() {
		_x = Constants.GAME_PANE_WIDTH / 2;
		_y = 20;
		_width = 25;
		_height = 40;
		_score = 0;
		_yAcceleration = Constants.GRAVITY_Y_ACCELL;
		_isAlive = true;

		// Creates the graphical representation of the player (AKA the "Doodle")
		_doodle = new Doodle(_x, _y, _width, _height, _game, _name);
	}

	/**
	 * Updates the player
	 */
	public void update() {

		// If the Player is alive:
		if (_isAlive) {
			// If he is currently using a toy, update the toys position, calculate the
			// players position according to the toys effect, and then update the doodle.
			if (_toy != null) {
				_toy.update(_x, _game.getTranslateY(_y - _height));
				_toy.effectPlayer(this);
				this.calculatePositionWithToy();
				_doodle.updatePosition(_x, _y);

			}
			// Otherwise, if there is no toy, apply normal laws of physics to calculate the
			// players position, and then update the doodle.
			else {
				this.calculatePosition();
				_doodle.updatePosition(_x, _y);
			}
		}
		// Otherwise if the player is not alive, calculate the "dead Position", i.e. the
		// position as he falls
		else {
			this.calculatePositionDead();
			_doodle.updatePosition(_x, _y);
		}
	}

	/**
	 * Calculate the players normal position according to the laws of physics
	 */
	private void calculatePosition() {
		_ySpeed += _yAcceleration;
		_y += _ySpeed;

		if (_x < 0) {
			_x = Constants.GAME_PANE_WIDTH;
		} else if (_x > Constants.GAME_PANE_WIDTH) {
			_x = 0;
		} else {
			_x += _xSpeed;
		}

		if (_ySpeed > 0) {
			_score = _y;
		}

	}

	/**
	 * When dead, the player falls at a constant speed.
	 */
	private void calculatePositionDead() {
		_ySpeed = -1.5;
		_y += _ySpeed;

	}

	/**
	 * Update the player according to the Toy's specified effect
	 */
	private void calculatePositionWithToy() {
		_y += _ySpeed;

		if (_x < 0) {
			_x = Constants.GAME_PANE_WIDTH;
		} else if (_x > Constants.GAME_PANE_WIDTH) {
			_x = 0;
		} else {
			_x += _xSpeed;
		}
		
		if (_ySpeed > 0) {
			_score = _y;
		}

	}

	/**
	 * Called when player dies. Sets the isAlive parameter ot false
	 */
	public void die() {
		_isAlive = false;
	}

	/**
	 * Called when a player bounces off a platform, sets a new Y Speed
	 */
	public void bounce(double newYSpeed) {
		_ySpeed = newYSpeed;
	}

	/**
	 * Adds a toy to this player
	 */
	public void addToy(Toy toy) {
		_toy = toy;
	}

	/**
	 * Removes a toy from this player
	 */
	public void removeToy() {
		_toy = null;
	}

	/**
	 * Set Player X speed to +5; move right smoothly until stopped
	 */
	public void moveRight() {
		_xSpeed = 5 / Constants.SPEED_FACTOR;
	}

	/**
	 * Set Player X speed to -5; move left smoothly until stopped
	 */
	public void moveLeft() {
		_xSpeed = -5 / Constants.SPEED_FACTOR;
	}

	/**
	 * Set Player X speed to 0; stop moving player in x direction
	 */
	public void stopMoving() {
		_xSpeed = 0;
	}

	/**
	 * Accessor to get the Players Y Position
	 */
	public double getY() {
		return _y;
	}

	/**
	 * Mutator to set the Players Y Position
	 */
	public void setY(double y) {
		_y = y;
	}

	/**
	 * Mutator to set the Players Y speed
	 */
	public void setYSpeed(double speed) {
		_ySpeed = speed;
	}

	/**
	 * Accessor to get the Players Y speed
	 */
	public double getYSpeed() {
		return _ySpeed;
	}

	/**
	 * Accessor to get the Players height
	 */
	public double getHeight() {
		return _height;
	}

	/**
	 * Mutator to see if the player is still alive
	 */
	public Boolean isAlive() {
		return _isAlive;
	}

	/**
	 * Checks if a rectangle representing the bottom half of the player has collided
	 * with the given platform
	 */
	public Boolean didCollide(Platform platform) {
		if (platform.getPlatformNode().intersects(_x - _width / 2, _game.getTranslateY(_y + _height / 3), _width,
				2 * _height / 3)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes the doodle from the screen
	 */
	public void removeDoodle() {
		_doodle.removeNodes();
	}

	/**
	 * Accessor to get the Players name
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Accessor to get the Players score
	 */
	public double getScore() {
		return _score;
	}

	/**
	 * Implemented because of the comparable interface. Compares the scores this
	 * player to that of another player and determines who is higher up on the
	 * leaderboard
	 */
	@Override
	public int compareTo(Player p) {
		return Double.compare(p.getScore(), this.getScore());
	}
}
package DoodleJump;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Abstract class representing a platform.
 *
 */
public abstract class Platform {
	protected double _x;
	protected double _y;
	protected double _width;
	protected Game _game;
	protected Node _platformNode;
	protected Boolean _onScreen;

	/**
	 * Super Constructor for the abstract platform class.
	 */
	public Platform(double x, double y, double width, Game game) {
		_x = x;
		_y = y;
		_width = width;
		_game = game;

		// If the platform is initialized on the screen, then draw it, Otherwise, wait
		// till it is on the screen and then it will be drawn
		if (_game.getTranslateY(_y) > 0) {
			this.draw(_game.getGamePane());
			_onScreen = true;
		} else {
			_onScreen = false;
		}
	}

	/**
	 * Abstract method that is called when a player bounces off this platform.
	 */
	public abstract void onBounce(Player playerToBounce);

	/**
	 * Abstract method that is called when this platform is to be drawn
	 */
	protected abstract void draw(Pane pane);

	/**
	 * Abstract method that is called whenever the timeline is updated; updates the
	 * position of the platform
	 */
	protected abstract void update();

	/**
	 * Removes all elements associated with the platform from the GamePane if the
	 * platform falls below the screen
	 */
	public abstract void removeFromPane();

	/**
	 * Accessor to get the Y value of the platform
	 */
	public double getY() {
		return _y;
	}

	/**
	 * Returns the platforms main node;
	 */
	public Node getPlatformNode() {
		return _platformNode;
	}

}
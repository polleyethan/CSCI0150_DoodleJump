package DoodleJump;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * Abstract class for a toy.
 *
 */
public abstract class Toy {

	protected Region _region;
	protected double _width;
	protected double _height;
	protected double _timeleft;
	protected Pane _gamePane;

	/**
	 * Constructor for a toy.
	 */
	public Toy(double x, double y, Pane gamepane) {
		_gamePane = gamepane;
		this.createToy(x, y);
	}

	/**
	 * Abstract method to create a toy at the given coordinates
	 */
	protected abstract void createToy(double x, double y);

	/**
	 * Abstract method to update the toy and draw it at the given coordinates
	 */
	public abstract void update(double x, double y);

	/**
	 * Abstract method for this toy to effect the given player
	 */
	public abstract void effectPlayer(Player playerToEffect);

	/**
	 * Removes all nodes from the screen.
	 */
	public void remove() {
		_gamePane.getChildren().remove(_region);
	}
}

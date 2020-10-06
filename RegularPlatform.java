package DoodleJump;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * A Subclass of class Platform. Is a regular platform. In single player mode,
 * it can have toys on it.
 *
 */
public class RegularPlatform extends Platform {

	private Line _platformLine;
	private Toy _toy;

	/**
	 * Constructor for RegularPlatform. Calls super on the abstract class Platform.
	 */
	public RegularPlatform(double x, double y, double width, Game game) {
		super(x, y, width, game);
		// If the game is a Single Player Game, set the toy variable for this platform
		if (game instanceof SinglePlayerGame) {
			this.setToy();
		}
	}

	/**
	 * Called when a player bounces off this platform
	 */
	public void onBounce(Player playerToBounce) {
		// If there is a toy attatched, add the toy to the player and delete it from
		// this platform
		if (_toy != null) {
			playerToBounce.addToy(_toy);
			_toy = null;
		}
		// Otherwise, bounce the player at the bounce speed set in the constants
		else {
			playerToBounce.bounce(Constants.REGULAR_PLATFORM_BOUNCE);
		}
	}

	/**
	 * Called when the Platform is on the screen and not yet drawn
	 */
	protected void draw(Pane pane) {
		_platformLine = new Line(_x - (_width / 2), _game.getTranslateY(_y), _x + (_width / 2),
				_game.getTranslateY(_y));
		_platformLine.setStroke(Color.rgb(255, 140, 0));
		_platformLine.setStrokeWidth(4);
		_platformNode = _platformLine;
		pane.getChildren().add(_platformNode);
	}

	/**
	 * Called whenever the TimeLine updates. Updates the platforms position on the
	 * screen.
	 */
	protected void update() {
		// If it is on the screen but has not yet been drawn, draw it.
		if (_game.getTranslateY(_y) > 0 && !_onScreen) {
			this.draw(_game.getGamePane());
			_onScreen = true;
		}
		// Otherwise, if it was already drawn, Update the Y values for the platform
		else if (_onScreen) {
			_platformLine.setStartY(_game.getTranslateY(_y));
			_platformLine.setEndY(_game.getTranslateY(_y));
			// If there is a toy attatched:
			if (_toy != null) {
				if (_toy instanceof HatToy) {
					// Update the HatToys positon
					_toy.update(_x, _game.getTranslateY(_y - 50));
				} else {
					// Update a different Toys position
					_toy.update(_x, _game.getTranslateY(_y));
				}
			}
		}
	}

	/**
	 * Using the Probabilities set in the Constants, Randomly generate toys on the
	 * platforms
	 */
	private void setToy() {
		int toytype = (int) (100 * Math.random());
		if (toytype > 100 - (Constants.HAT_TOY_PROB * 100)) {
			_toy = new HatToy(_x, _game.getTranslateY(_y - 50), _game.getGamePane());
		} else if (toytype < 100 - (Constants.HAT_TOY_PROB * 100)
				&& toytype > 100 - ((Constants.HAT_TOY_PROB + Constants.BROOM_TOY_PROB) * 100)) {
			_toy = new BroomToy(_x, _game.getTranslateY(_y), _game.getGamePane());
		} else {
			_toy = null;
		}
	}

	/**
	 * Remove all elements of the Platform from the GamePane, including a toy if it
	 * exists
	 */
	@Override
	public void removeFromPane() {
		_game.getGamePane().getChildren().removeAll(_platformLine);
		if (_toy != null) {
			_toy.remove();
		}

	}

}
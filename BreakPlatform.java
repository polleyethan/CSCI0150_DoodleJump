package DoodleJump;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;

/**
 * A Subclass of class Platform. When Jumped on the platform Breaks and falls
 * off the screen
 *
 */
public class BreakPlatform extends Platform {

	private Region _plankLeftRegion;
	private Region _plankRightRegion;
	private Line _platformLine;
	private Boolean _isBroken;
	private int _rotate;

	/**
	 * Constructor for BreakPlatform. Calls super on the abstract class Platform.
	 * Initializes IsBroken as false.
	 */
	public BreakPlatform(double x, double y, double width, Game game) {
		super(x, y, width, game);
		_isBroken = false;
	}

	/**
	 * Called when a player bounces off this platform.
	 */
	@Override
	public void onBounce(Player playerToBounce) {
		_isBroken = true;
		playerToBounce.bounce(0);

	}

	/**
	 * Called when the Platform is on the screen and not yet drawn
	 */
	@Override
	protected void draw(Pane pane) {
		// Create a line to represent the intersection point for the player
		_rotate = 180;
		_platformLine = new Line(_x - (_width / 2), _game.getTranslateY(_y), _x + (_width / 2),
				_game.getTranslateY(_y));
		_platformLine.setStroke(Color.rgb(179, 129, 66));
		_platformLine.setOpacity(0);
		_platformLine.setStrokeWidth(2);
		_platformNode = _platformLine;

		// Create an svg for the left part of the platform and initialize all its values
		String pathleft = "m 4004.4229,12422.925 -436.7268,296.795 451.1338,142.241 -494.9023,182.884 497.0365,157.296 -687.4234,274.437 c -2071,-27 -3477.87379,32.554 -3586.87379,10.554 -190,-39 -614,-175 -761.99991,-246 -123.0001,-58 -231,-128 -267,-172 -14,-18 -30,-38 -35,-44 -30,-37 -39,-206 -14,-280 33,-101 78,-114 447.99991,-122 l 280,-6 17,-58 c 24,-81 65,-177 120,-277 25,-47 54,-110 64,-140 10,-30 26,-71 35,-90 40,-78 135,-139 250.00001,-160 36.000014,-7 101.000112,-23 145.000112,-35 44,-13 105.999978,-26 136.999978,-30 50,-6 60,-12 87,-46 102,-126 307.00004,-140 463.00004,-33 85,59 137,183 123,293 -7,58 -38,150 -62,188 -8,12 -11,28 -7,34 4,7 3,9 -3,6 -6,-4 -18,4 -27,18 -9,14 -35,39 -58,55 -22,17 -36,30 -31,30 6,0 35,-21 64,-47 30,-27 48,-41 41,-32 -7,9 -14,39 -17,68 l -4,51 H 2795.4661";
		SVGPath plankleft = new SVGPath();
		plankleft.setContent(pathleft);

		_plankLeftRegion = new Region();
		_plankLeftRegion.setShape(plankleft);
		_plankLeftRegion.setMinSize(_width / 2, _width / 4);
		_plankLeftRegion.setMaxSize(_width / 2, _width / 4);
		_plankLeftRegion.setStyle("-fx-background-color: #D68E4C;");
		_plankLeftRegion.setTranslateX(_x);
		_plankLeftRegion.setTranslateY(_game.getTranslateY(_y));
		_plankLeftRegion.setRotate(_rotate);

		// Create an svg for the right part of the platform and initialize all its
		// values
		String pathright = "m -857.75508,12439.02 -227.04812,237.065 381.21892,190.025 -326.91482,194.83 291.29712,193.169 -606.64342,250.511 c 2071.00052,-27 4636.8458,20.607 4745.8458,-1.393 190.001,-39 614.001,-175 762.001,-246 123,-58 231,-128 267,-172 14,-18 30,-38 35,-44 30,-37 39,-206 14,-280 -33,-101 -78,-114 -448,-122 l -280,-6 -17,-58 c -24,-81 -65,-177 -120,-277 -25,-47 -54,-110 -64,-140 -10,-30 -26,-71 -35,-90 -40,-78 -135.001,-139 -250.001,-160 -36,-7 -101,-23 -145,-35 -44,-13 -106,-26 -137,-30 -50,-6 -60,-12 -87,-46 -102,-126 -307,-140 -463,-33 -85,59 -137,183 -123,293 7,58 38,150 62,188 8,12 11,28 7,34 -4,7 -3,9 3,6 6,-4 18,4 27,18 9,14 35,39 58,55 22,17 36,30 31,30 -6,0 -35,-21 -64,-47 -30,-27 -48,-41 -41,-32 7,9 14,39 17,68 l 4,51 H 351.20162";
		SVGPath plankright = new SVGPath();
		plankright.setContent(pathright);

		_plankRightRegion = new Region();
		_plankRightRegion.setShape(plankright);
		_plankRightRegion.setMinSize(_width / 2, _width / 4);
		_plankRightRegion.setMaxSize(_width / 2, _width / 4);
		_plankRightRegion.setStyle("-fx-background-color: #D68E4C;");
		_plankRightRegion.setTranslateX(_x - _width / 2);
		_plankRightRegion.setTranslateY(_game.getTranslateY(_y));
		_plankRightRegion.setRotate(_rotate);

		pane.getChildren().addAll(_platformLine, _plankLeftRegion, _plankRightRegion);

	}

	/**
	 * Called whenever the TimeLine updates. Updates the Platform elements'
	 * positions on the screen.
	 */
	@Override
	protected void update() {
		// If it is on the screen but has not yet been drawn, draw it.
		if (_game.getTranslateY(_y) > 0 && !_onScreen) {
			this.draw(_game.getGamePane());
			_onScreen = true;
		} else if (_onScreen) {
			// If it is broken, have its y value constantly decrease.
			if (_isBroken) {
				_y -= 5 / Constants.SPEED_FACTOR;
				// If it is not tilted at 90 degrees, change the rotate factor by 5 and change
				// the rotation of both svgs
				if (_rotate > 90) {
					_rotate -= 1;
					_plankRightRegion.setRotate(-1 * _rotate);
					_plankLeftRegion.setRotate(_rotate);
				}
			}
			// Update the X and Y values for the platform pieces
			_platformLine.setStartY(_game.getTranslateY(_y));
			_platformLine.setEndY(_game.getTranslateY(_y));
			_plankRightRegion.setTranslateX(_x - _width / 2);
			_plankRightRegion.setTranslateY(_game.getTranslateY(_y));
			_plankLeftRegion.setTranslateX(_x);
			_plankLeftRegion.setTranslateY(_game.getTranslateY(_y));
		}

	}

	/**
	 * If it goes out of sight, remove all components from the Pane
	 */
	@Override
	public void removeFromPane() {
		_game.getGamePane().getChildren().removeAll(_platformLine, _plankLeftRegion, _plankRightRegion);
	}
}
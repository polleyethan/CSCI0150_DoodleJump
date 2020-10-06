package DoodleJump;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.SVGPath;

/**
 * Subclass of Platform. Bounces a player higher than a RegularPlatform.
 *
 */
public class TrampolinePlatform extends Platform {

	private QuadCurve _platformCurve;
	private double _curveOffset;
	private double _change;
	private Region _cobwebRegion;

	/**
	 * Constructor for the TrampolinePlatform class. Initializes the curveOffest at
	 * 0 and
	 */
	public TrampolinePlatform(double x, double y, double width, Game game) {
		super(x, y, width, game);
		_curveOffset = 0;
		_change = 1;
	}

	/**
	 * Called when a player bounces on the platform. If the curve is flat, it makes
	 * it more curvey, until it bounces back up and launchers the user.
	 */
	public void onBounce(Player playerToBounce) {
		// If the curve offset is greater than or equal to 0:
		if (_curveOffset >= 0) {
			// set the players yspeed to 0 and their y to the curve offset. Then add one to
			// the curve offset
			playerToBounce.setYSpeed(0);
			playerToBounce.setY(_y - _curveOffset + (playerToBounce.getHeight() / 3));
			_curveOffset += _change;
			// Once it reaches 25, begin decreasing curve offset
			if (_curveOffset == 25) {
				_change = -1;
				// Once it reaches 0 again, make the curve offset -1
			} else if (_curveOffset == 0) {
				_curveOffset = -1;
				_change = 1;
			}
		} else {
			// if the curve offset is less than 1, set it to 0 and launch the player
			_curveOffset = 0;
			playerToBounce.bounce(17 / Constants.SPEED_FACTOR);
		}

	}

	/**
	 * Called when the Platform is on the screen and not yet drawn
	 */
	protected void draw(Pane pane) {

		// Create Quad Curve
		_platformCurve = new QuadCurve(_x - (_width / 2), _game.getTranslateY(_y), _x, _game.getTranslateY(_y),
				_x + (_width / 2), _game.getTranslateY(_y));
		_platformCurve.setStroke(Color.WHITE);
		_platformCurve.setStrokeWidth(5);
		_platformCurve.setFill(null);
		_platformNode = _platformCurve;

		// Path to cobweb svg
		String path = "M272.545,35.664c0.025-0.612-0.355-1.255-1.149-1.302c-44.926-2.669-90.092-1.623-135.017,0.333\n"
				+ "		c-37.802,1.646-75.132,0.068-112.733-1.639c-0.303-0.179-0.624-0.18-0.898-0.04c-7.17-0.326-14.347-0.653-21.543-0.966\n"
				+ "		c-1.63-0.071-1.586,2.386,0,2.525c7.445,0.655,14.895,1.192,22.347,1.667c13.707,16.959,17.012,41.897,9.292,62.311\n"
				+ "		c-2.07,1.645-4.156,3.288-6.19,4.938c-0.999,0.811,0.324,2.556,1.381,1.803c1.932-1.377,3.906-2.678,5.848-4.036\n"
				+ "		c0.357-0.018,0.679-0.189,0.879-0.545c39.707,7.709,74.168,28.949,98.433,61.645c0.143,0.193,0.315,0.32,0.497,0.413\n"
				+ "		c-0.12,8.895-0.448,17.783-1.154,26.659c-2.251,0.493-4.346,1.4-6.147,2.642c-0.604,0.202-1.151,0.505-1.698,0.807\n"
				+ "		c-3.159-3.553-3.488-8.288-1.302-12.824c0.511-1.06-0.912-2.424-1.749-1.359c-3.873,4.929-3.074,11.427,0.993,15.642\n"
				+ "		c-1.394,1.25-2.545,2.81-3.36,4.605c-3.385-0.836-6.698-2.12-9.006-4.794c-3.004-3.481-3.472-8.349-3.292-12.758\n"
				+ "		c0.052-1.261-1.891-1.528-2.187-0.298c-1.182,4.91-0.361,10.19,2.738,14.255c2.648,3.473,6.643,5.252,10.814,6.131\n"
				+ "		c-0.394,1.467-0.601,3.009-0.6,4.563c-2.255-0.282-4.662-0.048-6.733,0.276c-4.355,0.681-9.024,2.679-10.743,7.03\n"
				+ "		c-0.485,1.227,1.209,1.907,2.022,1.186c2.655-2.354,4.82-4.295,8.412-5.133c2.024-0.473,4.777-0.893,7.285-0.656\n"
				+ "		c0.218,1.35,0.541,2.687,1.105,3.957c0.333,0.751,0.764,1.42,1.187,2.094c-6.777,1.979-11.82,9.926-9.293,16.983\n"
				+ "		c0.415,1.16,2.354,1.087,2.273-0.31c-0.22-3.808,0.185-7.654,2.79-10.65c1.698-1.954,3.819-2.958,6.115-3.554\n"
				+ "		c1.681,1.826,3.677,3.296,5.898,4.293c-0.403,1.555-0.332,3.258,0.242,4.744c0.631,1.634,1.796,2.853,3.213,3.67\n"
				+ "		c-1.006,1.224-1.516,2.817-1.011,4.484c0.278,0.917,1.496,0.672,1.584-0.216c0.121-1.215,0.746-2.583,1.704-3.407\n"
				+ "		c0.522,0.114,1.042,0.241,1.59,0.254c0.723,0.017,1.437-0.079,2.131-0.244c0.89,0.885,0.467,2.237,0.669,3.502\n"
				+ "		c0.105,0.659,1.013,0.971,1.533,0.628c1.855-1.224,1.357-3.402,0.092-5.036c1.29-0.751,2.384-1.839,3.102-3.202\n"
				+ "		c0.814-1.546,1.204-3.557,0.969-5.417c1.886-0.964,3.622-2.299,5.106-4.026c4.164,0.837,7.777,3.236,9.921,7.253\n"
				+ "		c2.562,4.799,2.244,9.756,2.026,14.988c-0.06,1.426,2.045,1.658,2.428,0.331c2.815-9.762-2.737-22.952-12.906-24.599\n"
				+ "		c1.375-2.25,2.047-4.862,2.124-7.548c2.13-0.136,4.261-0.094,6.316,0.513c4.748,1.402,6.113,4.799,7.53,9.1\n"
				+ "		c0.345,1.046,1.938,0.896,1.946-0.265c0.064-9.376-8.253-11.634-15.839-11.168c-0.136-1.978-0.594-3.954-1.303-5.829\n"
				+ "		c9.902-1.934,17.836-10.321,18.698-20.856c0.115-1.404-2.214-1.74-2.463-0.336c-1.751,9.878-8.042,16.905-17.173,19.078\n"
				+ "		c-1.002-1.956-2.318-3.712-3.873-5.151c4.42-2.24,7.429-8.171,6.506-12.788c-0.247-1.239-1.889-0.787-2.015,0.275\n"
				+ "		c-0.299,2.526-0.744,4.971-2.117,7.156c-1.107,1.761-2.548,2.847-4.112,3.949c-1.473-1.008-3.111-1.74-4.891-2.086\n"
				+ "		c-1.422-0.276-2.852-0.363-4.257-0.294c0.83-9.145,1.307-18.413,1.541-27.748c25.655-25.821,54.405-39.341,91.552-34.518\n"
				+ "		c0.429,0.056,0.774-0.094,1.026-0.352c0.264,0.213,0.528,0.434,0.792,0.647c0.443,0.358,0.954,0.169,1.214-0.181\n"
				+ "		c0.359-0.088,0.68-0.331,0.706-0.846c0.917-18.431,1.583-33.157,9.64-50.324c7.032-14.985,15.64-29.213,30.079-37.967\n"
				+ "		C272.912,37.22,272.937,36.312,272.545,35.664z M224.073,36.663c-17.7,12.923-27.58,36.014-26.029,57.824\n"
				+ "		c-6.804-6.253-13.61-12.512-20.519-18.664c1.303-16.332,5.424-30.538,20.337-39.288C206.598,36.546,215.335,36.609,224.073,36.663z\n"
				+ "		 M192.642,36.517c-12.208,7.679-17.459,23.085-17.315,37.326c-6.647-5.896-13.336-11.734-20.069-17.481\n"
				+ "		c0.276-4.104-0.06-8.413,1.997-12.091c1.978-3.536,5.276-5.7,8.74-7.55C174.883,36.578,183.764,36.524,192.642,36.517z\n"
				+ "		 M136.379,37.556c7.825-0.34,15.639-0.551,23.448-0.714c-2.102,1.604-3.945,3.544-5.206,5.88c-1.891,3.504-2.007,7.69-1.674,11.683\n"
				+ "		c-6.665-5.662-13.359-11.261-20.097-16.724C134.027,37.642,135.203,37.607,136.379,37.556z M130.847,37.751\n"
				+ "		c6.192,6.513,12.616,12.908,19.146,19.235c-6.406-0.935-13.128,2.152-17.014,7.371c-0.748-8.952-1.526-17.834-2.279-26.601\n"
				+ "		C130.748,37.755,130.797,37.753,130.847,37.751z M132.993,123.81c-12.232-24.143-33.29-41.876-59.337-49.238\n"
				+ "		c6.144-3.989,12.295-7.966,18.443-11.946c18.656,1.443,32.433,11.734,39.23,29.307C131.933,102.559,132.531,113.186,132.993,123.81\n"
				+ "		z M130.895,84.185c-6.634-13.654-20.518-22.889-35.742-23.549c5.617-3.641,11.234-7.281,16.821-10.97\n"
				+ "		c5.796,0.746,11.175,4.251,13.828,9.552c1.695,3.387,2.84,7.039,4.336,10.526C130.37,74.556,130.628,79.37,130.895,84.185z\n"
				+ "		 M129.784,61.793c-1.352-3.539-3.081-6.928-5.649-9.272c-2.467-2.252-5.82-3.913-9.276-4.756c4.775-3.168,9.537-6.358,14.27-9.597\n"
				+ "		C129.21,46.036,129.458,53.912,129.784,61.793z M125.642,37.935c-5.233,2.495-10.425,5.208-15.594,8.02\n"
				+ "		c0.457-2.75-0.123-5.434-1.811-7.601C114.039,38.258,119.84,38.127,125.642,37.935z M106.4,38.373\n"
				+ "		c1.858,2.738,2.451,5.875,0.93,9.104c-4.951,2.749-9.851,5.664-14.725,8.646c1.304-6.067-0.189-13.217-4.32-17.71\n"
				+ "		C94.323,38.428,100.361,38.462,106.4,38.373z M84.723,38.42c2.154,2.66,4.139,5.101,5.082,8.702\n"
				+ "		c1.037,3.957,0.452,7.44-0.596,11.093c-6.174,3.854-12.255,7.895-18.272,12.025c1.128-11.812-4.091-23.905-12.22-32.327\n"
				+ "		C67.384,38.175,76.052,38.368,84.723,38.42z M26.669,36.445c9.806,0.593,19.617,1.052,29.431,1.374\n"
				+ "		c9.514,9.818,12.429,21.257,12.061,34.378c-10.759,7.48-21.254,15.285-31.357,23.203C43.168,75.945,39.345,52.353,26.669,36.445z\n"
				+ "		 M36.976,99.139c11.333-7.862,22.781-15.519,34.315-23.022c27.629,8.324,50.129,27.703,61.974,54.35\n"
				+ "		c0.348,9.425,0.539,18.847,0.488,28.261C109.745,127.042,75.996,105.976,36.976,99.139z M133.272,67.924\n"
				+ "		c4.902-6.743,11.105-8.666,19.55-7.505c0.21,0.029,0.413-0.013,0.607-0.082c5.797,5.546,11.716,11.01,17.696,16.416\n"
				+ "		c-13.15-1.447-27.156,3.333-36.185,13.128C134.425,82.535,133.872,75.203,133.272,67.924z M135.123,92.506\n"
				+ "		c11.677-9.912,24.037-14.111,39.412-12.42c0.106,0.012,0.192-0.019,0.288-0.031c6.262,5.603,12.61,11.107,18.984,16.565\n"
				+ "		c-22.361,2.869-44.008,11.492-56.891,30.818C136.549,115.797,135.917,104.126,135.123,92.506z M137.125,159.576\n"
				+ "		c0.209-9.68,0.146-19.435-0.123-29.216c0.019-0.022,0.046-0.017,0.064-0.041c14.248-19.927,35.881-28.232,59.48-30.933\n"
				+ "		c0.139-0.016,0.246-0.075,0.365-0.118c9.625,8.19,19.314,16.197,28.979,24.054C192.352,119.101,156.885,130.961,137.125,159.576z\n"
				+ "		 M230.363,124.74c-0.153-0.144-0.305-0.288-0.456-0.432c-0.105-0.179-0.261-0.318-0.449-0.425\n"
				+ "		c-9.592-9.16-19.345-18.296-29.2-27.382c-0.719-24.648,9.316-44.345,28.023-59.816c13.245,0.083,26.5,0.143,39.778,0.099\n"
				+ "		C240.491,54.744,227.627,93.205,230.363,124.74z";
		SVGPath cobweb = new SVGPath();
		cobweb.setContent(path);

		// Create Region with the SVG
		_cobwebRegion = new Region();
		_cobwebRegion.setShape(cobweb);
		_cobwebRegion.setMinSize(_width, _width);
		_cobwebRegion.setMaxSize(_width, _width);
		_cobwebRegion.setStyle("-fx-background-color: white;");
		_cobwebRegion.setTranslateX(_x - _width / 2);
		_cobwebRegion.setTranslateY(_game.getTranslateY(_y));
		pane.getChildren().addAll(_platformCurve, _cobwebRegion);

	}

	/**
	 * Called when the timeline updates. Updates the curve and Y values of the
	 * platform.
	 */
	@Override
	protected void update() {
		// If it has not yet been drawn, but is on the screen, draw it
		if (_game.getTranslateY(_y) > 0 && !_onScreen) {
			this.draw(_game.getGamePane());
			_onScreen = true;
		} else if (_onScreen) {
			// Otherwise update the y values
			_platformCurve.setStartY(_game.getTranslateY(_y));
			_platformCurve.setEndY(_game.getTranslateY(_y));
			_platformCurve.setControlY(_game.getTranslateY(_y - _curveOffset));
			_cobwebRegion.setTranslateX(_x - _width / 2);
			_cobwebRegion.setTranslateY(_game.getTranslateY(_y));
		}

	}

	/**
	 * Removes all nodes from the Pane.
	 */
	@Override
	public void removeFromPane() {
		_game.getGamePane().getChildren().removeAll(_platformCurve, _cobwebRegion);

	}
}
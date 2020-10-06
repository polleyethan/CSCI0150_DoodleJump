package DoodleJump;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;

/**
 * Subclass of Toy. Witches hat.
 *
 */
public class HatToy extends Toy {

	/**
	 * Constructor for HatToy
	 */
	public HatToy(double x, double y, Pane gamePane) {
		super(x, y, gamePane);
	}

	/**
	 * draws the toy to the given coordinates
	 */
	@Override
	protected void createToy(double x, double y) {
		_timeleft = 3;
		_width = 50;
		_height = 50;
		// Path to the hat svg
		String svgpath = "M394.496,409.887c-12.064-3.22-8.913,3.5-27.655,6.13c-27.599,3.874-28.436,1.112-39.281-3.407  c-12.267-5.111-14.573,9.354-33.763,12.67c-4.282,0.74-8.595,1.197-12.923,1.207c-18.725,0.042-44.944-2.319-61.894-4.584  c-35.8-4.784-67.355-10.696-98.273-29.38c-4.902-2.963-9.913-7.644-12.275-13.079c-0.87-2.002-0.43-3.955,1.328-5.311  c5.015-3.867,10.324-5.252,17.099-6.667c19.26-4.023,39.645-0.715,66.074-7.696c7.102-1.876,12.727-5.801,17.191-11.519  c3.623-4.641,4.22-7.156,8.738-9.094c7.814-3.352,10.889-4.666,7.759-17.336c-1.231-4.982-0.629-9.756,1.623-14.28  c13.256-26.626,33.668-46.381,34.489-84.627c0.153-7.143,0.113-14.291,0.06-21.437c-0.023-3.112-0.662-6.139-1.878-9.03  c-3.101-7.371-8.616-11.133-16.595-10.837c-9.098,0.338-17.974,2.074-26.549,5.169c-8.111,2.928-12.896,8.495-12.992,17.153  c-0.059,5.317,3.808,10.704,8.209,13.419c4.514,2.785,6.155,3.178,10.777,6.859c0.459,0.365,0.803,0.951,1.04,1.504  c0.59,1.38,0.087,2.624-1.31,3.165c-2.557,0.989-11.997,1.234-21.95-2.658c-12.322-4.819-16.416-17.441-14.126-29.588  c0.91-4.828,2.297-9.505,4.233-14.018c5.646-13.162,23.043-17.188,36.305-22.279c23.728-9.11,28.195-12.448,35.931-10.132  c10.851,3.249,26.846,9.578,31.456,11.139c11.915,4.035,7.217,16.847,18.409,20.52c3.588,1.178,6.262,3.453,8.241,6.62  c18.561,29.703,21.011,31.951,19.378,37.534c-2.852,9.749-3.77,13.507,7.087,23.331c4.3,3.891,7.41,8.593,9.543,13.946  c2.018,5.065,4.073,10.123,5.878,15.266c7.133,20.329,10.766,26.179,19.636,50.263c1,2.714,1.122,5.42,0.173,8.247  c-1.937,5.769,0.403,10.183,4.708,13.013c4.933,3.243,8.187,3.867,27.137,10.98c15.123,5.677,34.645,14.407,47.838,23.599  c4.151,2.893,6.114,6.337,3.375,10.637c-7.893,12.389-38.127,14.406-53.884,16.21C411.132,411.707,402.642,412.062,394.496,409.887z  ";
		SVGPath hat = new SVGPath();
		hat.setContent(svgpath);

		_region = new Region();
		_region.setShape(hat);
		_region.setMinSize(_width, _height);
		_region.setMaxSize(_width, _height);
		_region.setStyle("-fx-background-color: #521880;");
		_region.setTranslateX(x - _width / 2);
		_region.setTranslateY(y - 2 * _height);

		_gamePane.getChildren().add(_region);

	}

	/**
	 * Updates the toy to the given coordinates
	 */
	@Override
	public void update(double x, double y) {
		_region.setTranslateX(x - _width / 2);
		_region.setTranslateY(y - 2 * _height);

	}

	/**
	 * Effects the player until the _timeleft is 0, then erases the toy from the
	 * user.
	 */
	@Override
	public void effectPlayer(Player playerToEffect) {
		if (_timeleft > 0) {
			playerToEffect.setYSpeed(10 / Constants.SPEED_FACTOR);
			_timeleft -= Constants.TIMELINE_DELAY;
		} else {
			playerToEffect.removeToy();
			_gamePane.getChildren().remove(_region);
		}
	}

}

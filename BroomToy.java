package DoodleJump;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;

/**
 * Subclass of Toy. Flying broomstick
 *
 */
public class BroomToy extends Toy {

	/**
	 * Constructor for BroomToy
	 */
	public BroomToy(double x, double y, Pane gamepane) {
		super(x, y, gamepane);
	}

	/**
	 * Draws the Toy at the given X and Y coords
	 */
	@Override
	protected void createToy(double x, double y) {
		_timeleft = 4;
		_width = 30;
		_height = 70;
		String svgpath = "M180.598,7.481c-13.368,46.229-24.214,93.079-37.97,139.194c1.115,5.073,1.108,7.55-0.828,12.327   c1.808,9.989,4.375,14.832-1.975,25.429c0.057,0.315,0.039,0.579,0.024,0.842c2.187,2.014,0.314,7.496-0.932,9.611   c6.704,25.749,1.266,48.911-15.098,69.481c-1.199,0.933-2.805,2.852-4.382,3.137c-0.762-1.685,1.695-4.669,1.33-6.687   c-1.4,1.622-7.125,8.844-10.029,6.83c0.82-2.297,2.887-6.709,2.441-9.169c-2.502,4.166-4.182,6.032-9.09,6.269   c-0.536-2.963,1.578-6.406,1.522-9.587c-2.261,2.623-3.139,5.322-3.957,8.726c-4.636-0.073-2.316-11.368-2.154-13.351   c-1.905,0.997-4.094,7.253-4.916,9.552c-0.252,0.045-0.503,0.091-0.832,0.085c-2.447-6.329-1.972-13.057-1.537-19.649   c-1.285,1.535-2.57,3.07-3.569,4.749c-0.326-0.006-0.716-0.001-1.116-0.059l0,0c-0.2-2.177-0.2-2.177-0.135-4.339   c3.063-22.443,6.461-46.415,22.179-64.185c2.065-2.978,3.844-5.383,6.625-7.645c2.275-6.143,6.342-12.089,11.447-16.268   c3.175-1.877,3.175-1.877,4.018-2.616c20.055-44.086,24.693-99.496,38.715-146.232c1.394-2.01,4.187-3.492,6.582-3.926   C180.436,1.193,180.795,4.255,180.598,7.481z";
		SVGPath broom = new SVGPath();
		broom.setContent(svgpath);

		_region = new Region();
		_region.setShape(broom);
		_region.setMinSize(_width, _height);
		_region.setMaxSize(_width, _height);
		_region.setStyle("-fx-background-color: #5a3e2a;");
		_region.setTranslateX(x - _width / 2);
		_region.setTranslateY(y - _height);

		_gamePane.getChildren().add(_region);
	}

	/**
	 * updates the Toy to the given X and Y coords
	 */
	@Override
	public void update(double x, double y) {
		_region.setTranslateX(x - _width / 2);
		_region.setTranslateY(y - _height);

	}

	/**
	 * Effects the player until the _timeleft is 0, then erases the toy from the
	 * user.
	 */
	@Override
	public void effectPlayer(Player playerToEffect) {
		if (_timeleft > 0) {
			playerToEffect.setYSpeed(7 / Constants.SPEED_FACTOR);
			_timeleft -= Constants.TIMELINE_DELAY;
		} else {
			playerToEffect.removeToy();
			_gamePane.getChildren().remove(_region);
		}
	}
}

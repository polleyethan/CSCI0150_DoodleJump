package DoodleJump;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

/**
 * Graphical representation of the Player class.
 *
 */
public class Doodle {
	private Region _shapeRegion;
	private Game _game;
	private Text _nameText;
	private double _height;
	private double _width;

	/**
	 * Constructor for the Doodle class. Draws the doodle with the given paraments
	 * in the game
	 */
	public Doodle(double x, double y, double width, double height, Game game, String name) {
		_game = game;
		_height = height;
		_width = width;
		_nameText = new Text(name);
		_nameText.setX(x - _width / 2);
		_nameText.setY(_game.getTranslateY(y - height));
		_nameText.setFill(Color.WHITE);
		_game.getGamePane().getChildren().add(_nameText);
		this.createDoodle(x, y, width, height, _game.getGamePane());
	}

	/**
	 * Draws the doodle.
	 */
	public void createDoodle(double x, double y, double width, double height, Pane gamePane) {
		// SVGPath of the ghost shape
		String path = "M411.972,204.367c0-118.248-83.808-204.777-186.943-204.365C121.896-0.41,38.001,86.119,38.001,204.367L38.373,441\n"
				+ "	l62.386-29.716l62.382,38.717l62.212-38.716l62.215,38.718l62.213-38.714l62.221,29.722L411.972,204.367z M143.727,258.801\n"
				+ "	c-27.585-6.457-44.713-34.053-38.256-61.638l99.894,23.383C198.908,248.13,171.312,265.258,143.727,258.801z M306.276,258.801\n"
				+ "	c-27.585,6.457-55.181-10.671-61.638-38.256l99.894-23.383C350.988,224.748,333.861,252.344,306.276,258.801z";
		SVGPath doodle = new SVGPath();
		doodle.setContent(path);

		_shapeRegion = new Region();
		_shapeRegion.setShape(doodle);
		_shapeRegion.setMinSize(width, height);
		_shapeRegion.setMaxSize(width, height);
		_shapeRegion.setStyle("-fx-background-color: white;");
		_shapeRegion.setTranslateX(x - width / 2);
		_shapeRegion.setTranslateY(_game.getTranslateY(y + (height / 2)));

		_game.getGamePane().getChildren().add(_shapeRegion);

	}

	/**
	 * Updates the doodles position to the given x and y coordinate
	 */
	public void updatePosition(double x, double y) {
		_shapeRegion.setTranslateX(x - _width / 2);
		_shapeRegion.setTranslateY(_game.getTranslateY(y + (_height / 2)));
		_nameText.setX(x - _width / 2);
		_nameText.setY(_game.getTranslateY(y - (_height)));
	}

	/**
	 * Accessor for the doodles node
	 */
	public Node getDoodleNode() {
		return _shapeRegion;
	}

	/**
	 * Removes nodes from the gamepane
	 */
	public void removeNodes() {
		_game.getGamePane().getChildren().remove(_shapeRegion);
		_game.getGamePane().getChildren().remove(_nameText);
	}
}
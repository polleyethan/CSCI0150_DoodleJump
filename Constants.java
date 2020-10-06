package DoodleJump;

/**
 * Constants class.
 *
 */
public class Constants {
	public static final double TIMELINE_DELAY = 0.0025;
	public static final double SPEED_FACTOR = 10;
	public static final double GRAVITY_Y_ACCELL = -9.8 * TIMELINE_DELAY / SPEED_FACTOR;
	public static final double GAME_PANE_WIDTH = 400;
	public static final double GAME_PANE_HEIGHT = 600;
	public static final double SCENE_WIDTH = 700;
	public static final double SCENE_HEIGHT = 700;

	public static final double REGULAR_PLATFORM_BOUNCE = 9 / SPEED_FACTOR;

	public static final double TRAMPOLINE_PLAT_PROB = .1;

	public static final double BREAK_PLAT_PROB = .1;

	public static final double HAT_TOY_PROB = .025;
	public static final double BROOM_TOY_PROB = .05;

	private Constants() {

	}
}
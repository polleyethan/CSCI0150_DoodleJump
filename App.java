package DoodleJump;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Instantiate Menu Organizer and show it in the scene
 */
public class App extends Application {

	@Override
	public void start(Stage stage) {
		MenuOrganizer organizer = new MenuOrganizer(stage);
		stage.setTitle("DoodleJump");
		Scene main = new Scene(organizer.getRootPane(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
		main.getStylesheets().add("file:./DoodleJump/styles.css");
		stage.setScene(main);
		stage.show();
	}

	/*
	 * Here is the mainline! No need to change this.
	 */
	public static void main(String[] argv) {
		// launch is a static method inherited from Application.
		launch(argv);
	}
}

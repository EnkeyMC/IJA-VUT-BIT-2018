package ija.project;

import ija.project.ui.controllers.MainPanelController;
import ija.project.utils.UIComponentLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BlockEditor extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		Parent root = null;
		try {
			UIComponentLoader<MainPanelController> loader = new UIComponentLoader<>(MainPanelController.class);
			root = loader.load();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		primaryStage.setTitle("Hello World");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
}

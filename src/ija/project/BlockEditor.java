package ija.project;

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
			root = UIComponentLoader.loadComponent("MainPanel.fxml");
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		primaryStage.setTitle("Hello World");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
}

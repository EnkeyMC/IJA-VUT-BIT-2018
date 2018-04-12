package ija.project;

import ija.project.register.BlockRegister;
import ija.project.schema.BlockType;
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
			e.printStackTrace();
			System.exit(1);
		}
		primaryStage.setTitle("BlockType Editor");
		primaryStage.setScene(new Scene(root));
		BlockRegister.reg("Built-in", new BlockType("1", "Block1"));
		BlockRegister.reg("Built-in", new BlockType("2", "Block2"));
		BlockRegister.reg("Built-in", new BlockType("3", "Block3"));
		BlockRegister.reg("Built-in", new BlockType("3", "Block3"));
		BlockRegister.reg("ASfd", new BlockType("3", "Block3"));
		BlockRegister.reg("Built-insf", new BlockType("3", "Block3"));
		BlockRegister.reg("Built-in", new BlockType("3", "Block3"));
		BlockRegister.reg("User", new BlockType("4", "Block4"));
		BlockRegister.reg("User", new BlockType("5", "Block5"));
		primaryStage.show();
	}
}

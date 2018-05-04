package ija.project;

import ija.project.register.ComponentLoader;
import ija.project.schema.BlockFactory;
import ija.project.schema.Block;
import ija.project.schema.ResultBlock;
import ija.project.schema.ValueBlock;
import ija.project.ui.control.schema.BlockControl;
import ija.project.ui.control.schema.BlockControlFactory;
import ija.project.ui.control.schema.ResultBlockControl;
import ija.project.ui.control.schema.ValueBlockControl;
import ija.project.ui.controllers.MainPanelController;
import ija.project.ui.utils.UIComponentLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class BlockEditor extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		Parent root = null;
		UIComponentLoader<MainPanelController> loader = new UIComponentLoader<>(MainPanelController.class);
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/ija/project/resources/images/icon.png")));
		primaryStage.setTitle("Block editor");
		primaryStage.setScene(new Scene(root));
		primaryStage.setMinWidth(400);
		primaryStage.setMinHeight(300);

		primaryStage.setOnCloseRequest(event -> loader.getController().onExit(event));

		primaryStage.show();

		BlockFactory.registerBlock(Block.XML_TAG, Block.class);
		BlockFactory.registerBlock(ValueBlock.XML_TAG, ValueBlock.class);
		BlockFactory.registerBlock(ResultBlock.XML_TAG, ResultBlock.class);

		BlockControlFactory.registerControl(Block.class, BlockControl.class);
		BlockControlFactory.registerControl(ValueBlock.class, ValueBlockControl.class);
		BlockControlFactory.registerControl(ResultBlock.class, ResultBlockControl.class);

		ComponentLoader.loadFromXML(getClass().getResource("/ija/project/resources/schema/builtin.xml"));
	}
}

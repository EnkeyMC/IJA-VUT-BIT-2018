package ija.project.ui.control.schema;

import ija.project.register.BlockTypeRegister;
import ija.project.schema.Block;
import ija.project.schema.BlockType;
import ija.project.schema.Schema;
import ija.project.utils.UIComponentLoader;
import ija.project.utils.UIContolLoader;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SchemaControl extends VBox {

	@FXML
	private AnchorPane schemaPane;

	private Schema schema;

	public static String getFXMLPath() {
		return "schema/Schema.fxml";
	}

	public SchemaControl() {
		super();
		UIContolLoader.load(this);

		schema = new Schema();

		BlockControl blockControl = new BlockControl(new BlockType());
		schemaPane.getChildren().add(blockControl);
		blockControl.setLayoutX(50);
		blockControl.setLayoutY(50);
	}

	public void bindDisplayNameTo(Property property) {
		property.bindBidirectional(schema.displayNameProperty());
	}

	@FXML
	private void onDragOver(DragEvent event) {
		Dragboard db = event.getDragboard();
		if (db.hasString()) {
			event.acceptTransferModes(TransferMode.COPY);
		}
	}

	@FXML
	private void onDragDropped(DragEvent event) {
		Dragboard db = event.getDragboard();
		if (db.hasString()) {
			String[] parts = db.getString().split(":");
			BlockType type;
			try {
				type = BlockTypeRegister.getBlockTypeById(parts[0], parts[1]);
			} catch (RuntimeException e) {
				e.printStackTrace();
				event.setDropCompleted(false);
				return;
			}

			BlockControl blockControl = new BlockControl(type);
			schemaPane.getChildren().add(blockControl);
			blockControl.setLayoutX(event.getX() - blockControl.getPrefWidth()/2);
			blockControl.setLayoutY(event.getY() - blockControl.getPrefHeight()/2);
			schema.addBlock(blockControl.getBlock());
			event.setDropCompleted(true);
		} else {
			event.setDropCompleted(false);
		}
	}
}

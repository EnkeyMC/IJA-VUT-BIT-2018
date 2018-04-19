package ija.project.ui.control.schema;

import ija.project.register.BlockTypeRegister;
import ija.project.schema.BlockType;
import ija.project.schema.Schema;
import ija.project.ui.utils.UIContolLoader;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SchemaControl extends VBox implements Initializable {

	@FXML
	private AnchorPane schemaPane;

	private Schema schema;

	private BlockConnector connector;

	public static String getFXMLPath() {
		return "schema/Schema.fxml";
	}

	public SchemaControl() {
		super();
		UIContolLoader.load(this);
		schema = new Schema();
	}

	public void bindDisplayNameTo(Property property) {
		schema.displayNameProperty().bindBidirectional(property);
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

			BlockControl blockControl = new BlockControl(this, type);
			schemaPane.getChildren().add(blockControl);
			blockControl.setLayoutX(event.getX() - blockControl.getPrefWidth()/2);
			blockControl.setLayoutY(event.getY() - blockControl.getPrefHeight()/2);
			schema.addBlock(blockControl.getBlock());
			event.setDropCompleted(true);
		} else {
			event.setDropCompleted(false);
		}
	}

	public void startConnection(BlockControl srcBlock, BlockPortControl srcPort) {
		connector = new BlockConnector(this, srcBlock, srcPort);
	}

	public void endConnection(BlockControl dstBlock, BlockPortControl dstPort) {
		assert connector != null;
		connector.connect(dstBlock, dstPort);
		connector = null;
	}

	public boolean isBlockConnectingActive() {
		return connector != null;
	}

	public AnchorPane getSchemaPane() {
		return schemaPane;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
}

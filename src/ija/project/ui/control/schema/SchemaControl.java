package ija.project.ui.control.schema;

import ija.project.exception.ApplicationException;
import ija.project.register.BlockTypeRegister;
import ija.project.schema.Block;
import ija.project.schema.BlockType;
import ija.project.schema.Schema;
import ija.project.ui.utils.UIContolLoader;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SchemaControl extends VBox {

	@FXML
	private AnchorPane schemaPane;

	private Schema schema;

	private Map<Long, BlockControl> blockControls;

	private BlockConnector connector;

	private BooleanProperty changed = new SimpleBooleanProperty(false);

	public static String getFXMLPath() {
		return "schema/Schema.fxml";
	}

	public SchemaControl(Schema schema) {
		super();
		UIContolLoader.load(this);
		this.schema = schema;
		blockControls = new HashMap<>();

		Collection<Block> blocks = schema.getBlockCollection();
		BlockControl blockControl;
		for (Block block : blocks) {
			blockControl = new BlockControl(this, block);
			addBlockControl(blockControl);
		}

		BlockConnector blockConnector;
		BlockPortControl blockPortControl;
		for (Block block : blocks) {
			for (Map.Entry<String, Pair<Block, String>> connection : block.getConnections().entrySet()) {
				if (connection.getValue() != null) {
					blockControl = blockControls.get(block.getId());
					blockPortControl = blockControl.getPortControl(connection.getKey());
					if (!blockPortControl.isInput()) {
						blockConnector = new BlockConnector(this, blockControl, blockPortControl);

						blockControl = blockControls.get(connection.getValue().getKey().getId());
						blockPortControl = blockControl.getPortControl(connection.getValue().getValue());
						blockConnector.connect(blockControl, blockPortControl);
					}
				}
			}
		}

		schemaPane.getChildren().addListener(new ListChangeListener<Node>() {
			@Override
			public void onChanged(Change<? extends Node> c) {
				setChanged(true);
			}
		});
	}

	public void bindDisplayNameTo(StringProperty property) {
		property.bind(Bindings.when(changed).then("*").otherwise("").concat(schema.displayNameProperty()));
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

			BlockControl blockControl = new BlockControl(this, new Block(type));
			addBlockControl(blockControl);
			schema.addBlock(blockControl.getBlock());
			blockControl.setLayoutX(event.getX() - blockControl.getPrefWidth()/2);
			blockControl.setLayoutY(event.getY() - blockControl.getPrefHeight()/2);

			setChanged(true);
			event.setDropCompleted(true);
		} else {
			event.setDropCompleted(false);
		}
	}

	protected void addBlockControl(BlockControl blockControl) {
		schemaPane.getChildren().add(blockControl);
		blockControls.put(blockControl.getBlock().getId(), blockControl);
	}

	public void startConnection(BlockControl srcBlock, BlockPortControl srcPort) {
		connector = new BlockConnector(this, srcBlock, srcPort);
	}

	public void endConnection(BlockControl dstBlock, BlockPortControl dstPort) {
		assert connector != null;
		try {
			connector.connect(dstBlock, dstPort);
			setChanged(true);
		} catch (ApplicationException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error occurred");
			alert.setHeaderText("Cannot connect ports");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
		connector = null;
	}

	public boolean isBlockConnectingActive() {
		return connector != null;
	}

	public AnchorPane getSchemaPane() {
		return schemaPane;
	}

	public Schema getSchema() {
		return schema;
	}

	public boolean isChanged() {
		return changed.get();
	}

	public BooleanProperty changedProperty() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed.set(changed);
	}
}

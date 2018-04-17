package ija.project.ui.control.schema;

import ija.project.schema.Block;
import ija.project.schema.BlockPort;
import ija.project.schema.BlockType;
import ija.project.ui.control.Spacer;
import ija.project.ui.utils.UIContolLoader;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Map;

public class BlockControl extends BorderPane {

	private Block block;

	private SchemaControl schemaControl;

	private double dragStartX;
	private double dragStartY;

	public static String getFXMLPath() {
		return "schema/Block.fxml";
	}

	public BlockControl(SchemaControl schemaControl, BlockType blockType) {
		super();
		UIContolLoader.load(this);
		this.schemaControl = schemaControl;

		Label label = new Label(blockType.getDisplayName());
		this.setCenter(label);

		HBox inputPorts = new HBox(5);
		HBox outputPorts = new HBox(5);
		this.setTop(inputPorts);
		this.setBottom(outputPorts);

		ArrayList<BlockPort> ports = blockType.getInputPorts();
		BlockPortControl blockPortControl;
		inputPorts.getChildren().add(new Spacer());
		for (BlockPort port : ports) {
			blockPortControl = new BlockPortControl(this, port);
			inputPorts.getChildren().add(blockPortControl);
			inputPorts.getChildren().add(new Spacer());
		}

		ports = blockType.getOutputPorts();
		outputPorts.getChildren().add(new Spacer());
		for (BlockPort port : ports) {
			blockPortControl = new BlockPortControl(this, port);
			outputPorts.getChildren().add(blockPortControl);
			outputPorts.getChildren().add(new Spacer());
		}

		block = new Block(blockType);
		block.xProperty().bindBidirectional(this.layoutXProperty());
		block.yProperty().bindBidirectional(this.layoutYProperty());
		this.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
		this.setCursor(Cursor.MOVE);
		dragStartX = 0;
		dragStartY = 0;
	}

	public Block getBlock() {
		return block;
	}

	@FXML
	private void onMousePressed(MouseEvent event) {
		if (event.isPrimaryButtonDown()) {
			Point2D local = this.sceneToLocal(event.getSceneX(), event.getSceneY());
			dragStartX = event.getX();
			dragStartY = event.getY();
			event.consume();
		}
	}

	@FXML
	private void onMouseDragged(MouseEvent event) {
		if (event.isPrimaryButtonDown()) {
			Point2D local = this.getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
			if (local.getX() < 0 || local.getY() < 0)
				return;  // TODO a bit better handling

			this.setLayoutX(local.getX() - dragStartX);
			this.setLayoutY(local.getY() - dragStartY);
			event.consume();
		}
	}

	public SchemaControl getSchemaControl() {
		return schemaControl;
	}
}

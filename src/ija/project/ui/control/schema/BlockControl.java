package ija.project.ui.control.schema;

import com.sun.javafx.cursor.CursorType;
import ija.project.schema.Block;
import ija.project.schema.BlockPort;
import ija.project.schema.BlockType;
import ija.project.ui.control.Spacer;
import ija.project.ui.utils.UIContolLoader;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlockControl extends BorderPane implements Removable {

	private Block block;

	private SchemaControl schemaControl;

	private Map<String, BlockPortControl> blockPortControls;

	private double dragStartX;
	private double dragStartY;

	public static String getFXMLPath() {
		return "schema/Block.fxml";
	}

	public BlockControl(SchemaControl schemaControl, BlockType blockType) {
		super();
		block = new Block(blockType);
		this.schemaControl = schemaControl;

		init(blockType);
	}

	public BlockControl(SchemaControl schemaControl, Block block) {
		super();
		this.schemaControl = schemaControl;
		this.block = block;

		init(block.getBlockType());
	}

	protected void init(BlockType blockType) {
		UIContolLoader.load(this);

		blockPortControls = new HashMap<>();
		dragStartX = 0;
		dragStartY = 0;
		this.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
		updateCursor();

		Label label = new Label(blockType.getDisplayName());
		this.setCenter(label);
		this.schemaControl.toolRemoveSelectedProperty().addListener((observable, oldValue, newValue) -> updateCursor());
		updateCursor();

		HBox inputPorts = new HBox(5);
		HBox outputPorts = new HBox(5);
		this.setTop(inputPorts);
		this.setBottom(outputPorts);

		ArrayList<BlockPort> ports = blockType.getInputPorts();
		BlockPortControl blockPortControl;
		inputPorts.getChildren().add(new Spacer());
		for (BlockPort port : ports) {
			blockPortControl = new BlockPortControl(this, port, true);
			blockPortControls.put(port.getName(), blockPortControl);
			inputPorts.getChildren().add(blockPortControl);
			inputPorts.getChildren().add(new Spacer());
		}

		ports = blockType.getOutputPorts();
		outputPorts.getChildren().add(new Spacer());
		for (BlockPort port : ports) {
			blockPortControl = new BlockPortControl(this, port, false);
			blockPortControls.put(port.getName(), blockPortControl);
			outputPorts.getChildren().add(blockPortControl);
			outputPorts.getChildren().add(new Spacer());
		}

		double x = block.getX();
		double y = block.getY();
		block.xProperty().bindBidirectional(this.layoutXProperty());
		block.yProperty().bindBidirectional(this.layoutYProperty());
		block.setX(x);
		block.setY(y);
	}

	public Block getBlock() {
		return block;
	}

	public BlockPortControl getPortControl(String port) {
		return blockPortControls.get(port);
	}

	@FXML
	private void onMousePressed(MouseEvent event) {
		if (event.isPrimaryButtonDown()) {
			dragStartX = event.getX();
			dragStartY = event.getY();
			setCursor(Cursor.CLOSED_HAND);
		}
	}

	@FXML
	private void onMouseReleased(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			updateCursor();
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
			this.schemaControl.setChanged(true);
			event.consume();
		}
	}

	public SchemaControl getSchemaControl() {
		return schemaControl;
	}

	private void updateCursor() {
		if (schemaControl.isModeRemove())
			setCursor(null);
		else
			setCursor(Cursor.OPEN_HAND);
	}

	@Override
	public void onRemove() {
		for (BlockPortControl blockPortControl : blockPortControls.values()) {
			blockPortControl.onRemove();
		}
		this.schemaControl.getSchema().removeBlock(block);
	}
}

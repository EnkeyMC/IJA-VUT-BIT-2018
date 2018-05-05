package ija.project.ui.control.schema;

import ija.project.schema.Block;
import ija.project.schema.BlockPort;
import ija.project.schema.BlockType;
import ija.project.ui.control.Spacer;
import ija.project.ui.utils.UIContolLoader;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Displays logical Block
 */
public class BlockControl extends BorderPane implements Removable, Selectable {

	/** Block display name label */
	@FXML
	protected Label displayNameLabel;

	/** Block this control displays */
	private Block block;

	/** Schema control this block is in */
	private SchemaControl schemaControl;

	/** Mapping of child port controls (port name => BlockPortControl) */
	private Map<String, BlockPortControl> blockPortControls;

	/** Indicates the starting x coordinate of mouse drag relative to this Node */
	private double dragStartX;
	/** Indicates the starting y coordinate of mouse drag relative to this Node */
	private double dragStartY;

	/**
	 * Get path to FXML file for this control
	 * @return path to FXML file relative to FXML root
	 */
	public static String getFXMLPath() {
		return "schema/Block.fxml";
	}

	/**
	 * Create BlockControl from BlockType
	 * @param schemaControl parent schema control
	 * @param blockType BlockType
	 */
	public BlockControl(SchemaControl schemaControl, BlockType blockType) {
		super();
		block = new Block(blockType);
		this.schemaControl = schemaControl;

		init(blockType);
	}

	/**
	 * Create BlockControl displaying given block
	 * @param schemaControl parent schema control
	 * @param block Block to display
	 */
	public BlockControl(SchemaControl schemaControl, Block block) {
		super();
		this.schemaControl = schemaControl;
		this.block = block;

		init(block.getBlockType());
	}

	/**
	 * Init BlockControl. Load FXML file, initializes members and sets up children and bindings
	 * @param blockType block's BlockType
	 */
	protected void init(BlockType blockType) {
		UIContolLoader.load(this);

		blockPortControls = new HashMap<>();
		dragStartX = 0;
		dragStartY = 0;
		updateCursor();
		this.getStyleClass().add("schema-block");

		displayNameLabel.setText(blockType.getDisplayName());
		this.schemaControl.toolRemoveSelectedProperty().addListener((observable, oldValue, newValue) -> updateCursor());
		this.schemaControl.readOnlyProperty().addListener((observable, oldValue, newValue) -> {
			updateCursor();
			if (newValue)
				addEventFilter(MouseEvent.MOUSE_PRESSED, SchemaControl.eventConsume);
			else
				removeEventFilter(MouseEvent.MOUSE_PRESSED, SchemaControl.eventConsume);
		});
		updateCursor();

		HBox inputPorts = new HBox(5);
		HBox outputPorts = new HBox(5);
		this.setTop(inputPorts);
		this.setBottom(outputPorts);
		inputPorts.getStyleClass().add("hbox-up");
		outputPorts.getStyleClass().add("hbox-down");

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

	/**
	 * Get Block this control displays
	 * @return Block
	 */
	public Block getBlock() {
		return block;
	}

	/**
	 * Get BlockPortControl by name
	 * @param port port name
	 * @return BlockPortControl
	 */
	public BlockPortControl getPortControl(String port) {
		return blockPortControls.get(port);
	}

	/**
	 * Handles mouse pressed event. Saves mouse coordinates to dragStartX and dragStartY and sets cursor to closed hand
	 * on primary button down.
	 * @param event event data
	 */
	@FXML
	private void onMousePressed(MouseEvent event) {
		if (event.isPrimaryButtonDown()) {
			dragStartX = event.getX();
			dragStartY = event.getY();
			setCursor(Cursor.CLOSED_HAND);
		}
	}

	/**
	 * Handles mouse released event. Updates cursor on primary button click.
	 * @param event event data
	 */
	@FXML
	private void onMouseReleased(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			updateCursor();
		}
	}

	/**
	 * Handles mouse dragged event. Drags block.
	 * @param event event data
	 */
	@FXML
	private void onMouseDragged(MouseEvent event) {
		if (event.isPrimaryButtonDown()) {
			Point2D local = this.getParent().sceneToLocal(event.getSceneX(), event.getSceneY());

			this.setLayoutX(local.getX() - dragStartX);
			this.setLayoutY(local.getY() - dragStartY);
			this.schemaControl.setChanged(true);

			if (local.getX() - dragStartX < 0) {
				this.setLayoutX(0);
			}
			if (local.getY() - dragStartY < 0) {
				this.setLayoutY(0);
			}
			event.consume();
		}
	}

	/**
	 * Get parent schema control
	 * @return schema control
	 */
	public SchemaControl getSchemaControl() {
		return schemaControl;
	}

	/**
	 * Update cursor to default if is remove mode, open hand otherwise
	 */
	private void updateCursor() {
		if (schemaControl.isModeRemove() || schemaControl.isReadOnly())
			setCursor(null);
		else
			setCursor(Cursor.OPEN_HAND);
	}

	/**
	 * Calls onRemove method for all BlockPortControl children, removes itself from schema
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void onRemove() {
		for (BlockPortControl blockPortControl : blockPortControls.values()) {
			blockPortControl.onRemove();
		}
		if (this.schemaControl.getSelectionModel().getSelectedNode() == this)
			this.schemaControl.getSelectionModel().setSelectedNode(null);
		this.schemaControl.getSchema().removeBlock(block);
	}

	/**
	 * Adds style class SELECTED_CLASS
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void onSelected() {
		this.getStyleClass().add(Selectable.SELECTED_CLASS);
	}

	/**
	 * Removes style class SELECTED_CLASS
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void onDeselected() {
		this.getStyleClass().remove(Selectable.SELECTED_CLASS);
	}
}

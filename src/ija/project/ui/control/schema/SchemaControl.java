package ija.project.ui.control.schema;

import ija.project.exception.ApplicationException;
import ija.project.processor.Processor;
import ija.project.register.BlockTypeRegister;
import ija.project.schema.*;
import ija.project.ui.utils.UIContolLoader;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Controls schema
 */
public class SchemaControl extends VBox {

	/** Pane for displaying schema */
	@FXML
	private AnchorPane schemaPane;

	/** Button for toggling removal of removable nodes (see {@link Removable}) */
	@FXML
	private ToggleButton toolRemove;

	/** Toolbar object injected from FXML */
	@FXML
	private ToolBar toolbar;

	/** Schema this control displays */
	private Schema schema;
	/** Processor instance for processing schema, null if calculation is not running */
	private Processor processor;

	/** Child block controls mapped by their block ID */
	private Map<Long, BlockControl> blockControls;

	/** Connector handling connections */
	private BlockConnector connector;
	/** Connection line preview */
	private ConnectionLine connectionLinePreview;
	/** Dummy port control for connection preview */
	private DummyBlockPortControl dummyBlockPortControl;

	/** Indicates that the schema was modified since last save */
	private BooleanProperty changed = new SimpleBooleanProperty(false);

	/** Schema selection model */
	private SchemaSelectionModel selectionModel;

	/** Indicates that the schema is read only currently */
	private BooleanProperty readOnly;

	/** Event handler consuming the event */
	public static final EventHandler<Event> eventConsume = event -> event.consume();

	/** Class to added to SchemaControl when readOnly property is toggled */
	private static final String READ_ONLY_CLASS = "readonly";

	/**
	 * Get path to FXML file for this control
	 * @return path to FXML file relative to FXML root
	 */
	public static String getFXMLPath() {
		return "schema/Schema.fxml";
	}

	/**
	 * Create schema control for given Schema
	 * @param schema Schema
	 */
	public SchemaControl(Schema schema) {
		super();
		UIContolLoader.load(this);
		this.schema = schema;
		blockControls = new HashMap<>();
		selectionModel = new SchemaSelectionModel(getSchemaPane());
		readOnly = new SimpleBooleanProperty(false);

		Collection<Block> blocks = schema.getBlockCollection();
		BlockControl blockControl;
		for (Block block : blocks) {
			blockControl = BlockControlFactory.create(block, this);
			addBlockControl(blockControl);
		}

		ConnectionLine connectionLine;
		BlockPortControl blockPortControlOutput;
		BlockPortControl blockPortControlInput;

		for (Block block : blocks) {
			for (Map.Entry<String, Pair<Block, String>> connection : block.getConnections().entrySet()) {
				if (connection.getValue() != null) {
					blockControl = blockControls.get(block.getId());
					blockPortControlOutput = blockControl.getPortControl(connection.getKey());

					if (!blockPortControlOutput.isInput()) {
						blockControl = blockControls.get(connection.getValue().getKey().getId());
						blockPortControlInput = blockControl.getPortControl(connection.getValue().getValue());

						connectionLine = new ConnectionLine(blockPortControlOutput, blockPortControlInput);
						schemaPane.getChildren().add(connectionLine);
					}
				}
			}
		}

		schemaPane.getChildren().addListener((ListChangeListener<Node>) c -> setChanged(true));
		this.sceneProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				newValue.setOnKeyPressed(event -> {
					if (event.getCode() == KeyCode.DELETE) {
						Selectable selectable = selectionModel.getSelectedNode();
						if (selectable instanceof Removable) {
							Removable removable = (Removable) selectable;
							removable.onRemove();
							if (removable instanceof Node)
								schemaPane.getChildren().remove(removable);
						}
					}
				});
			}
		});
	}

	/**
	 * Binds schema display name to given property
	 * @param property property to bind
	 */
	public void bindDisplayNameTo(StringProperty property) {
		property.bind(Bindings.when(changed).then("*").otherwise("").concat(schema.displayNameProperty()));
	}

	/**
	 * Accepts drag event if it has string
	 * @param event event data
	 */
	@FXML
	private void onDragOver(DragEvent event) {
		Dragboard db = event.getDragboard();
		if (db.hasString()) {
			event.acceptTransferModes(TransferMode.COPY);
		}
	}

	/**
	 * Creates new block from data in Dragboard
	 * @param event event data
	 */
	@FXML
	private void onDragDropped(DragEvent event) {
		Dragboard db = event.getDragboard();
		if (db.hasString()) {
			String[] parts = db.getString().split("@", 2);
			BlockType type;
			try {
				type = BlockTypeRegister.getBlockTypeById(parts[0], parts[1]);
			} catch (RuntimeException e) {
				e.printStackTrace();
				event.setDropCompleted(false);
				return;
			}

			BlockControl blockControl;
			blockControl = BlockControlFactory.create(BlockFactory.create(type), this);
			schema.addBlock(blockControl.getBlock());
			addBlockControl(blockControl);
			blockControl.setLayoutX(event.getX() - blockControl.getPrefWidth()/2);
			blockControl.setLayoutY(event.getY() - blockControl.getPrefHeight()/2);

			setChanged(true);
			event.setDropCompleted(true);
		} else {
			event.setDropCompleted(false);
		}
	}

	/**
	 * Handles node removal on primary button click, connection cancellation on secondary button click
	 * @param event event data
	 */
	@FXML
	private void onMouseClicked(MouseEvent event) {
		if (isModeRemove() && event.getButton() == MouseButton.PRIMARY) {
			if (!(event.getTarget() instanceof Node))
				return;

			Node node = (Node) event.getTarget();
			while (!(node instanceof Removable)) {
				node = node.getParent();
				if (node == schemaPane || node == null)
					return;
			}

			((Removable) node).onRemove();
			schemaPane.getChildren().remove(node);
		} else if (event.getButton() == MouseButton.SECONDARY && isBlockConnectingActive()) {
			endConnection(null, null);
		}
	}

	/**
	 * Handles connection line preview moving by moving dummy block port control
	 * @param event event data
	 */
	@FXML
	private void onMouseMoved(MouseEvent event) {
		if (connectionLinePreview != null) {
			dummyBlockPortControl.setLayoutX(event.getX());
			dummyBlockPortControl.setLayoutY(event.getY());
		}
	}

	/**
	 * Initialize Processor if not initialized
	 * @return returns new Processor instance if not initialized, old one otherwise
	 */
	private Processor initProcessor() {
		if (this.processor == null) {
			setReadOnly(true);
			this.processor = new Processor(schema);
		}
		return this.processor;
	}

	/**
	 * Handle stepped calculation
	 * @param event event data
	 */
	@FXML
	private void calculateStepActionHandler(ActionEvent event) {
		Processor processor = initProcessor();

		Block block = null;
		try {
			block = processor.calculateStep();
		} catch (ApplicationException | ParseCancellationException e) {
			exceptionAlert("Error occurred during calculation", e);
		}

		if (block == null) {
			this.processor = null;
			this.selectionModel.setSelectedNode(null);
			this.setReadOnly(false);
		} else {
			this.selectionModel.setSelectedNode(blockControls.get(block.getId()));
		}
	}

	/**
	 * Handle calculation
	 * @param event event data
	 */
	@FXML
	private void calculateActionHandler(ActionEvent event) {
		Processor processor = initProcessor();
		this.processor = null;

		try {
			processor.calculateAll();
		} catch (ApplicationException | ParseCancellationException e) {
			exceptionAlert("Error occurred during calculation", e);
		} finally {
			setReadOnly(false);
		}
	}

	/**
	 * Handle calculation stop action
	 * @param event event data
	 */
	@FXML
	private void calculationStopActionHandler(ActionEvent event) {
		processor = null;
		setReadOnly(false);
	}

	/**
	 * Add block control to schema pane and to blockControls map
	 * @param blockControl block control
	 */
	protected void addBlockControl(BlockControl blockControl) {
		schemaPane.getChildren().add(blockControl);
		blockControls.put(blockControl.getBlock().getId(), blockControl);
	}

	/**
	 * Start connection from given block and port
	 * @param srcBlock source block
	 * @param srcPort source port
	 */
	public void startConnection(BlockControl srcBlock, BlockPortControl srcPort) {
		connector = new BlockConnector(this, srcBlock, srcPort);

		dummyBlockPortControl = new DummyBlockPortControl();
		dummyBlockPortControl.setLayoutX(srcPort.connectionXProperty().getValue());
		dummyBlockPortControl.setLayoutY(srcPort.connectionYProperty().getValue());
		dummyBlockPortControl.setMouseTransparent(true);

		if (srcPort.isInput())
			connectionLinePreview = new ConnectionLine(dummyBlockPortControl, srcPort);
		else
			connectionLinePreview = new ConnectionLine(srcPort, dummyBlockPortControl);
		connectionLinePreview.setMouseTransparent(true);
		schemaPane.getChildren().add(connectionLinePreview);
	}

	/**
	 * End connection by connecting to given block and port, of one of the parameters is null, connection is canceled.
	 * Connection has to be active! (see {@link SchemaControl#isBlockConnectingActive()})
	 * @param dstBlock destination block or null to cancel connection
	 * @param dstPort destination port or null to cancel connection
	 */
	public void endConnection(BlockControl dstBlock, BlockPortControl dstPort) {
		assert connector != null;
		try {
			if (dstBlock != null && dstPort != null) {
				connector.connect(dstBlock, dstPort);
				setChanged(true);
			} else {
				connector.getSrcPort().setConnectionLine(null);
			}
		} catch (ApplicationException e) {
			exceptionAlert("Cannot connect ports", e);
		} finally {
			schemaPane.getChildren().remove(connectionLinePreview);
			connectionLinePreview = null;
			dummyBlockPortControl = null;
			connector = null;
		}
	}

	/**
	 * Indicates whether user is currently connecting ports
	 * @return is port connecting active
	 */
	public boolean isBlockConnectingActive() {
		return connector != null;
	}

	/**
	 * Get pane for displaying schema nodes
	 * @return schema pane
	 */
	public AnchorPane getSchemaPane() {
		return schemaPane;
	}

	/**
	 * Get Schema
	 * @return Schema
	 */
	public Schema getSchema() {
		return schema;
	}

	/**
	 * Is schema changed since last save
	 * @return is changed
	 */
	public boolean isChanged() {
		return changed.get();
	}

	/**
	 * Get changed property
	 * @return changed property
	 */
	public BooleanProperty changedProperty() {
		return changed;
	}

	/**
	 * Set whether schema has changed since last save
	 * @param changed has changed since last save
	 */
	public void setChanged(boolean changed) {
		this.changed.set(changed);
	}

	/**
	 * Is schema in removing mode
	 * @return is removing mode active
	 */
	public boolean isModeRemove() {
		return toolRemove.isSelected();
	}

	/**
	 * Get tool remove selected property
	 * @return tool remove selected property
	 */
	public BooleanProperty toolRemoveSelectedProperty() {
		return toolRemove.selectedProperty();
	}

	/**
	 * Get schema selection model
	 * @return selection model
	 */
	public SchemaSelectionModel getSelectionModel() {
		return selectionModel;
	}

	/**
	 * Show user alert with message from exception
	 * @param header alert header
	 * @param e exception
	 */
	private void exceptionAlert(String header, Exception e) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error occurred");
		alert.setHeaderText(header);
		alert.setContentText(e.getMessage());
		alert.showAndWait();
	}

	/**
	 * Is schema read only
	 * @return is read only
	 */
	public boolean isReadOnly() {
		return readOnly.get();
	}

	/**
	 * Set if schema is read only, when set to true, all events that allow to change schema are consumed.
	 * @param readOnly is read only
	 */
	public void setReadOnly(boolean readOnly) {
		if (readOnly != this.readOnly.get()) {
			if (readOnly) {
				getStyleClass().add(READ_ONLY_CLASS);
				toolRemove.setDisable(true);
				schemaPane.addEventFilter(DragEvent.ANY, eventConsume);
				schemaPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, eventConsume);
			} else {
				getStyleClass().remove(READ_ONLY_CLASS);
				toolRemove.setDisable(false);
				schemaPane.removeEventFilter(DragEvent.ANY, eventConsume);
				schemaPane.removeEventFilter(MouseEvent.MOUSE_DRAGGED, eventConsume);
			}
			this.readOnly.setValue(readOnly);
		}
	}

	/**
	 * Get read only property
	 * @return read only property
	 */
	public BooleanProperty readOnlyProperty() {
		return readOnly;
	}

	/**
	 * Disable schema toolbar
	 */
	public void disableToolbar() {
		toolbar.setDisable(true);
	}
}

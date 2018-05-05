package ija.project.ui.control.schema;

import ija.project.schema.BlockPort;
import ija.project.schema.Schema;
import ija.project.ui.utils.UIContolLoader;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

/**
 * Displays block port
 */
public class BlockPortControl extends AnchorPane {

	/** Parent block control */
	private BlockControl blockControl;
	/** Ports BlockPort */
	private BlockPort blockPort;

	/** X coordinate for connection */
	private ReadOnlyDoubleProperty connectionX;
	/** Y coordinate for connection */
	private ReadOnlyDoubleProperty connectionY;

	/** Connection line, null if not connected */
	private ConnectionLine connectionLine;

	/** True if this port is input port, false if output */
	private boolean input;

	/**
	 * Get path to FXML file for this control
	 * @return path to FXML file relative to FXML root
	 */
	public static String getFXMLPath() {
		return "schema/BlockPort.fxml";
	}

	public BlockPortControl() {

	}

	public BlockPortControl(BlockControl blockControl, BlockPort port, boolean input) {
		super();
		UIContolLoader.load(this);
		this.blockControl = blockControl;
		this.blockPort = port;
		this.input = input;

		this.getStyleClass().add("port");
		if (input)
			this.getStyleClass().add("port-input");
		else
			this.getStyleClass().add("port-output");

		Tooltip tooltip = new Tooltip(port.getName() + " (" + port.getType().getDisplayName() + ")");
		Tooltip.install(this, tooltip);

		updateCursor();
		blockControl.getSchemaControl().toolRemoveSelectedProperty().addListener((observable, oldValue, newValue) -> updateCursor());
		blockControl.getSchemaControl().readOnlyProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				this.addEventFilter(MouseEvent.MOUSE_CLICKED, SchemaControl.eventConsume);
			} else {
				this.removeEventFilter(MouseEvent.MOUSE_CLICKED, SchemaControl.eventConsume);
			}
			updateCursor();
		});
	}

	@FXML
	private void onMouseClicked(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY) && !blockControl.getSchemaControl().isModeRemove()) {
			if (this.blockControl.getSchemaControl().isBlockConnectingActive()) {
				this.blockControl.getSchemaControl().endConnection(this.blockControl, this);
			} else {
				this.blockControl.getSchemaControl().startConnection(this.blockControl, this);
			}
			event.consume();
		}
	}

	public BlockPort getBlockPort() {
		return blockPort;
	}

	public BlockControl getBlockControl() {
		return blockControl;
	}

	public ReadOnlyDoubleProperty connectionXProperty() {
		if (connectionX == null) {
			DoubleProperty property = new SimpleDoubleProperty();
			property.bind(
				this.getParent().layoutXProperty()
					.add(this.blockControl.layoutXProperty())
					.add(this.layoutXProperty())
					.add(this.widthProperty()
						.divide(2)));
			connectionX = property;
		}
		return connectionX;
	}

	public ReadOnlyDoubleProperty connectionYProperty() {
		if (connectionY == null) {
			DoubleProperty property = new SimpleDoubleProperty();
			property.bind(
				this.getParent().layoutYProperty()
					.add(this.blockControl.layoutYProperty())
					.add(this.layoutYProperty())
					.add(this.heightProperty()
						.multiply(input ? 0 : 1)));
			connectionY = property;
		}
		return connectionY;
	}

	public boolean isInput() {
		return input;
	}

	private void updateCursor() {
		if (blockControl.getSchemaControl().isModeRemove() || blockControl.getSchemaControl().isReadOnly())
			setCursor(null);
		else
			setCursor(Cursor.CROSSHAIR);
	}

	public void setConnectionLine(ConnectionLine connectionLine) {
		this.connectionLine = connectionLine;
	}

	public void onRemove() {
		if (connectionLine != null) {
			blockControl.getSchemaControl().getSchemaPane().getChildren().remove(connectionLine);
			connectionLine.onRemove();
		}
	}
}

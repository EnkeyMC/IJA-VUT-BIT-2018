package ija.project.ui.control.schema;

import ija.project.schema.BlockPort;
import ija.project.ui.utils.UIContolLoader;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class BlockPortControl extends AnchorPane {

	private BlockControl blockControl;
	private BlockPort blockPort;

	private ReadOnlyDoubleProperty connectionX;
	private ReadOnlyDoubleProperty connectionY;

	private ConnectionLine connectionLine;

	private boolean input;

	public static String getFXMLPath() {
		return "schema/BlockPort.fxml";
	}

	public BlockPortControl(BlockControl blockControl, BlockPort port, boolean input) {
		super();
		UIContolLoader.load(this);
		this.blockControl = blockControl;
		this.blockPort = port;
		this.input = input;

		Tooltip tooltip = new Tooltip(port.getName() + " (" + port.getType().getDisplayName() + ")");
		Tooltip.install(this, tooltip);

		if (input)
			this.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
		else
			this.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));

		updateCursor();
		blockControl.getSchemaControl().toolRemoveSelectedProperty().addListener((observable, oldValue, newValue) -> updateCursor());
	}

	@FXML
	private void onMouseClicked(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY) && !blockControl.getSchemaControl().isModeRemove()) {
			if (this.blockControl.getSchemaControl().isBlockConnectingActive()) {
				if (input)
					this.blockControl.getSchemaControl().endConnection(this.blockControl, this);
			} else {
				if (!input)
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
		if (blockControl.getSchemaControl().isModeRemove())
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

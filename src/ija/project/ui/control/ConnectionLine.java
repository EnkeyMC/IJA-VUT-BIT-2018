package ija.project.ui.control;

import ija.project.ui.control.schema.BlockPortControl;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;

public class ConnectionLine extends Path {

	private BlockPortControl outputPort;
	private BlockPortControl inputPort;

	private Pane parent;

	private static final int arrowX = 7;
	private static final int arrowY = 10;

	public ConnectionLine(Pane parent, BlockPortControl outputPort, BlockPortControl inputPort) {
		super();
		this.outputPort = outputPort;
		this.inputPort = inputPort;
		this.parent = parent;

		this.setStrokeWidth(3);
		this.layoutXProperty().bind(outputPort.connectionXProperty());
		this.layoutYProperty().bind(outputPort.connectionYProperty());
		this.setManaged(false);
		bindLines();

		this.setOnMouseClicked(event -> {
			if (event.getButton().equals(MouseButton.SECONDARY)) {
				ConnectionLine line = (ConnectionLine) event.getSource();
				line.removeConnection();
				event.consume();
			}
		});
	}

	private void bindLines() {
		ObservableList<PathElement> elements = getElements();
		elements.add(new MoveTo(0,0));

		VLineTo vline = new VLineTo();
		DoubleBinding midY = inputPort.connectionYProperty().subtract(outputPort.connectionYProperty()).divide(2);
		vline.yProperty().bind(midY);
		elements.add(vline);

		HLineTo hline = new HLineTo();
		hline.xProperty().bind(inputPort.connectionXProperty().subtract(outputPort.connectionXProperty()));
		elements.add(hline);

		vline = new VLineTo();
		vline.yProperty().bind(midY.add(midY));
		elements.add(vline);

		MoveTo moveTo = new MoveTo();
		moveTo.xProperty().bind(hline.xProperty());
		moveTo.yProperty().bind(vline.yProperty());
		elements.add(moveTo);

		LineTo arrowLine = new LineTo();
		arrowLine.xProperty().bind(hline.xProperty().subtract(arrowX));
		arrowLine.yProperty().bind(vline.yProperty().subtract(arrowY));
		elements.add(arrowLine);

		moveTo = new MoveTo();
		moveTo.xProperty().bind(hline.xProperty());
		moveTo.yProperty().bind(vline.yProperty());
		elements.add(moveTo);

		arrowLine = new LineTo();
		arrowLine.xProperty().bind(hline.xProperty().add(arrowX));
		arrowLine.yProperty().bind(vline.yProperty().subtract(arrowY));
		elements.add(arrowLine);
	}

	private void removeConnection() {
		this.parent.getChildren().remove(this);
		outputPort.getBlockControl().getBlock().disconnectPort(outputPort.getBlockPort().getName());
		inputPort.getBlockControl().getBlock().disconnectPort(inputPort.getBlockPort().getName());
	}
}

package ija.project.ui.control.schema;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.NumberBinding;
import javafx.collections.ObservableList;
import javafx.scene.shape.*;

public class ConnectionLine extends Path implements Removable {

	private BlockPortControl outputPort;
	private BlockPortControl inputPort;

	private static final int arrowX = 7;
	private static final int arrowY = 10;

	public ConnectionLine(BlockPortControl outputPort, BlockPortControl inputPort) {
		super();
		this.outputPort = outputPort;
		this.inputPort = inputPort;

		this.setStrokeWidth(3);
		this.layoutXProperty().bind(outputPort.connectionXProperty());
		this.layoutYProperty().bind(outputPort.connectionYProperty());
		this.setManaged(false);
		bindLines();
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

		NumberBinding sign = Bindings.when(outputPort.connectionYProperty().greaterThan(inputPort.connectionYProperty()))
			.then(-1).otherwise(1);

		LineTo arrowLine = new LineTo();
		arrowLine.xProperty().bind(hline.xProperty().subtract(arrowX));
		arrowLine.yProperty().bind(vline.yProperty().subtract(sign.multiply(arrowY)));
		elements.add(arrowLine);

		moveTo = new MoveTo();
		moveTo.xProperty().bind(hline.xProperty());
		moveTo.yProperty().bind(vline.yProperty());
		elements.add(moveTo);

		arrowLine = new LineTo();
		arrowLine.xProperty().bind(hline.xProperty().add(arrowX));
		arrowLine.yProperty().bind(vline.yProperty().subtract(sign.multiply(arrowY)));
		elements.add(arrowLine);
	}

	@Override
	public void onRemove() {
		outputPort.getBlockControl().getBlock().disconnectPort(outputPort.getBlockPort().getName());
		inputPort.getBlockControl().getBlock().disconnectPort(inputPort.getBlockPort().getName());
	}
}

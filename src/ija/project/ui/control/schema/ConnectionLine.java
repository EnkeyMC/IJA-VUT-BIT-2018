package ija.project.ui.control.schema;

import ija.project.schema.Block;
import ija.project.schema.TypeValues;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.NumberBinding;
import javafx.collections.ObservableList;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;

import java.util.Map;

public class ConnectionLine extends Path implements Removable {

	private BlockPortControl outputPort;
	private BlockPortControl inputPort;

	private static final int arrowX = 7;
	private static final int arrowY = 10;

	public ConnectionLine(BlockPortControl outputPort, BlockPortControl inputPort) {
		super();
		this.outputPort = outputPort;
		this.inputPort = inputPort;

		this.setStrokeWidth(2);
		this.setStroke(Color.color(0.4,0.4,0.4));
		this.layoutXProperty().bind(outputPort.connectionXProperty());
		this.layoutYProperty().bind(outputPort.connectionYProperty());
		this.setManaged(false);
		bindLines();

		Tooltip tooltip = new Tooltip(getValueString());
		tooltip.activatedProperty().addListener((observable, oldValue, newValue) -> tooltip.setText(getValueString()));
		Tooltip.install(this, tooltip);
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

	private String getValueString() {
		StringBuilder builder = new StringBuilder();
		Block block = outputPort.getBlockControl().getBlock();
		TypeValues values = block.getOutputPortValues().get(outputPort.getBlockPort().getName());
		for (Map.Entry<String, Double> value : values.getValuesMap().entrySet()) {
			builder.append(value.getKey()).append(": ");
			if (value.getValue() == null)
				builder.append("null");
			else
				builder.append(value.getValue());
		}
		return builder.toString();
	}

	@Override
	public void onRemove() {
		outputPort.getBlockControl().getBlock().disconnectPort(outputPort.getBlockPort().getName());
		inputPort.getBlockControl().getBlock().disconnectPort(inputPort.getBlockPort().getName());
	}
}

package ija.project.ui.control.schema;

import javafx.scene.shape.Line;

public class BlockConnector {

	private BlockPortControl srcPort;
	private BlockControl srcBlock;

	private SchemaControl schemaControl;

	public BlockConnector(SchemaControl control, BlockControl srcBlock, BlockPortControl srcPort) {
		this.srcPort = srcPort;
		this.srcBlock = srcBlock;
		this.schemaControl = control;
	}

	public boolean connect(BlockControl dstBlock, BlockPortControl dstPort) {
		if (dstBlock == srcBlock)
			return false;

		srcBlock.getBlock().connectTo(
			srcPort.getBlockPort().getName(),
			dstBlock.getBlock(),
			dstPort.getBlockPort().getName());

		Line line = new Line();
		line.startXProperty().bind(srcPort.connectionXProperty());
		line.startYProperty().bind(srcPort.connectionYProperty());
		line.endXProperty().bind(dstPort.connectionXProperty());
		line.endYProperty().bind(dstPort.connectionYProperty());
		schemaControl.getSchemaPane().getChildren().add(line);
		return true;
	}
}

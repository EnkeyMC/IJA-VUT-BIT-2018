package ija.project.ui.control.schema;

import javafx.scene.shape.Line;

public class BlockConnector {

	private String srcPort;
	private BlockControl srcBlock;

	private SchemaControl schemaControl;

	public BlockConnector(SchemaControl control, BlockControl srcBlock, String srcPort) {
		this.srcPort = srcPort;
		this.srcBlock = srcBlock;
		this.schemaControl = control;
	}

	public boolean connect(BlockControl dstBlock, String dstPort) {
		if (dstBlock == srcBlock)
			return false;

		srcBlock.getBlock().connectTo(srcPort, dstBlock.getBlock(), dstPort);

		Line line = new Line();
		line.startXProperty().bind(srcBlock.centerXProperty());
		line.startYProperty().bind(srcBlock.centerYProperty());
		line.endXProperty().bind(dstBlock.centerXProperty());
		line.endYProperty().bind(dstBlock.centerYProperty());
		schemaControl.getSchemaPane().getChildren().add(line);
		return true;
	}
}

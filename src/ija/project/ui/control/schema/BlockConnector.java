package ija.project.ui.control.schema;

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
			dstPort.getBlockPort().getName()
		);

		ConnectionLine line;
		if (srcPort.isInput())
			line = new ConnectionLine(dstPort, srcPort);
		else
			line = new ConnectionLine(srcPort, dstPort);
		schemaControl.getSchemaPane().getChildren().add(line);
		return true;
	}
}

package ija.project.ui.control.schema;

import ija.project.exception.ApplicationException;

public class BlockConnector {

	private BlockPortControl srcPort;
	private BlockControl srcBlock;

	private SchemaControl schemaControl;

	public BlockConnector(SchemaControl control, BlockControl srcBlock, BlockPortControl srcPort) {
		this.srcPort = srcPort;
		this.srcBlock = srcBlock;
		this.schemaControl = control;
	}

	public void connect(BlockControl dstBlock, BlockPortControl dstPort) {
		if (dstBlock == srcBlock)
			throw new ApplicationException("Cannot connect ports on the same block!");

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
		srcPort.setConnectionLine(line);
		dstPort.setConnectionLine(line);
		schemaControl.getSchemaPane().getChildren().add(line);
	}

	public BlockControl getSrcBlock() {
		return srcBlock;
	}

	public BlockPortControl getSrcPort() {
		return srcPort;
	}
}

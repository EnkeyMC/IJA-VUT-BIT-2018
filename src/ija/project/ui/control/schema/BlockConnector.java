package ija.project.ui.control.schema;

import ija.project.exception.ApplicationException;

/**
 * Handles block connecting
 */
public class BlockConnector {

	/** Source port control */
	private BlockPortControl srcPort;
	/** Source block control */
	private BlockControl srcBlock;

	/** SchemaControl to add ConnectionLine to */
	private SchemaControl schemaControl;

	/**
	 * Create BlockConnector starting from given source block and port control
	 * @param control SchemaControl for adding resulting ConnectionLine
	 * @param srcBlock source BlocControl
	 * @param srcPort source BlockPortControl
	 */
	public BlockConnector(SchemaControl control, BlockControl srcBlock, BlockPortControl srcPort) {
		this.srcPort = srcPort;
		this.srcBlock = srcBlock;
		this.schemaControl = control;
	}

	/**
	 * Connect destination block and port to source block and port
	 * @param dstBlock destination BlockControl
	 * @param dstPort destination BlockPortControl
	 */
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
		schemaControl.getSchemaPane().getChildren().add(line);
	}

	/**
	 * Get source port for connection
	 * @return source port control
	 */
	public BlockPortControl getSrcPort() {
		return srcPort;
	}
}

package ija.project.schema;


public class BlockConnection {
	private BlockPort inputPort;
	private BlockPort outputPort;

	/**
	 * Create blank BlockConnection
	 */
	public BlockConnection() {

	}

	/**
	 * Create block connection
	 * @param inputPort block input port
	 * @param outputPort block output port
	 */
	public BlockConnection(BlockPort inputPort, BlockPort outputPort) {

	}

	public BlockPort getInputPort() {
		return inputPort;
	}

	public void setInputPort(BlockPort inputPort) {
		this.inputPort = inputPort;
	}

	public BlockPort getOutputPort() {
		return outputPort;
	}

	public void setOutputPort(BlockPort outputPort) {
		this.outputPort = outputPort;
	}
}

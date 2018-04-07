package ija.project.schema;


import ija.project.utils.XMLBuilder;
import ija.project.utils.XMLRepresentable;

public class BlockConnection implements XMLRepresentable {
	private BlockPort inputPort;
	private BlockPort outputPort;

	/**
	 * Create blank BlockConnection
	 */
	public BlockConnection() {
		inputPort = null;
		outputPort = null;
	}

	/**
	 * Create block connection
	 * @param inputPort block input port
	 * @param outputPort block output port
	 */
	public BlockConnection(BlockPort inputPort, BlockPort outputPort) {
		this.inputPort = inputPort;
		this.outputPort = outputPort;
	}

	/**
	 * Get input port
	 * @return input port
	 */
	public BlockPort getInputPort() {
		return inputPort;
	}

	/**
	 * Set input port
	 * @param inputPort input port to set
	 */
	public void setInputPort(BlockPort inputPort) {
		this.inputPort = inputPort;
	}

	/**
	 * Get output port
	 * @return output port
	 */
	public BlockPort getOutputPort() {
		return outputPort;
	}

	/**
	 * Set output port
	 * @param outputPort output port to set
	 */
	public void setOutputPort(BlockPort outputPort) {
		this.outputPort = outputPort;
	}

	@Override
	public void fromXML(XMLBuilder xmlDom) {

	}

	@Override
	public void toXML(XMLBuilder xmlDom) {

	}
}

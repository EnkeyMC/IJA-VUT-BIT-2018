package ija.project.schema;

import ija.project.exception.ApplicationException;
import ija.project.exception.XMLParsingException;
import ija.project.register.BlockTypeRegister;
import ija.project.xml.XmlActiveNode;
import ija.project.xml.XmlRepresentable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Block that can be placed in schema. Ports are defined by BlockType.
 */
public class Block implements XmlRepresentable {

	/** Block id */
	private long id;
	/**
	 * BlockType of this Block
	 */
	private BlockType blockType;
	/**
	 * Input ports to TypeValues mapping
	 */
	private HashMap<String, TypeValues> inputPorts;
	/**
	 * Output ports to TypeValues mapping
	 */
	private HashMap<String, TypeValues> outputPorts;
	/**
	 * Blocks connected on ports
	 */
	private HashMap<String, Pair<Block, String>> connections;

	/**
	 * X coordinate in schema
	 */
	private DoubleProperty x;
	/**
	 * Y coordinate in schema
	 */
	private DoubleProperty y;

	/**
	 * Construct blank object
	 */
	public Block() {
		initEmpty();
	}

	/**
	 * Construct object from given block types
	 * @param blockType block types
	 */
	public Block(BlockType blockType) {
		initEmpty();
		initFromBlockType(blockType);
		this.id = 0;
	}

	/**
	 * Init empty block object
	 */
	private void initEmpty() {
		this.blockType = null;
		this.inputPorts = new HashMap<>();
		this.outputPorts = new HashMap<>();
		this.connections = new HashMap<>();
		x = new SimpleDoubleProperty(0);
		y = new SimpleDoubleProperty(0);
	}

	/**
	 * Init block from block types
	 * @param blockType block types
	 */
	private void initFromBlockType(BlockType blockType) {
		this.blockType = blockType;

		ArrayList<BlockPort> ports = blockType.getInputPorts();
		for (BlockPort port : ports) {
			inputPorts.put(port.getName(), new TypeValues(port.getType()));
			connections.put(port.getName(), null);
		}

		ports = blockType.getOutputPorts();
		for (BlockPort port : ports) {
			outputPorts.put(port.getName(), new TypeValues(port.getType()));
			connections.put(port.getName(), null);
		}
	}

	@Override
	public void fromXML(XmlActiveNode xmlDom) throws XMLParsingException {
		xmlDom.getCurrentNode("block");
		String blockTypeCat = xmlDom.getAttribute("block-type-cat");
		String blockTypeId = xmlDom.getAttribute("block-type-id");
		BlockType blockType = BlockTypeRegister.getBlockTypeById(blockTypeCat, blockTypeId);
		initFromBlockType(blockType);

		this.x.setValue(Integer.valueOf(xmlDom.getAttribute("x")));
		this.y.setValue(Integer.valueOf(xmlDom.getAttribute("y")));
		this.setId(Long.valueOf(xmlDom.getAttribute("id")));
	}

	@Override
	public void toXML(XmlActiveNode xmlDom) {
		xmlDom.createChildElement("block");
		xmlDom.setAttribute("block-type-cat", blockType.getCategory());
		xmlDom.setAttribute("block-type-id", blockType.getId());
		xmlDom.setAttribute("x", Integer.toString((int) x.get()));
		xmlDom.setAttribute("y", Integer.toString((int) y.get()));
		xmlDom.setAttribute("id", Long.toString(id));
		xmlDom.parentNode();
	}

	/**
	 * Connect block's port to another block's port
	 * @param srcPort port from
	 * @param dstBlock block to
	 * @param dstPort port to
	 */
	public void connectTo(String srcPort, Block dstBlock, String dstPort) throws ApplicationException {
		if (!connections.containsKey(srcPort))
			throw new ApplicationException("Block '" + blockType.getId() + "' does not contain port '" + srcPort + "'");
		if (!dstBlock.connections.containsKey(dstPort))
			throw new ApplicationException("Block '" + dstBlock.blockType.getId() + "' does not contain port '" + dstPort + "'");
		if (connections.get(srcPort) != null)
			throw new ApplicationException("Port '" + srcPort + "' is already connected");
		if (dstBlock.connections.get(dstPort) != null)
			throw new ApplicationException("Port '" + dstPort + "' is already connected");

		connections.put(srcPort, new Pair<>(dstBlock, dstPort));
		dstBlock.connections.put(dstPort, new Pair<>(this, srcPort));
	}

	/**
	 * Disconnect input port, does not disconnect port on the other side of the connection
	 * @param portName port name
	 */
	public void disconnectPort(String portName) {
		connections.put(portName, null);
	}

	/**
	 * Block has unconnected output port
	 * @return true if block has unconnected output port, false otherwise
	 */
	public boolean hasUnconnectedOutputPort() {
		for (Map.Entry<String, TypeValues> port : outputPorts.entrySet()) {
			if (connections.get(port.getKey()) == null)
				return true;
		}
		return false;
	}

	/**
	 * Is given port connected
	 * @param port port name
	 * @return true if connected, false otherwise
	 */
	public boolean isConnected(String port) {
		return connections.get(port) != null;
	}

	/**
	 * Get block x coordinate
	 * @return x
	 */
	public double getX() {
		return x.get();
	}

	/**
	 * Get x coordinate property
	 * @return x property
	 */
	public DoubleProperty xProperty() {
		return x;
	}

	/**
	 * Set block x coordinate
	 * @param x x coordinate
	 */
	public void setX(double x) {
		this.x.set(x);
	}

	/**
	 * Get block y coordinate
	 * @return y coordinate
	 */
	public double getY() {
		return y.get();
	}

	/**
	 * Get y coordinate property
	 * @return y property
	 */
	public DoubleProperty yProperty() {
		return y;
	}

	/**
	 * Set block y coordinate
	 * @param y coordinate
	 */
	public void setY(double y) {
		this.y.set(y);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public HashMap<String, Pair<Block, String>> getConnections() {
		return connections;
	}

	public BlockType getBlockType() {
		return blockType;
	}
}

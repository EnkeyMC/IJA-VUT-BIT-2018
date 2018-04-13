package ija.project.schema;

import ija.project.exception.ApplicationException;
import ija.project.utils.XMLBuilder;
import ija.project.utils.XMLRepresentable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Block that can be placed in schema. Ports are defined by BlockType.
 */
public class Block implements XMLRepresentable {

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
	private HashMap<String, Block> connections;

	/**
	 * X coordinate in schema
	 */
	private DoubleProperty x;
	/**
	 * Y coordinate in schema
	 */
	private DoubleProperty y;

	public Block() {
		initEmpty();
	}

	public Block(BlockType blockType) {
		initEmpty();
		initFromBlockType(blockType);
	}

	private void initEmpty() {
		this.blockType = null;
		this.inputPorts = new HashMap<>();
		this.outputPorts = new HashMap<>();
		this.connections = new HashMap<>();
		x = new SimpleDoubleProperty(0);
		y = new SimpleDoubleProperty(0);
	}

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
	public void fromXML(XMLBuilder xmlDom) {

	}

	@Override
	public void toXML(XMLBuilder xmlDom) {

	}

	public void connectTo(String srcPort, Block dstBlock, String dstPort) {
		if (!connections.containsKey(srcPort))
			throw new ApplicationException("Block '" + blockType.getId() + "' does not contain port '" + srcPort + "'");
		if (!dstBlock.connections.containsKey(dstPort))
			throw new ApplicationException("Block '" + dstBlock.blockType.getId() + "' does not contain port '" + dstPort + "'");

		connections.put(srcPort, dstBlock);
		dstBlock.connections.put(dstPort, this);
	}

	public boolean hasUnconnectedOutputPort() {
		for (Map.Entry<String, TypeValues> port : outputPorts.entrySet()) {
			if (connections.get(port.getKey()) == null)
				return true;
		}
		return false;
	}

	public double getX() {
		return x.get();
	}

	public DoubleProperty xProperty() {
		return x;
	}

	public void setX(double x) {
		this.x.set(x);
	}

	public double getY() {
		return y.get();
	}

	public DoubleProperty yProperty() {
		return y;
	}

	public void setY(double y) {
		this.y.set(y);
	}
}

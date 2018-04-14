package ija.project.schema;

import ija.project.utils.XMLBuilder;
import ija.project.utils.XMLRepresentable;

/**
 * Block's port definition
 */
public class BlockPort implements XMLRepresentable {

	/**
	 * Reference to parent BlockType
	 */
	private BlockType blockType;

	/**
	 * Name of port
	 */
	private String name;

	/**
	 * Data type of port
	 */
	private Type type;

	/**
	 * Construct object with given name and type
	 * @param blockType parent BlockType
	 * @param name port name
	 * @param type port data type
	 */
	public BlockPort(BlockType blockType, String name, Type type) {
		this.blockType = blockType;
		this.name = name;
		this.type = type;
	}

	/**
	 * Get parent block type
	 * @return parent block type
	 */
	public BlockType getBlockType() {
		return blockType;
	}

	/**
	 * Get port name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get port data type
	 * @return type
	 */
	public Type getType() {
		return type;
	}

	@Override
	public void fromXML(XMLBuilder xmlDom) {

	}

	@Override
	public void toXML(XMLBuilder xmlDom) {

	}
}

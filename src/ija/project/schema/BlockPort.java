package ija.project.schema;

import ija.project.exception.XMLParsingException;
import ija.project.register.TypeRegister;
import ija.project.xml.XmlActiveNode;
import ija.project.xml.XmlRepresentable;

/**
 * Block's port definition
 */
public class BlockPort implements XmlRepresentable {

	/**
	 * Reference to parent BlockType
	 */
	private BlockType blockType;

	/**
	 * Name of port
	 */
	private String name;

	/**
	 * Data types of port
	 */
	private Type type;

	/**
	 * Construct blank block port
	 */
	public BlockPort() {}

	/**
	 * Construct block port with reference to block types
	 * @param blockType parent block types
	 */
	public BlockPort(BlockType blockType) {
		this.blockType = blockType;
	}

	/**
	 * Construct object with given name and types
	 * @param blockType parent BlockType
	 * @param name port name
	 * @param type port data types
	 */
	public BlockPort(BlockType blockType, String name, Type type) {
		this.blockType = blockType;
		this.name = name;
		this.type = type;
	}

	/**
	 * Get parent block types
	 * @return parent block types
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
	 * Get port data types
	 * @return types
	 */
	public Type getType() {
		return type;
	}

	@Override
	public void fromXML(XmlActiveNode xmlDom) throws XMLParsingException {
		xmlDom.getCurrentNode("blockport");
		this.name = xmlDom.getAttribute("name");
		this.type = TypeRegister.getTypeById(xmlDom.getAttribute("type"));
	}

	@Override
	public void toXML(XmlActiveNode xmlDom) {
		xmlDom.createChildElement("blockport");
		xmlDom.setAttribute("name", this.name);
		xmlDom.setAttribute("type", this.type.getId());
		xmlDom.parentNode();
	}
}

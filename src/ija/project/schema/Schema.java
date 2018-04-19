package ija.project.schema;

import ija.project.exception.XMLParsingException;
import ija.project.xml.XmlActiveNode;
import ija.project.xml.XmlRepresentable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.util.ArrayList;

/**
 * Schema represents block graph
 */
public class Schema implements XmlRepresentable {

	/** Last generated ID */
	private long lastID;
	/**
	 * Schema file
	 */
	private File file;
	/**
	 * Schema display name
	 */
	private StringProperty displayName;
	/**
	 * Schema blocks
	 */
	private ArrayList<Block> blocks;

	/**
	 * Construct blank schema
	 */
	public Schema() {
		this.lastID = 0;
		this.blocks = new ArrayList<>();
		displayName = new SimpleStringProperty("Untitled");
	}

	/**
	 * Get all blocks in schema
	 * @return list of blocks
	 */
	public ArrayList<Block> getBlocks() {
		return this.blocks;
	}

	/**
	 * Add block to schema
	 * @param block block
	 */
	public void addBlock(Block block) {
		block.setId(this.generateID());
		this.blocks.add(block);
	}

	@Override
	public void fromXML(XmlActiveNode xmlDom) throws XMLParsingException {
		xmlDom.getCurrentNode("schema");

		Block block;
		for (XmlActiveNode blocksNode : xmlDom.childIterator()) {
			if (blocksNode.getTag().equals("blocks")) {
				for (XmlActiveNode blockNode : blocksNode.childIterator()) {
					block = new Block();
					block.fromXML(blockNode);
					this.blocks.add(block);
				}
			}
		}
	}

	@Override
	public void toXML(XmlActiveNode xmlDom) {
		xmlDom.createChildElement("schema");

		xmlDom.createChildElement("blocks");
		for (Block block : blocks) {
			block.toXML(xmlDom);
		}
		xmlDom.parentNode();

		xmlDom.parentNode();
	}

	public String getDisplayName() {
		return displayName.get();
	}

	public StringProperty displayNameProperty() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName.set(displayName);
	}

	private long generateID() {
		return ++lastID;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
		this.displayName.setValue(file.getName());
	}
}

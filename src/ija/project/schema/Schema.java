package ija.project.schema;

import ija.project.register.BlockTypeRegister;
import ija.project.register.ComponentLoader;
import ija.project.exception.XMLParsingException;
import ija.project.xml.XmlActiveNode;
import ija.project.xml.XmlRepresentable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;

import java.io.File;
import java.util.*;

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
	private Map<Long, Block> blocks;

	/**
	 * Construct blank schema
	 */
	public Schema() {
		this.lastID = 0;
		this.blocks = new HashMap<>();
		displayName = new SimpleStringProperty("Untitled");
	}

	/**
	 * Get all blocks in schema
	 * @return list of blocks
	 */
	public Map<Long, Block> getBlocks() {
		return this.blocks;
	}

	/**
	 * Get all blocks in schema as Collection
	 * @return all blocks in schema
	 */
	public Collection<Block> getBlockCollection() {
		return this.blocks.values();
	}

	/**
	 * Get block corresponding to the given ID
	 * @return block
	 */
	public Block getBlock(String id) {
		return this.blocks.get(Long.valueOf(id));
	}

	/**
	 * Add block to schema
	 * @param block block
	 */
	public void addBlock(Block block) {
		block.setId(this.generateID());
		this.blocks.put(block.getId(), block);
	}

	/**
	 * Remove block from schema
	 * @param block block to remove
	 */
	public void removeBlock(Block block) {
		blocks.remove(block.getId());
	}

	@Override
	public void fromXML(XmlActiveNode xmlDom) throws XMLParsingException {
		if (xmlDom.getTag().equals("register")) {
			ComponentLoader.loadFromXML(xmlDom);
			xmlDom.nextNode("schema");
		}

		Block block;
		for (XmlActiveNode childNode : xmlDom.childIterator()) {
			if (childNode.getTag().equals("blocks")) {
				for (XmlActiveNode blockNode : childNode.childIterator()) {
					block = BlockFactory.create(blockNode.getTag());
					block.fromXML(blockNode);
					this.blocks.put(block.getId(), block);
				}
			} else if (childNode.getTag().equals("connections")) {
				for (XmlActiveNode connection : childNode.childIterator()) {
					if (connection.getTag().equals("connection")) {
						blocks.get(Long.valueOf(connection.getAttribute("src-block-id"))).connectTo(
							connection.getAttribute("src-port"),
							blocks.get(Long.valueOf(connection.getAttribute("dst-block-id"))),
							connection.getAttribute("dst-port")
						);
					}
				}
			}
		}

		this.lastID = findMaxId();
	}

	@Override
	public void toXML(XmlActiveNode xmlDom) {
		xmlDom.createChildElement("register");
		xmlDom.createChildElement("blocktypes");
		xmlDom.createChildElement("category");
		xmlDom.setAttribute("name", "SchemaBlocks");

		for (Block block: blocks.values()) {
			boolean newType = true;
			for (String builtinType : BlockTypeRegister.builtins) {
				if (builtinType.equals(block.getBlockType().getId())) {
					newType = false;
					break;
				}
			}
			if (!newType)
				continue;

			for (String saved : BlockTypeRegister.savedTypes) {
				if (saved.equals(block.getBlockType().getId())) {
					newType = false;
					break;
				}
			}
			if (newType) {
				block.getBlockType().toXML(xmlDom);
				BlockTypeRegister.savedTypes.add(block.getBlockType().getId());
			}
		}
		xmlDom.parentNode();
		xmlDom.parentNode();
		xmlDom.parentNode();

		xmlDom.createChildElement("schema");
		xmlDom.createChildElement("blocks");
		for (Block block : blocks.values()) {
			block.toXML(xmlDom);
		}
		xmlDom.parentNode();

		xmlDom.createChildElement("connections");
		for (Block block : blocks.values()) {
			for (Map.Entry<String, Pair<Block, String>> connection : block.getConnections().entrySet()) {
				if (connection.getValue() != null && !block.isInputPort(connection.getKey())) {
					xmlDom.createChildElement("connection");
					xmlDom.setAttribute("src-block-id", Long.toString(block.getId()));
					xmlDom.setAttribute("src-port", connection.getKey());
					xmlDom.setAttribute("dst-block-id", Long.toString(connection.getValue().getKey().getId()));
					xmlDom.setAttribute("dst-port", connection.getValue().getValue());
					xmlDom.parentNode();
				}
			}
		}
		xmlDom.parentNode();

		xmlDom.parentNode();
	}

	/**
	 * Find the highest block ID in schema
	 * @return max ID
	 */
	private long findMaxId() {
		long max = 0;
		for (Block block : this.getBlockCollection()) {
			if (block.getId() > max) {
				max = block.getId();
			}
		}
		return max;
	}

	/**
	 * Get schema display name (file name without path)
	 * @return display name
	 */
	public String getDisplayName() {
		return displayName.get();
	}

	/**
	 * Get display name property
	 * @return display name property
	 */
	public StringProperty displayNameProperty() {
		return displayName;
	}

	/**
	 * Set display name
	 * @param displayName display name
	 */
	public void setDisplayName(String displayName) {
		this.displayName.set(displayName);
	}

	/**
	 * Generate next block ID
	 * @return next ID
	 */
	private long generateID() {
		return ++lastID;
	}

	/**
	 * Get schema file
	 * @return File object
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Set file to schema
	 * @param file File object
	 */
	public void setFile(File file) {
		this.file = file;
		this.displayName.setValue(file.getName());
	}
}

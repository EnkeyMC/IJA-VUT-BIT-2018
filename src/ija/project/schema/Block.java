package ija.project.schema;

import java.util.ArrayList;

public class Block {

	private String id;
	private String displayName;
	private ArrayList<BlockPort> inputPorts;
	private ArrayList<BlockPort> outputPorts;

	/**
	 * Create blank block
	 */
	public Block() {

	}

	/**
	 * Load block definition from XML file
	 * @param filename file to parse
	 * @return Block instance
	 */
	public static Block loadFromXML(String filename) {
		return null;
	}

	/**
	 * Save block definition to XML file
	 * @param filename file to save to
	 */
	public static void saveToXML(String filename) {

	}

	/**
	 * Add input port to block with given name
	 * @param name port name
	 */
	public void addInputPort(String name) {

	}

	/**
	 * Add output port to block with given
	 * @param name port name
	 */
	public void addOutputPort(String name) {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}

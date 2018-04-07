package ija.project.schema;

import ija.project.utils.XMLBuilder;

import java.util.ArrayList;

public class Block implements XMLRepresentable {

	private String id;
	private String displayName;
	private ArrayList<InputPort> inputPorts;
	private ArrayList<OutputPort> outputPorts;

	/**
	 * Create blank block
	 */
	public Block() {

	}

	@Override
	public void fromXML(XMLBuilder xmlDom) {

	}

	@Override
	public void toXML(XMLBuilder xmlDom) {

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

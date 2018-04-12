package ija.project.schema;

import ija.project.utils.XMLBuilder;
import ija.project.utils.XMLRepresentable;

import java.util.ArrayList;

public class BlockType implements XMLRepresentable {

	private String id;
	private String displayName;
	private ArrayList<BlockPort> inputPorts;
	private ArrayList<BlockPort> outputPorts;
	private ArrayList<Formula> formulas;

	/**
	 * Create blank block type
	 */
	public BlockType() {
		id = null;
		displayName = null;
		inputPorts = new ArrayList<>();
		outputPorts = new ArrayList<>();
		formulas = new ArrayList<>();
	}

	public BlockType(String id, String displayName) {
		this.id = id;
		this.displayName = displayName;
		inputPorts = new ArrayList<>();
		outputPorts = new ArrayList<>();
		formulas = new ArrayList<>();
	}

	@Override
	public void fromXML(XMLBuilder xmlDom) {

	}

	@Override
	public void toXML(XMLBuilder xmlDom) {

	}

	/**
	 * Add input port to block with given name and type
	 * @param name port name
	 * @param type type of port
	 */
	public void addInputPort(String name, Type type) {
		inputPorts.add(new BlockPort(this, name, type));
	}

	/**
	 * Add output port to block with given name and type
	 * @param name port name
	 * @param type type of port
	 */
	public void addOutputPort(String name, Type type) {
		outputPorts.add(new BlockPort(this, name, type));
	}

	/**
	 * Add new formulas to block
	 * @param f formulas to add
	 */
	public void addFormula(Formula f) {
		formulas.add(f);
	}

	/**
	 * Get certain formulas
	 */
	public ArrayList<Formula> getFormulas() {
		return formulas;
	}

	/**
	 * Get id value
	 * @return id value
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set id value
	 * @param id id value to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get name of block
	 * @return name of block
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Set name of block
	 * @param displayName new name to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public ArrayList<BlockPort> getInputPorts() {
		return this.inputPorts;
	}

	public ArrayList<BlockPort> getOutputPorts() {
		return this.outputPorts;
	}
}

package ija.project.schema;

import ija.project.exception.XMLParsingException;
import ija.project.xml.XmlActiveNode;
import ija.project.xml.XmlRepresentable;

import java.util.ArrayList;

/**
 * Block types defines block's input/output ports, formulas, id and display name
 */
public class BlockType implements XmlRepresentable {

	/**
	 * Block types ID
	 */
	private String id;
	/**
	 * Block type category
	 */
	private String category;
	/**
	 * Display name
	 */
	private String displayName;
	/**
	 * Input port definitions
	 */
	private ArrayList<BlockPort> inputPorts;
	/**
	 * Output port definitions
	 */
	private ArrayList<BlockPort> outputPorts;
	/**
	 * Blocks formulas
	 */
	private ArrayList<Formula> formulas;

	/**
	 * Create blank block types
	 */
	public BlockType() {
		inputPorts = new ArrayList<>();
		outputPorts = new ArrayList<>();
		formulas = new ArrayList<>();
	}

	/**
	 * Create block types with id and display name
	 * @param id id
	 * @param category category
	 * @param displayName display name
	 */
	public BlockType(String id, String category, String displayName) {
		this.id = id;
		this.category = category;
		this.displayName = displayName;
		inputPorts = new ArrayList<>();
		outputPorts = new ArrayList<>();
		formulas = new ArrayList<>();
	}

	/**
	 * Add input port to block with given name and types
	 * @param name port name
	 * @param type types of port
	 */
	public void addInputPort(String name, Type type) {
		inputPorts.add(new BlockPort(this, name, type));
	}

	/**
	 * Add output port to block with given name and types
	 * @param name port name
	 * @param type types of port
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

	/**
	 * Get input port definitions
	 * @return list of input ports
	 */
	public ArrayList<BlockPort> getInputPorts() {
		return this.inputPorts;
	}

	/**
	 * Get output port definitions
	 * @return list of output ports
	 */
	public ArrayList<BlockPort> getOutputPorts() {
		return this.outputPorts;
	}

	@Override
	public void fromXML(XmlActiveNode xmlDom) throws XMLParsingException {
		xmlDom.getCurrentNode("blocktype");
		this.id = xmlDom.getAttribute("id");
		this.displayName = xmlDom.getAttribute("display-name");

		for (XmlActiveNode typeChildren : xmlDom.childIterator()) {
			switch (typeChildren.getTag()) {
				case "inputs": {
					BlockPort blockPort;
					for (XmlActiveNode node : typeChildren.childIterator()) {
						blockPort = new BlockPort(this);
						blockPort.fromXML(node);
						this.inputPorts.add(blockPort);
					}
					break;
				}

				case "outputs": {
					BlockPort blockPort;
					for (XmlActiveNode node : typeChildren.childIterator()) {
						blockPort = new BlockPort(this);
						blockPort.fromXML(node);
						this.outputPorts.add(blockPort);
					}
					break;
				}

				case "formulas": {
					Formula formula;
					for (XmlActiveNode node : typeChildren.childIterator()) {
						formula = new Formula();
						formula.fromXML(node);
						this.formulas.add(formula);
					}
					break;
				}
			}
		}
	}

	@Override
	public void toXML(XmlActiveNode xmlDom) {
		xmlDom.createChildElement("blocktype");
		xmlDom.setAttribute("id", this.id);
		xmlDom.setAttribute("display-name", this.displayName);

		xmlDom.createChildElement("inputs");
		for (BlockPort port : this.inputPorts) {
			port.toXML(xmlDom);
		}
		xmlDom.parentNode();

		xmlDom.createChildElement("outputs");
		for (BlockPort port : this.outputPorts) {
			port.toXML(xmlDom);
		}
		xmlDom.parentNode();

		xmlDom.createChildElement("formulas");
		for (Formula formula : this.formulas) {
			formula.toXML(xmlDom);
		}
		xmlDom.parentNode();

		xmlDom.parentNode();
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}

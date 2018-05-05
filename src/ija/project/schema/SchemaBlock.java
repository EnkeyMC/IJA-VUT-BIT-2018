package ija.project.schema;

import ija.project.processor.Processor;
import ija.project.exception.ApplicationException;
import ija.project.exception.XMLParsingException;
import ija.project.register.BlockTypeRegister;
import ija.project.xml.XmlActiveNode;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.security.KeyException;


public class SchemaBlock extends Block {

	/** XML tag to use for saving/loading */
	public static final String XML_TAG = "schemablock";

	/**
	 * Create blank schema block
	 */
	public SchemaBlock() {
		super();
	}

	/**
	 * Construct object from the given block type
	 * @param blockType block types
	 */
	public SchemaBlock(BlockType blockType) {
		super(blockType);
	}

	/**
	 * Get if BlockType is for ValueBlock
	 * @param blockType BlockType
	 * @return true if BlockType is for ValueBlock, false otherwise
	 */
	public static boolean isSchemaBlock(BlockType blockType) {
		return blockType.getBlockXmlTag().equals(XML_TAG);
	}

	@Override
	public void calculate() {
		getInputForComputation();
		dummyOutputConnections();
		Processor processor = new Processor(getBlockType().getSchema());
		try { processor.calculateAll(); }
		catch (ApplicationException e) {
			throw new ApplicationException("Block '" + getBlockType().getDisplayName()
					+ "' (ID " + getId() + ")\n" + e.getMessage());
		}
		transferResultToOutput();
	}

	/**
	 * Prepare the inner schema of SchemaBlock for calculation
	 * Ports of the SchemaBlock are not actually interconnected with
	 * ports of inner schema blocks, so input values must be
	 * transported 'manually'
	 */
	private void getInputForComputation() {
		for (BlockPort port: getBlockType().getInputPorts()) {
			TypeValues input = getInputPortValues().get(port.getName());

			String[] idPort = port.getName().split("_", 2);
			Block block = getBlockType().getSchema().getBlock(idPort[0]);

			block.getInputPortValues().put(idPort[1], input);
			block.makeDummyConnection(idPort[1]);
		}
	}

	/**
	 * Satisfy the constraint that every port must be connected
	 */
	private void dummyOutputConnections() {
		for (BlockPort port: getBlockType().getOutputPorts()) {
			String[] idPort = port.getName().split("_", 2);
			Block block = getBlockType().getSchema().getBlock(idPort[0]);
			block.makeDummyConnection(idPort[1]);
		}
	}

	/**
	 * Move the results from the output ports of the inner schema blocks
	 * to the output ports of the SchemaBlock
	 */
	private void transferResultToOutput() {
		for (BlockPort port: getBlockType().getOutputPorts()) {
			String[] idPort = port.getName().split("_", 2);
			Block block = getBlockType().getSchema().getBlock(idPort[0]);

			TypeValues output = block.getOutputPortValues().get(idPort[1]);
			getOutputPortValues().put(port.getName(), output);
		}
	}

	@Override
	public void fromXML(XmlActiveNode xmlDom) throws XMLParsingException {
		xmlDom.getCurrentNode(XML_TAG);
		String blockTypeCat = xmlDom.getAttribute("block-type-cat");
		String blockTypeId = xmlDom.getAttribute("block-type-id");
		BlockType blockType = BlockTypeRegister.getBlockTypeById(blockTypeCat, blockTypeId);
		initFromBlockType(blockType);

		this.setX(Integer.valueOf(xmlDom.getAttribute("x")));
		this.setY(Integer.valueOf(xmlDom.getAttribute("y")));
		this.setId(Long.valueOf(xmlDom.getAttribute("id")));
	}

	@Override
	public void toXML(XmlActiveNode xmlDom) {
		xmlDom.createChildElement(XML_TAG);
		xmlDom.setAttribute("block-type-cat", getBlockType().getCategory());
		xmlDom.setAttribute("block-type-id", getBlockType().getId());
		xmlDom.setAttribute("x", Integer.toString((int) getX()));
		xmlDom.setAttribute("y", Integer.toString((int) getY()));
		xmlDom.setAttribute("id", Long.toString(getId()));
		xmlDom.parentNode();
	}
}

package ija.project.schema;

import ija.project.exception.XMLParsingException;
import ija.project.register.BlockTypeRegister;
import ija.project.xml.XmlActiveNode;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.security.KeyException;
import java.util.Map;


public class ValueBlock extends Block {

	private TypeValues values;

	public static final String XML_TAG = "valueblock";


	public ValueBlock() {
		super();
	}

	public ValueBlock(BlockType blockType) {
		super(blockType);
		values = new TypeValues(blockType.getOutputPort("value").getType());
	}

	public static boolean isValueBlock(String id) {
		String[] parts = id.split("_", 2);
		return parts[0].equals("value");
	}

	public TypeValues getValues() {
		return values;
	}

	@Override
	public void calculate() {
		try {
			for (Map.Entry<String, Double> entry : values.getValuesMap().entrySet()) {
					getOutputPortValues().get("value").setValue(entry.getKey(), entry.getValue());
			}
		} catch (KeyException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void fromXML(XmlActiveNode xmlDom) throws XMLParsingException {
		xmlDom.getCurrentNode(XML_TAG);
		String blockTypeCat = xmlDom.getAttribute("block-type-cat");
		String blockTypeId = xmlDom.getAttribute("block-type-id");
		BlockType blockType = BlockTypeRegister.getBlockTypeById(blockTypeCat, blockTypeId);
		initFromBlockType(blockType);
		values = new TypeValues(blockType.getOutputPort("value").getType());

		this.setX(Integer.valueOf(xmlDom.getAttribute("x")));
		this.setY(Integer.valueOf(xmlDom.getAttribute("y")));
		this.setId(Long.valueOf(xmlDom.getAttribute("id")));

		xmlDom.firstChildNode("values");
		for (XmlActiveNode value : xmlDom.childIterator()) {
			try {
				String val = value.getAttribute("value");
				if (val.equals("null"))
					values.setValue(value.getAttribute("name"), null);
				else
					values.setValue(value.getAttribute("name"), Double.valueOf(val));
			} catch (KeyException e) {
				throw new XMLParsingException(
					"Type " + blockType.getOutputPort("value").getType().getDisplayName()
						+ " does not have component " + value.getAttribute("value"));
			}
		}
		xmlDom.parentNode();
	}

	@Override
	public void toXML(XmlActiveNode xmlDom) {
		xmlDom.createChildElement("valueblock");
		xmlDom.setAttribute("block-type-cat", getBlockType().getCategory());
		xmlDom.setAttribute("block-type-id", getBlockType().getId());
		xmlDom.setAttribute("x", Integer.toString((int) getX()));
		xmlDom.setAttribute("y", Integer.toString((int) getY()));
		xmlDom.setAttribute("id", Long.toString(getId()));

		xmlDom.createChildElement("values");
		for (Map.Entry<String, Double> value : values.getValuesMap().entrySet()) {
			xmlDom.createChildElement("value");
			xmlDom.setAttribute("name", value.getKey());
			Double val = value.getValue();
			if (val == null)
				xmlDom.setAttribute("value", "null");
			else
				xmlDom.setAttribute("value", Double.toString(val));
			xmlDom.parentNode();
		}
		xmlDom.parentNode();

		xmlDom.parentNode();
	}
}

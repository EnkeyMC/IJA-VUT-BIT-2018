package ija.project.schema;

import ija.project.exception.XMLParsingException;
import ija.project.utils.XmlActiveNode;
import ija.project.utils.XmlRepresentable;

import java.util.Map;

/**
 * Formula represents a way to transform input values to output value
 */
public class Formula implements XmlRepresentable {

	/**
	 * Transform input values to output values
	 * @param inputPorts input ports with values
	 * @param outputPorts output ports to transform values to
	 */
	public void transform(Map<String, TypeValues> inputPorts, Map<String, TypeValues> outputPorts) {

	}

	@Override
	public void fromXML(XmlActiveNode xmlDom) throws XMLParsingException {

	}

	@Override
	public void toXML(XmlActiveNode xmlDom) {

	}
}

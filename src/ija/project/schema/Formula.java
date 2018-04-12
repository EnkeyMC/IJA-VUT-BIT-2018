package ija.project.schema;

import ija.project.utils.XMLBuilder;
import ija.project.utils.XMLRepresentable;

import java.util.Collection;
import java.util.Map;

public class Formula implements XMLRepresentable {

	/**
	 * Transform input values to output values
	 * @param inputPorts input ports with values
	 * @param outputPorts output ports to transform values to
	 */
	public void transform(Map<String, TypeValues> inputPorts, Map<String, TypeValues> outputPorts) {

	}

	@Override
	public void fromXML(XMLBuilder xmlDom) {

	}

	@Override
	public void toXML(XMLBuilder xmlDom) {

	}
}

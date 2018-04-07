package ija.project.schema;

import ija.project.utils.XMLBuilder;

public interface XMLRepresentable {

	/**
	 * Load object from XML DOM
	 * @param xmlDom XML DOM representation
	 */
	void fromXML(XMLBuilder xmlDom);

	/**
	 * Save object to XML DOM
	 * @param xmlDom XML DOM representation
	 */
	void toXML(XMLBuilder xmlDom);
}

package ija.project.xml;


import ija.project.exception.XMLParsingException;

public interface XmlRepresentable {

	/**
	 * Load object from XML DOM
	 * @param xmlDom XML DOM representation
	 * @throws XMLParsingException if error occurred during parsing
	 */
	void fromXML(XmlActiveNode xmlDom) throws XMLParsingException;

	/**
	 * Save object to XML DOM
	 * @param xmlDom XML DOM representation
	 */
	void toXML(XmlActiveNode xmlDom);
}

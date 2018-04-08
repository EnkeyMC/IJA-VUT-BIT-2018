package ija.project.utils;

import ija.project.exception.XMLParsingException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class XMLBuilder {

	private Document dom;
	private Node currentNode;

	public XMLBuilder() {
		dom = null;
		currentNode = null;
	}

	public void parseFile(String file) {
		try {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			dom = docBuilder.parse(file);
			currentNode = dom.getDocumentElement();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new XMLParsingException("Failed parsing XML file (" + file + "): " + e.getMessage());
		}
	}

	public void parseString(String xml) {
		try {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource src = new InputSource(new StringReader(xml));
			dom = docBuilder.parse(src);
			currentNode = dom.getDocumentElement();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new XMLParsingException("Failed parsing XML string: " + e.getMessage());
		}
	}

	public Node getCurrentNode() {
		return currentNode;
	}

	public Node nextNode() {
		currentNode = currentNode.getNextSibling();
		return currentNode;
	}

	public Node parentNode() {
		currentNode = currentNode.getParentNode();
		return currentNode;
	}

	public Node firstChildNode() {
		currentNode = currentNode.getFirstChild();
		return currentNode;
	}

	public String getAttribute(String attr) throws XMLParsingException {
		try {
			return currentNode.getAttributes().getNamedItem(attr).getTextContent();
		} catch (NullPointerException e) {
			throw new XMLParsingException("Node "+ currentNode.getNodeName() +" does not have attribute " + attr);
		}
	}
}

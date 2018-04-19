package ija.project.xml;

import ija.project.exception.XMLParsingException;
import ija.project.exception.XMLWritingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Wrapper for DocumentBuilder, simplifies api, remembers current position in document
 */
public class XmlDom implements XmlActiveNode {

	private Document dom;
	private Node currentNode;

	/**
	 * Construct object
	 */
	public XmlDom() {
		dom = null;
		currentNode = null;
	}

	/**
	 * Construct object from document and node
	 * @param dom Document
	 * @param currentNode current Node
	 */
	protected XmlDom(Document dom, Node currentNode) {
		this.dom = dom;
		this.currentNode = currentNode;
	}

	///////////////////
	//  XML READING  //
	///////////////////

	/**
	 * Parse XML file
	 * @param file filename
	 */
	public void parseFile(String file) {
		try {
			parseFromStream(new FileInputStream(file));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new XMLParsingException("Failed parsing XML file (" + file + "): " + e.getMessage());
		}
	}

	public void parseFile(URL file) {
		try {
			parseFromStream(file.openStream());
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new XMLParsingException("Failed parsing XML file (" + file.getPath() + "): " + e.getMessage());
		}
	}

	public void parseFile(File file) {
		try {
			parseFromStream(new FileInputStream(file));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new XMLParsingException("Failed parsing XML file (" + file.getPath() + "): " + e.getMessage());
		}
	}

	private void parseFromStream(InputStream stream) throws IOException, SAXException, ParserConfigurationException {
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		dom = docBuilder.parse(stream);
		currentNode = dom.getDocumentElement();
	}

	/**
	 * Parse XML string
	 * @param xml XML string
	 */
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

	@Override
	public Node getCurrentNode() {
		return currentNode;
	}

	@Override
	public Node getCurrentNode(String  expectedTag) throws XMLParsingException {
		if (!currentNode.getNodeName().equals(expectedTag))
			throw new XMLParsingException("Expected element " + expectedTag + ", actual: " + currentNode.getNodeName());
		return currentNode;
	}

	@Override
	public Node nextNode() {
		currentNode = currentNode.getNextSibling();
		return currentNode;
	}

	@Override
	public Node nextNode(String expectedTag) throws XMLParsingException {
		this.nextNode();
		return this.getCurrentNode(expectedTag);
	}

	@Override
	public Node parentNode() {
		currentNode = currentNode.getParentNode();
		return currentNode;
	}

	@Override
	public Node firstChildNode() {
		currentNode = currentNode.getFirstChild();
		return currentNode;
	}

	@Override
	public Node firstChildNode(String expectedTag) throws XMLParsingException {
		this.firstChildNode();
		return this.getCurrentNode(expectedTag);
	}

	@Override
	public String getTag() {
		return currentNode.getNodeName();
	}

	@Override
	public String getAttribute(String attr) throws XMLParsingException {
		try {
			return currentNode.getAttributes().getNamedItem(attr).getTextContent();
		} catch (NullPointerException e) {
			throw new XMLParsingException("Node "+ currentNode.getNodeName() +" does not have attribute " + attr);
		}
	}

	@Override
	public String getText() {
		return currentNode.getTextContent();
	}

	@Override
	public Iterable<XmlActiveNode> childIterator() {
		return () -> new Iterator<XmlActiveNode>() {
			private int currentChildIdx = 0;

			@Override
			public boolean hasNext() {
				return getChildNodes().getLength() > currentChildIdx;
			}

			@Override
			public XmlActiveNode next() {
				return new XmlDom(dom, getChildNodes().item(currentChildIdx++));
			}
		};
	}

	protected NodeList getChildNodes() {
		return new XmlNodeList(currentNode.getChildNodes());
	}

	///////////////////
	//  XML WRITING  //
	///////////////////

	/**
	 * Create new XML document with root element
	 * @param rootTag XML root tag
	 */
	public void newDocument(String rootTag) {
		try {
			dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			dom.appendChild(dom.createElement(rootTag));
			currentNode = dom.getDocumentElement();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write current XML DOM to file
	 * @param filename path to file
	 * @throws XMLWritingException when saving fails
	 */
	public void writeToFile(String filename) throws XMLWritingException {
		writeToFile(new File(filename));
	}

	public void writeToFile(File file) throws XMLWritingException {
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Result output = new StreamResult(file);
			Source input = new DOMSource(dom);

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(input, output);
		} catch (TransformerException e) {
			throw new XMLWritingException("Could not save " + file.getPath());
		}
	}

	@Override
	public Node createChildElement(String tag) {
		Node newNode = dom.createElement(tag);
		currentNode.appendChild(newNode);
		currentNode = newNode;
		return newNode;
	}

	@Override
	public void setAttribute(String name, String value) {
		Element el = (Element) currentNode;
		el.setAttribute(name, value);
	}

	@Override
	public void setText(String text) {
		currentNode.setTextContent(text);
	}

	/**
	 * This fixes whitespace text nodes with elements in it
	 *
	 * Code for this class taken from this answer on StackOverflow: https://stackoverflow.com/a/5851888/4551645
 	 */
	private static class XmlNodeList implements NodeList, Iterable<Node> {

		private List<Node> nodes;

		public XmlNodeList(NodeList list) {
			nodes = new ArrayList<>();
			for (int i = 0; i < list.getLength(); i++) {
				if (!isWhitespaceNode(list.item(i))) {
					nodes.add(list.item(i));
				}
			}
		}

		@Override
		public Node item(int index) {
			return nodes.get(index);
		}

		@Override
		public int getLength() {
			return nodes.size();
		}

		private static boolean isWhitespaceNode(Node n) {
			if (n.getNodeType() == Node.TEXT_NODE) {
				String val = n.getNodeValue();
				return val.trim().length() == 0;
			} else {
				return false;
			}
		}

		@Override
		public Iterator<Node> iterator() {
			return nodes.iterator();
		}
	}
}

package ija.project.utils;

import com.sun.istack.internal.NotNull;
import ija.project.exception.XMLParsingException;
import org.w3c.dom.Node;

import java.util.Iterator;

public interface XmlActiveNode {

	/**
	 * Get current selected node
	 * @return current node
	 */
	Node getCurrentNode();

	/**
	 * Get current selected node
	 * @param expectedTag expected node tag if not null
	 * @return current node
	 * @throws XMLParsingException if expected tag is not same as actual
	 */
	Node getCurrentNode(String expectedTag) throws XMLParsingException;

	/**
	 * Move to next sibling node and return it
	 * @return next sibling node, null if sibling node does not exist
	 */
	Node nextNode();

	/**
	 * Move to next sibling node and return it
	 * @param expectedTag expected node tag
	 * @return next sibling node, null if sibling node does not exist
	 * @throws XMLParsingException if sibling's node tag is not same as expected
	 */
	Node nextNode(String expectedTag) throws XMLParsingException;

	/**
	 * Move to parent node and return it
	 * @return parent node, null if parent node does not exist
	 */
	Node parentNode();

	/**
	 * Move to first child node and return it
	 * @return child node, null if child node does not exist
	 */
	Node firstChildNode();

	/**
	 * Move to first child node and return it
	 * @param expectedTag child node's expected tag
	 * @return child node, null if child node does not exist
	 * @throws XMLParsingException if child's node tag is not same as expected
	 */
	Node firstChildNode(String expectedTag) throws XMLParsingException;

	/**
	 * Get current node tag
	 * @return tag
	 */
	String getTag();

	/**
	 * Get node attribute by name
	 * @param attr attribute name
	 * @return attribute content
	 * @throws XMLParsingException if attribute does not exist
	 */
	String getAttribute(String attr) throws XMLParsingException;

	/**
	 * Get current node text content
	 * @return text content
	 */
	String getText();

	/**
	 * Get child iterator
	 * @return iterator
	 */
	Iterable<XmlActiveNode> childIterator();

	/**
	 * Create child node in current node and move to it
	 * @param tag child's node tag name
	 * @return newly created child node
	 */
	Node createChildElement(String tag);

	/**
	 * Set attribute to current node
	 * @param name attribute name
	 * @param value attribute value
	 */
	void setAttribute(String name, String value);

	/**
	 * Set text content to current node
	 * @param text text content
	 */
	void setText(String text);
}

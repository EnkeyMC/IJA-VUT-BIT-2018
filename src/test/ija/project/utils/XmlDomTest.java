package test.ija.project.utils;

import ija.project.exception.XMLParsingException;
import ija.project.xml.XmlDom;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlDomTest {

	private XmlDom builder;

	@Before
	public void setUp() throws Exception {
		builder = new XmlDom();
	}

	@Test(expected = XMLParsingException.class)
	public void parseEmptyTest() {
		builder.parseString("");
	}

	@Test(expected = XMLParsingException.class)
	public void parseInvalidFileTest() {
		builder.parseFile("invalid");
	}

	@Test
	public void simpleElementTest() {
		String xml = "<schema></schema>";
		builder.parseString(xml);
		assertEquals("schema", builder.getCurrentNode().getNodeName());
	}

	@Test(expected = XMLParsingException.class)
	public void twoRootElementsTest() {
		String xml = "<schema></schema><schema2></schema2>";
		builder.parseString(xml);
	}

	@Test
	public void firstChildNodeTest() {
		String xml = "<schema><node></node></schema>";
		builder.parseString(xml);
		assertEquals("node", builder.firstChildNode().getNodeName());
		assertEquals("node", builder.getCurrentNode().getNodeName());
	}

	@Test
	public void firstChildNodeInvalidTest() {
		String xml = "<schema></schema>";
		builder.parseString(xml);
		assertNull(builder.firstChildNode());
	}

	@Test
	public void nextNodeTest() {
		String xml = "<schema><node></node><node1></node1></schema>";
		builder.parseString(xml);
		builder.firstChildNode();
		assertEquals("node1", builder.nextNode().getNodeName());
	}

	@Test
	public void nextNodeInvalidTest() {
		String xml = "<schema><node></node></schema>";
		builder.parseString(xml);
		builder.firstChildNode();
		assertNull(builder.nextNode());
	}

	@Test
	public void parentNodeTest() {
		String xml = "<schema><node></node></schema>";
		builder.parseString(xml);
		builder.firstChildNode();
		assertEquals("schema", builder.parentNode().getNodeName());
	}

	@Test
	public void parentNodeInvalidTest() {
		String xml = "<schema></schema>";
		builder.parseString(xml);
		builder.parentNode();
		assertNull(builder.parentNode());
	}

	@Test
	public void getAttributeTest() {
		String xml = "<schema attr=\"ibute\"></schema>";
		builder.parseString(xml);
		assertEquals("ibute", builder.getAttribute("attr"));
	}

	@Test(expected = XMLParsingException.class)
	public void getInvalidAttributeTest() {
		String xml = "<schema attr=\"ibute\"></schema>";
		builder.parseString(xml);
		builder.getAttribute("invalid");
	}

	@Test
	public void firstChildNodeInvalidWithAttr() {
		String xml = "<schema attr=\"val\"></schema>";
		builder.parseString(xml);
		assertNull(builder.firstChildNode());
	}
}

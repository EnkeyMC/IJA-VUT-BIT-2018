package ija.project.utils;

import ija.project.exception.XMLParsingException;
import org.junit.Before;
import org.junit.Test;

public class XMLBuilderTest {

	private XMLBuilder builder;

	@Before
	public void setUp() throws Exception {
		builder = new XMLBuilder();
	}

	@Test(expected = XMLParsingException.class)
	public void parseEmptyTest() {
		builder.parseString("");
	}

	@Test
	public void parseInvalidFileTest() {

	}
}

package ija.project.schema;

import org.junit.Test;

import java.security.KeyException;

import static org.junit.Assert.*;

public class BlockPortTest {

	@Test
	public void testSetValue() throws KeyException {
		Block b = new Block();
		Type t = new Type();
		t.addKey("blockport");
		BlockPort bp = new BlockPort(b, "first", t);
		bp.setValue("blockport", 5.4);

		assertEquals("Values aren't same", new Double(5.4), bp.getValue("blockport"));
	}
}

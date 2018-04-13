package test.ija.project.schema;

import ija.project.schema.BlockType;
import ija.project.schema.BlockPort;
import ija.project.schema.Type;
import org.junit.Test;

import java.security.KeyException;

import static org.junit.Assert.*;

public class BlockPortTest {

	@Test
	public void testSetValue() throws KeyException {
		BlockType b = new BlockType();
		Type t = new Type();
		t.addKey("blockport");
		BlockPort bp = new BlockPort(b, "first", t);

		assertTrue(bp.getType().getKeys().contains(new String("blockport")));
	}
}

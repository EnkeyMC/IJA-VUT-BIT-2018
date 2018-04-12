package test.ija.project.schema;

import ija.project.schema.Type;
import org.junit.Test;

import java.security.KeyException;

import static org.junit.Assert.*;

public class TypeTest {

	@Test(expected = KeyException.class)
	public void addKeyThrowsException() throws KeyException {
		Type t = new Type();
		t.addKey("first");
		t.addKey("first");
	}

	@Test
	public void testAddKey() throws KeyException {
		Type t = new Type();
		t.addKey("first");
		assertTrue("Value in map after addKey is not null", t.getKeys().contains(new String("first")));
	}
}

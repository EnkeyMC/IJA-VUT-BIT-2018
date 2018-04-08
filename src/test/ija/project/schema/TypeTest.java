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
		assertEquals("Value in map after addKey is not null", null, t.getValue("first"));
	}

	@Test(expected = KeyException.class)
	public void testSetValueThrowsException() throws KeyException {
		Type t = new Type();
		t.addKey("second");
		t.setValue("secnd", 0.0);
	}

	@Test
	public void testSetValue() throws KeyException {
		Type t = new Type();
		t.addKey("third");
		t.setValue("third", 5.1);
		assertEquals("Values aren't same", new Double(5.1), t.getValue("third"));
	}
}

package test.ija.project.register;

import ija.project.register.TypeRegister;
import ija.project.schema.Type;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeRegisterTest {

	@Test
	public void registerTypeTest() {
		Type t = new Type();
		t.setId("tr");
		TypeRegister.reg(t);
		assertTrue(TypeRegister.getTypeRegistry().contains(t));
	}

	@Test
	public void removeTypeTest() {
		Type t = new Type();
		t.setId("t");
		TypeRegister.reg(t);
		try {
			TypeRegister.removeType("t");
		} catch (RuntimeException e) {
			fail();
		}
	}

	@Test(expected = RuntimeException.class)
	public void removeInvalidTypeTest() {
		Type t = new Type();
		t.setId("ta");
		TypeRegister.reg(t);
		TypeRegister.removeType("invalid");
	}
}

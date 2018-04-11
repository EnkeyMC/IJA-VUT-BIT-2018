package test.ija.project.register;

import ija.project.register.BlockRegister;
import ija.project.schema.Block;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlockRegisterTest {

	@Test
	public void registerBlockBuiltinTest() {
		Block b = new Block();
		b.setId("q");
		BlockRegister.reg("type1", b);
		assertTrue(BlockRegister.getBlockRegistry("type1").contains(b));
		assertFalse(BlockRegister.getBlockRegistry("type2").contains(b));
	}

	@Test
	public void removeBlockTest() {
		Block b = new Block();
		b.setId("a");
		BlockRegister.reg("type1", b);
		try {
			BlockRegister.removeBlock("type1", "a");
		} catch (RuntimeException e) {
			fail();
		}
	}

	@Test(expected = RuntimeException.class)
	public void removeInvalidBlock() {
		Block b = new Block();
		b.setId("b");
		BlockRegister.reg("type1", b);
		BlockRegister.removeBlock("type1", "invalid");
	}
}

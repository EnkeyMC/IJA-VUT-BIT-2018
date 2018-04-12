package test.ija.project.register;

import ija.project.register.BlockRegister;
import ija.project.schema.BlockType;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlockTypeRegisterTest {

	@Test
	public void registerBlockBuiltinTest() {
		BlockType b = new BlockType();
		b.setId("q");
		BlockRegister.reg("type1", b);
		assertTrue(BlockRegister.getBlockRegistry("type1").contains(b));
		assertFalse(BlockRegister.getBlockRegistry("type2").contains(b));
	}

	@Test
	public void removeBlockTest() {
		BlockType b = new BlockType();
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
		BlockType b = new BlockType();
		b.setId("b");
		BlockRegister.reg("type1", b);
		BlockRegister.removeBlock("type1", "invalid");
	}
}

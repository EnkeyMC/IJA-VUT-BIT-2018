package test.ija.project.register;

import ija.project.register.BlockTypeRegister;
import ija.project.schema.BlockType;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlockTypeRegisterTest {

	@Test
	public void registerBlockBuiltinTest() {
		BlockType b = new BlockType();
		b.setId("q");
		BlockTypeRegister.reg("type1", b);
		assertTrue(BlockTypeRegister.getBlockRegistry("type1").contains(b));
	}

	@Test
	public void removeBlockTest() {
		BlockType b = new BlockType();
		b.setId("a");
		BlockTypeRegister.reg("type1", b);
		try {
			BlockTypeRegister.removeBlock("type1", "a");
		} catch (RuntimeException e) {
			fail();
		}
	}

	@Test(expected = RuntimeException.class)
	public void removeInvalidBlock() {
		BlockType b = new BlockType();
		b.setId("b");
		BlockTypeRegister.reg("type1", b);
		BlockTypeRegister.removeBlock("type1", "invalid");
	}
}

package ija.project.register;

import ija.project.schema.Block;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlockRegisterTest {

	@Test
	public void registerBlockBuiltinTest() {
		Block b = new Block();
		b.setId("q");
		BlockRegister.reg(BlockType.BUILT_IN, b);
		assertTrue(BlockRegister.getBlockRegistry(BlockType.BUILT_IN).contains(b));
		assertFalse(BlockRegister.getBlockRegistry(BlockType.SCHEMA_BLOCKS).contains(b));
		assertFalse(BlockRegister.getBlockRegistry(BlockType.USER_DEFINED).contains(b));
	}

	@Test
	public void registerBlockUserTest() {
		Block b = new Block();
		b.setId("qf");
		BlockRegister.reg(BlockType.USER_DEFINED, b);
		assertTrue(BlockRegister.getBlockRegistry(BlockType.USER_DEFINED).contains(b));
		assertFalse(BlockRegister.getBlockRegistry(BlockType.SCHEMA_BLOCKS).contains(b));
		assertFalse(BlockRegister.getBlockRegistry(BlockType.BUILT_IN).contains(b));
	}

	@Test
	public void	registerBlockSchemaTest() {
		Block b = new Block();
		b.setId("qa");
		BlockRegister.reg(BlockType.SCHEMA_BLOCKS, b);
		assertTrue(BlockRegister.getBlockRegistry(BlockType.SCHEMA_BLOCKS).contains(b));
		assertFalse(BlockRegister.getBlockRegistry(BlockType.BUILT_IN).contains(b));
			assertFalse(BlockRegister.getBlockRegistry(BlockType.USER_DEFINED).contains(b));
	}

	@Test
	public void removeBlockTest() {
		Block b = new Block();
		b.setId("a");
		BlockRegister.reg(BlockType.BUILT_IN, b);
		try {
			BlockRegister.removeBlock(BlockType.BUILT_IN, "a");
		} catch (RuntimeException e) {
			fail();
		}
	}

	@Test(expected = RuntimeException.class)
	public void removeInvalidBlock() {
		Block b = new Block();
		b.setId("b");
		BlockRegister.reg(BlockType.BUILT_IN, b);
		BlockRegister.removeBlock(BlockType.BUILT_IN, "invalid");
	}
}

package ija.project.register;

import ija.project.schema.Block;

import java.util.ArrayList;
import java.util.Map;

enum BlockType {
	BUILT_IN,
	USER_DEFINED,
	SCHEMA_BLOCKS
}

public class BlockRegister {
	private static Map<BlockType, ArrayList<Block>> register;

	public static void reg(BlockType type, Block block) {
		ArrayList<Block> blocks = register.getOrDefault(type, new ArrayList<>());
		blocks.add(block);
	}

	public static ArrayList<Block> getBlockRegistry(BlockType type) {
		return register.getOrDefault(type, new ArrayList<>());
	}
}

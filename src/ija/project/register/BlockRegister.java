package ija.project.register;

import ija.project.schema.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

enum BlockType {
	BUILT_IN,
	USER_DEFINED,
	SCHEMA_BLOCKS
}

public class BlockRegister {
	private static Map<BlockType, ArrayList<Block>> register = new HashMap<>();

	public static void reg(BlockType type, Block block) {
		if (!register.containsKey(type))
			register.put(type, new ArrayList<>());
		ArrayList<Block> blocks = register.get(type);
		blocks.add(block);
	}

	public static ArrayList<Block> getBlockRegistry(BlockType type) {
		if (!register.containsKey(type))
			register.put(type, new ArrayList<>());
		return register.get(type);
	}

	public static void removeBlock(BlockType type, String id) {
		for (Block b : getBlockRegistry(type)) {
			if (b.getId().equals(id)) {
				register.get(type).remove(b);
				return;
			}
		}
		throw new RuntimeException("Block " + id + " is not in registry");
	}
}

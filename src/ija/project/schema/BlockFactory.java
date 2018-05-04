package ija.project.schema;

import ija.project.exception.ApplicationException;

import java.util.HashMap;
import java.util.Map;

public class BlockFactory {

	private static Map<String, Class<? extends Block>> regsiteredBlocks = new HashMap<>();

	public static Block create(String xmlTag) {
		if (!regsiteredBlocks.containsKey(xmlTag))
			throw new ApplicationException("Block with name '" + xmlTag + "' is not registered");
		Class<? extends Block> blockClass = regsiteredBlocks.get(xmlTag);
		try {
			return blockClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return new Block();
		}
	}

	public static void registerBlock(String xmlTag, Class<? extends Block> blockClass) {
		regsiteredBlocks.put(xmlTag, blockClass);
	}
}

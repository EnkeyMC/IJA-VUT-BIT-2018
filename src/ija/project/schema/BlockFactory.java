package ija.project.schema;

import ija.project.exception.ApplicationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

	public static Block create(BlockType blockType) {
		if (!regsiteredBlocks.containsKey(blockType.getBlockXmlTag()))
			throw new ApplicationException("Block with name '" + blockType.getBlockXmlTag() + "' is not registered");
		Class<? extends Block> blockClass = regsiteredBlocks.get(blockType.getBlockXmlTag());
		try {
			Constructor<? extends Block> ctor = blockClass.getConstructor(BlockType.class);
			return ctor.newInstance(blockType);
		} catch (InstantiationException
				| IllegalAccessException
				| NoSuchMethodException
				| InvocationTargetException e)
		{
			e.printStackTrace();
			return new Block(blockType);
		}
	}

	public static void registerBlock(String xmlTag, Class<? extends Block> blockClass) {
		regsiteredBlocks.put(xmlTag, blockClass);
	}
}

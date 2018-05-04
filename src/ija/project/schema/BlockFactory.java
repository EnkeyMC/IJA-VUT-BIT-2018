package ija.project.schema;

import ija.project.exception.ApplicationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates instances of Blocks from registered Block subclasses
 */
public class BlockFactory {

	/** Registered Block Subclasses mapped by their XML tag */
	private static Map<String, Class<? extends Block>> regsiteredBlocks = new HashMap<>();

	/**
	 * Create Block instance from subclass given by it's XML tag
	 * @param xmlTag xml tag of block class
	 * @return new Block instance
	 */
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

	/**
	 * Create Block instance from subclass given by it's BlockType
	 * @param blockType blocks BlockType
	 * @return new Block instance
	 */
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

	/**
	 * Register Block subclass by it's XML tag
	 * @param xmlTag xml tag
	 * @param blockClass class to register
	 */
	public static void registerBlock(String xmlTag, Class<? extends Block> blockClass) {
		regsiteredBlocks.put(xmlTag, blockClass);
	}
}

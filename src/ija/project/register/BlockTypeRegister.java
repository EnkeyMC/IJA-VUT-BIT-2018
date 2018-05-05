package ija.project.register;

import ija.project.schema.BlockType;
import ija.project.xml.XmlActiveNode;
import ija.project.xml.XmlDom;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.ArrayList;
import java.net.URL;

/**
 * Static class containing all registered block types in their categories
 */
public class BlockTypeRegister {

	/**
	 * Types of schema blocks that have been already saved
	 */
	public static ArrayList<String> savedTypes = new ArrayList<>();

	/**
	 * Builtion schema blocks
	 */
	public static String[] builtins = { "value_double", "result_double",
		"double_add", "double_sub", "double_mul", "double_div" };

	/**
	 * BlockType registry with categories.
	 *
	 * Automatically gets listed in GUI due to ObservableMap and ObservableList being bind to GUI containers.
	 */
	private static ObservableMap<String, ObservableList<BlockType>> register = FXCollections.observableHashMap();

	/**
	 * Register BlockType in given category. BlockType id has to be unique in category.
	 * @param category category to register block in
	 * @param blockType block types to register
	 */
	public static void reg(String category, BlockType blockType) throws RuntimeException {
		if (!register.containsKey(category))
			register.put(category, FXCollections.observableArrayList());
		else {
			for (BlockType bType : register.get(category)) {
				if (bType.getId().equals(blockType.getId()))
					throw new RuntimeException("BlockType with id " + blockType.getId() + " already exists in category " + category);
			}
		}
		register.get(category).add(blockType);
	}

	/**
	 * Get all registered categories with block types
	 * @return list of block types mapped to categories
	 */
	public static ObservableMap<String, ObservableList<BlockType>> getAllRegisters() {
		return register;
	}

	/**
	 * Get registered block types in category
	 * @param category category to get
	 * @return list of registered block types in category
	 */
	public static ObservableList<BlockType> getBlockRegistry(String category) throws RuntimeException {
		if (!register.containsKey(category))
			throw new RuntimeException("Block category " + category + " is not loaded, please load missing components first");
		return register.get(category);
	}

	/**
	 * Remove block types from register given by id
	 * @param category category of the block types
	 * @param id block types id
	 */
	public static void removeBlock(String category, String id) throws RuntimeException {
		for (BlockType b : getBlockRegistry(category)) {
			if (b.getId().equals(id)) {
				register.get(category).remove(b);
				return;
			}
		}
		throw new RuntimeException("BlockType " + id + " (" + category + ") is not in registry");
	}

	/**
	 * Remove given block types from register
	 * @param category category of the block types
	 * @param blockType block types to remove
	 */
	public static void removeBlock(String category, BlockType blockType) throws RuntimeException {
		getBlockRegistry(category).remove(blockType);
	}

	/**
	 * Get block types by id
	 * @param category category of block types
	 * @param id id of block types
	 * @return block types
	 */
	public static BlockType getBlockTypeById(String category, String id) throws RuntimeException {
		for (BlockType blockType : getBlockRegistry(category)) {
			if (blockType.getId().equals(id))
				return blockType;
		}
		throw new RuntimeException("BlockType " + id + " (" + category + ") is not loaded, please load missing components first");
	}
}

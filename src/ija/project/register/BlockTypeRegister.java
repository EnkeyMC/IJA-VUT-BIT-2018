package ija.project.register;

import ija.project.schema.BlockType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 * Static class containing all registered block types in their categories
 */
public class BlockTypeRegister {

	/**
	 * BlockType registry with categories.
	 *
	 * Automatically gets listed in GUI due to ObservableMap and ObservableList being bind to GUI containers.
	 */
	private static ObservableMap<String, ObservableList<BlockType>> register = FXCollections.observableHashMap();

	/**
	 * Register BlockType in given category. BlockType id has to be unique in category.
	 * @param category category to register block in
	 * @param blockType block type to register
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
			throw new RuntimeException("Category " + category + " does not exist in BlockTypeRegistry");
		return register.get(category);
	}

	/**
	 * Remove block type from register given by id
	 * @param category category of the block type
	 * @param id block type id
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
	 * Remove given block type from register
	 * @param category category of the block type
	 * @param blockType block type to remove
	 */
	public static void removeBlock(String category, BlockType blockType) throws RuntimeException {
		getBlockRegistry(category).remove(blockType);
	}

	/**
	 * Get block type by id
	 * @param category category of block type
	 * @param id id of block type
	 * @return block type
	 */
	public static BlockType getBlockTypeById(String category, String id) throws RuntimeException {
		for (BlockType blockType : register.get(category)) {
			if (blockType.getId().equals(id))
				return blockType;
		}
		throw new RuntimeException("BlockType " + id + " (" + category + ") is not in registry");
	}
}

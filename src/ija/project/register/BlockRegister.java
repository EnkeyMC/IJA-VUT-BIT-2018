package ija.project.register;

import ija.project.schema.BlockType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class BlockRegister {
	private static ObservableMap<String, ObservableList<BlockType>> register = FXCollections.observableHashMap();

	public static void reg(String type, BlockType blockType) {
		getBlockRegistry(type).add(blockType);
	}

	public static ObservableMap<String, ObservableList<BlockType>> getAllRegisters() {
		return register;
	}

	public static ObservableList<BlockType> getBlockRegistry(String type) {
		if (!register.containsKey(type))
			register.put(type, FXCollections.observableArrayList());
		return register.get(type);
	}

	public static void removeBlock(String type, String id) {
		for (BlockType b : getBlockRegistry(type)) {
			if (b.getId().equals(id)) {
				register.get(type).remove(b);
				return;
			}
		}
		throw new RuntimeException("BlockType " + id + " is not in registry");
	}
}

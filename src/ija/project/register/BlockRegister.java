package ija.project.register;

import ija.project.schema.Block;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlockRegister {
	private static ObservableMap<String, ObservableList<Block>> register = FXCollections.observableHashMap();

	public static void reg(String type, Block block) {
		getBlockRegistry(type).add(block);
	}

	public static ObservableMap<String, ObservableList<Block>> getAllRegisters() {
		return register;
	}

	public static ObservableList<Block> getBlockRegistry(String type) {
		if (!register.containsKey(type))
			register.put(type, FXCollections.observableArrayList());
		return register.get(type);
	}

	public static void removeBlock(String type, String id) {
		for (Block b : getBlockRegistry(type)) {
			if (b.getId().equals(id)) {
				register.get(type).remove(b);
				return;
			}
		}
		throw new RuntimeException("Block " + id + " is not in registry");
	}
}

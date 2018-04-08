package ija.project.register;

import ija.project.schema.Type;

import java.util.ArrayList;

public class TypeRegister {
	private static ArrayList<Type> register;

	public static void reg(Type type) {
		register.add(type);
	}

	public static ArrayList<Type> getBlockRegistry(BlockType type) {
		return register;
	}
}

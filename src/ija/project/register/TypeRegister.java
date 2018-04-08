package ija.project.register;

import ija.project.schema.Type;

import java.util.ArrayList;

public class TypeRegister {
	private static ArrayList<Type> register = new ArrayList<>();

	public static void reg(Type type) {
		register.add(type);
	}

	public static ArrayList<Type> getTypeRegistry() {
		return register;
	}

	public static void removeType(String id) {
		for (Type t : getTypeRegistry()) {
			if (t.getId().equals(id)) {
				register.remove(t);
				return;
			}
		}
		throw new RuntimeException("Type " + id + " is not in registry");
	}
}

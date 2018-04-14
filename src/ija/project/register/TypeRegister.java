package ija.project.register;

import ija.project.schema.Type;

import java.util.ArrayList;

/**
 * Static class containing all registered data types
 */
public class TypeRegister {
	/**
	 * List of registered types
	 */
	private static ArrayList<Type> register = new ArrayList<>();

	/**
	 * Register a type
	 * @param type type
	 */
	public static void reg(Type type) {
		register.add(type);
	}

	/**
	 * Get all registered types
	 * @return list of registered types
	 */
	public static ArrayList<Type> getTypeRegistry() {
		return register;
	}

	/**
	 * Get registered type by id
	 * @param id type id
	 * @return type
	 * @throws RuntimeException if type with given id does not exist
	 */
	public static Type getTypeById(String id) throws RuntimeException {
		for (Type t : register) {
			if (t.getId().equals(id))
				return t;
		}
		throw new RuntimeException("Type " + id + " is not in registry");
	}

	/**
	 * Remove type from registry
	 * @param id type id
	 */
	public static void removeType(String id) throws RuntimeException {
		for (Type t : register) {
			if (t.getId().equals(id)) {
				register.remove(t);
				return;
			}
		}
		throw new RuntimeException("Type " + id + " is not in registry");
	}

	/**
	 * Remove type from registry
	 * @param type type
	 */
	public static void removeType(Type type) throws RuntimeException {
		if (register.remove(type)) {
			throw new RuntimeException("Type " + type.getId() + " is not in registry");
		}
	}
}

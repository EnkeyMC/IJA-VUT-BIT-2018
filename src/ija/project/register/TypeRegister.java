package ija.project.register;

import ija.project.schema.Type;
import ija.project.xml.XmlActiveNode;
import ija.project.xml.XmlDom;

import java.net.URL;
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
	 * Register a types
	 * @param type types
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
	 * Get registered types by id
	 * @param id types id
	 * @return types
	 * @throws RuntimeException if types with given id does not exist
	 */
	public static Type getTypeById(String id) throws RuntimeException {
		for (Type t : register) {
			if (t.getId().equals(id))
				return t;
		}
		throw new RuntimeException("Type " + id + " is not in registry");
	}

	/**
	 * Remove types from registry
	 * @param id types id
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
	 * Remove types from registry
	 * @param type types
	 */
	public static void removeType(Type type) throws RuntimeException {
		if (register.remove(type)) {
			throw new RuntimeException("Type " + type.getId() + " is not in registry");
		}
	}
}

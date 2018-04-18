package ija.project.schema;

import java.security.KeyException;
import java.util.HashMap;

/**
 * Values for given Type
 */
public class TypeValues {
	/** Mapped values by keys from Type */
	private HashMap<String, Double> valuesMap;
	/** Type */
	private Type type;

	/**
	 * Construct values from given types
	 * @param fromType types
	 */
	public TypeValues(Type fromType) {
		this.type = fromType;
		this.valuesMap = new HashMap<>();

		for (String key : fromType.getKeys()) {
			valuesMap.put(key, null);
		}
	}

	/**
	 * Set key and value to types
	 * @param key value key
	 * @param value value
	 */
	public void setValue(String key, Double value) throws KeyException {
		if (valuesMap.containsKey(key)) {
			valuesMap.put(key, value);
		} else {
			throw new KeyException("Not existing types with this key");
		}
	}

	/**
	 * Get key value
	 * @param key value key
	 * @return value
	 */
	public Double getValue(String key) throws KeyException {
		if (valuesMap.containsKey(key)) {
			return valuesMap.get(key);
		} else {
			throw new KeyException("Not existing types with this key");
		}
	}

	/**
	 * Get types definition for values
	 * @return types
	 */
	public Type getType() {
		return type;
	}
}

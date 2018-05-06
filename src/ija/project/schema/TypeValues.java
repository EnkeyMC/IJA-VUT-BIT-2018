package ija.project.schema;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;

import java.security.KeyException;
import java.util.HashMap;
import java.util.Map;

/**
 * Values for given Type
 */
public class TypeValues {

	/** Mapped values by keys from Type */
	private MapProperty<String, Double> valuesMap;
	/** Type */
	private Type type;

	/**
	 * Construct values from given types
	 * @param fromType types
	 */
	public TypeValues(Type fromType) {
		this.type = fromType;
		this.valuesMap = new SimpleMapProperty<>(FXCollections.observableHashMap());

		for (String key : fromType.getKeys()) {
			valuesMap.put(key, null);
		}
	}

	/**
	 * Set key and value to types
	 * @param key value key
	 * @param value value
	 * @throws KeyException when there is no existing type with this key
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
	 * @throws KeyException when no existing types with this key
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
	 * Clear values (sets them to null)
	 */
	public void clearValues() {
		for (Map.Entry<String, Double> value : valuesMap.entrySet()) {
			value.setValue(null);
		}
	}

	/**
	 * Get all values
	 * @return values map
	 */
	public MapProperty<String, Double> getValuesMap() {
		return valuesMap;
	}

	/**
	 * Get types definition for values
	 * @return types
	 */
	public Type getType() {
		return type;
	}
}

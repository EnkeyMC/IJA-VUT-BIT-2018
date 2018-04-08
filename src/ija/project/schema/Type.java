package ija.project.schema;

import ija.project.utils.XMLBuilder;
import ija.project.utils.XMLRepresentable;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.security.KeyException;
import java.util.HashMap;
import java.util.Map;

public class Type implements XMLRepresentable {

	/** Type identificator */
	private String id;
	/** Type display name */
	private String displayName;
	/** Type mapped values */
	private Map<String, Double> mapping;

	/**
	 * Creates new blank type
	 */
	public Type() {
		id = null;
		displayName = null;
		mapping = new HashMap<>();
	}

	/**
	 * Create type with given id and display name
	 * @param id unique type id
	 * @param displayName type display name
	 */
	public Type(String id, String displayName) {
		this.displayName = displayName;
		this.id = id;
	}

	/**
	 * Add key to type
	 * @param key value key
	 */
	public void addKey(String key) throws KeyException {
		if (mapping.containsKey(key)) {
			throw new KeyException("Type already contains given key");
		}
		mapping.put(key, null);
	}

	/**
	 * Set key and value to type
	 * @param key value key
	 * @param value ---
	 */
	public void setValue(String key, Double value) throws KeyException {
		if (mapping.containsKey(key)) {
			mapping.put(key, value);
		} else {
			throw new KeyException("Not existing type with this key");
		}
	}

	/**
	 * Get key value
	 * @param key value key
	 * @return value
	 */
	public Double getValue(String key) throws KeyException {
		if (mapping.containsKey(key)) {
			return mapping.get(key);
		} else {
			throw new KeyException("Not existing type with this key");
		}
	}

	/**
	 * Get id value
	 * @return id value
	 */
	public String getId() {
		return id;
	}

	/**
	 * Get display name
	 * @return display name value
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Set id value
	 * @param id id value to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Set display name
	 * @param displayName display name to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public void fromXML(XMLBuilder xmlDom) {

	}

	@Override
	public void toXML(XMLBuilder xmlDom) {

	}
}

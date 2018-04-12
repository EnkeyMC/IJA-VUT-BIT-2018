package ija.project.schema;

import ija.project.utils.XMLBuilder;
import ija.project.utils.XMLRepresentable;

import java.security.KeyException;
import java.util.ArrayList;
import java.util.HashMap;

public class Type implements XMLRepresentable {

	/** Type identificator */
	private String id;
	/** Type display name */
	private String displayName;
	/** Type mapped values */
	private ArrayList<String> keys;

	/**
	 * Creates new blank type
	 */
	public Type() {
		id = null;
		displayName = null;
		keys = new ArrayList<>();
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
		if (keys.contains(key)) {
			throw new KeyException("Type already contains given key");
		}
		keys.add(key);
	}

	public ArrayList<String> getKeys() {
		return keys;
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

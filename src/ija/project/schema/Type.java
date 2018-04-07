package ija.project.schema;

import ija.project.utils.XMLBuilder;

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

	}

	/**
	 * Create type with given id and display name
	 * @param id unique type id
	 * @param displayName type display name
	 */
	public Type(String id, String displayName) {

	}

	/**
	 * Add key to type
	 * @param key value key
	 */
	public void addKey(String key) {

	}

	/**
	 * Get key value
	 * @param key value key
	 * @return value
	 */
	public Double getValue(String key) {
		return null;
	}

	public String getId() {
		return id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setId(String id) {
		this.id = id;
	}

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

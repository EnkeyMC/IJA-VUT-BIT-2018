package ija.project.schema;

import ija.project.exception.XMLParsingException;
import ija.project.xml.XmlActiveNode;
import ija.project.xml.XmlRepresentable;

import java.security.KeyException;
import java.util.ArrayList;

/**
 * Type defines set of keys for values
 */
public class Type implements XmlRepresentable {

	/** Type identificator */
	private String id;
	/** Type display name */
	private String displayName;
	/** Type mapped values */
	private ArrayList<String> keys;

	/**
	 * Creates new blank types
	 */
	public Type() {
		id = null;
		displayName = null;
		keys = new ArrayList<>();
	}

	/**
	 * Create types with given id and display name
	 * @param id unique types id
	 * @param displayName types display name
	 */
	public Type(String id, String displayName) {
		this.displayName = displayName;
		this.id = id;
	}

	/**
	 * Add key to types
	 * @param key value key
	 * @throws KeyException when type already contains given key
	 */
	public void addKey(String key) throws KeyException {
		if (keys.contains(key)) {
			throw new KeyException("Type already contains given key");
		}
		keys.add(key);
	}

	/**
	 * Get types keys
	 * @return list of keys
	 */
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
	public void fromXML(XmlActiveNode xmlDom) throws XMLParsingException {
		xmlDom.getCurrentNode("type");
		this.id = xmlDom.getAttribute("id");
		this.displayName = xmlDom.getAttribute("display-name");

		for (XmlActiveNode key : xmlDom.childIterator()) {
			if (key.getTag().equals("key")) {
				this.keys.add(key.getText());
			}
		}
	}

	@Override
	public void toXML(XmlActiveNode xmlDom) {
		xmlDom.createChildElement("type");
		xmlDom.setAttribute("id", this.id);
		xmlDom.setAttribute("display-name", this.displayName);

		for (String key : this.keys) {
			xmlDom.createChildElement("key");
			xmlDom.setText(key);
			xmlDom.parentNode();
		}
		xmlDom.parentNode();
	}
}

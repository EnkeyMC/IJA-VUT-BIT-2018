package ija.project.schema;

import ija.project.utils.XMLBuilder;
import ija.project.utils.XMLRepresentable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

/**
 * Schema represents block graph
 */
public class Schema implements XMLRepresentable {
	/**
	 * Schema file name
	 */
	private String filename;
	/**
	 * Schema display name
	 */
	private StringProperty displayName;
	/**
	 * Schema blocks
	 */
	private ArrayList<Block> blocks;

	/**
	 * Construct blank schema
	 */
	public Schema() {
		this.blocks = new ArrayList<>();
		displayName = new SimpleStringProperty("Untitled");
	}

	/**
	 * Get all blocks in schema
	 * @return list of blocks
	 */
	public ArrayList<Block> getBlocks() {
		return this.blocks;
	}

	/**
	 * Add block to schema
	 * @param block block
	 */
	public void addBlock(Block block) {
		this.blocks.add(block);
	}

	@Override
	public void fromXML(XMLBuilder xmlDom) {

	}

	@Override
	public void toXML(XMLBuilder xmlDom) {

	}

	public String getDisplayName() {
		return displayName.get();
	}

	public StringProperty displayNameProperty() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName.set(displayName);
	}
}

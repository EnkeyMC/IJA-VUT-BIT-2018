package ija.project.schema;

import ija.project.utils.XMLBuilder;
import ija.project.utils.XMLRepresentable;

import java.util.ArrayList;

/**
 * Schema represents block graph
 */
public class Schema implements XMLRepresentable {
	/**
	 * Schema ID
	 */
	private String id;
	/**
	 * Schema display name
	 */
	private String displayName;
	/**
	 * Schema blocks
	 */
	private ArrayList<Block> blocks;

	/**
	 * Construct blank schema
	 */
	public Schema() {
		this.blocks = new ArrayList<>();
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
}

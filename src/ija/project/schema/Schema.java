package ija.project.schema;

import ija.project.utils.XMLBuilder;
import ija.project.utils.XMLRepresentable;

import java.util.ArrayList;

public class Schema implements XMLRepresentable {

	private String id;
	private String displayName;

	private ArrayList<Block> blocks;

	public Schema() {
		this.blocks = new ArrayList<>();
	}

	@Override
	public void fromXML(XMLBuilder xmlDom) {

	}

	@Override
	public void toXML(XMLBuilder xmlDom) {

	}

	public ArrayList<Block> getBlocks() {
		return this.blocks;
	}

	public void addBlock(Block block) {
		this.blocks.add(block);
	}
}

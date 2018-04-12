package ija.project.schema;

import ija.project.utils.XMLBuilder;
import ija.project.utils.XMLRepresentable;

public class BlockPort implements XMLRepresentable {

	private BlockType blockType;
	private String name;
	private Type type;

	public BlockPort(BlockType blockType, String name, Type type) {
		this.blockType = blockType;
		this.name = name;
		this.type = type;
	}

	public BlockType getBlockType() {
		return blockType;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	@Override
	public void fromXML(XMLBuilder xmlDom) {

	}

	@Override
	public void toXML(XMLBuilder xmlDom) {

	}
}

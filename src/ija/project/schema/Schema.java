package ija.project.schema;

import ija.project.utils.XMLBuilder;
import ija.project.utils.XMLRepresentable;

import java.util.Collection;

public class Schema implements XMLRepresentable {

	private String id;
	private String displayName;

	private Collection<Block> blocks;

	@Override
	public void fromXML(XMLBuilder xmlDom) {

	}

	@Override
	public void toXML(XMLBuilder xmlDom) {

	}

	public Collection<Block> getBlocks() {
		return this.blocks;
	}
}

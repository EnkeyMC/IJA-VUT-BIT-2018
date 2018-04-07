package ija.project.schema;

import ija.project.utils.XMLBuilder;

import java.util.Collection;

public class Schema implements XMLRepresentable {

	private String id;
	private String displayName;

	private Collection<Block> blocks;
	private Collection<BlockConnection> blockConnections;

	@Override
	public void fromXML(XMLBuilder xmlDom) {

	}

	@Override
	public void toXML(XMLBuilder xmlDom) {

	}
}

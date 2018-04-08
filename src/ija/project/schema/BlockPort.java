package ija.project.schema;

import ija.project.utils.XMLBuilder;
import ija.project.utils.XMLRepresentable;

import java.security.KeyException;

public class BlockPort implements XMLRepresentable {

	private Block block;
	private String port;
	private Type type;

	public BlockPort(Block block, String port, Type type) {
		this.block = block;
		this.port = port;
		this.type = type;
	}

	public Block getBlock() {
		return block;
	}

	public String getPort() {
		return port;
	}

	public Type getType() {
		return type;
	}

	public Double getValue(String key) throws KeyException {
		return type.getValue(key);
	}

	public void setValue(String key, Double value) throws KeyException {
		type.setValue(key, value);
	}

	@Override
	public void fromXML(XMLBuilder xmlDom) {

	}

	@Override
	public void toXML(XMLBuilder xmlDom) {

	}
}

package ija.project.schema;

import ija.project.utils.XMLBuilder;
import ija.project.utils.XMLRepresentable;

import java.security.KeyException;

public class BlockPort implements InputPort, OutputPort, XMLRepresentable {

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

	@Override
	public Double getValue(String key) {
		return type.getValue(key);
	}

	@Override
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

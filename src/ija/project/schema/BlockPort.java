package ija.project.schema;

import ija.project.utils.XMLBuilder;
import ija.project.utils.XMLRepresentable;

import java.security.KeyException;

public class BlockPort implements XMLRepresentable {

	private Block block;
	private String port;
	private Type type;
	private BlockPort opposite;

	public BlockPort(Block block, String port, Type type) {
		this.block = block;
		this.port = port;
		this.type = type;
		this.opposite = null;
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

	public BlockPort getOpposite() {
		return this.opposite;
	}

	public void setValue(String key, Double value) throws KeyException {
		type.setValue(key, value);
	}

	public void setOpposite(BlockPort opposite) {
		this.opposite = opposite;
	}

	@Override
	public void fromXML(XMLBuilder xmlDom) {

	}

	@Override
	public void toXML(XMLBuilder xmlDom) {

	}
}

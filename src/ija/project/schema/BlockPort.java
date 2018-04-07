package ija.project.schema;

public class BlockPort implements InputPort, OutputPort {

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
		return null;
	}

	@Override
	public void setValue(String key, Double value) {

	}
}

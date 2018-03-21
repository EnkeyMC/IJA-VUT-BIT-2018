package ija.project.schema;

public class BlockPort {

	private Block block;
	private String port;

	public BlockPort(Block block, String port) {
		this.block = block;
		this.port = port;
	}

	public Block getBlock() {
		return block;
	}

	public String getPort() {
		return port;
	}
}

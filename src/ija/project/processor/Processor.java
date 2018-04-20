package ija.project.processor;

import ija.project.schema.Block;
import ija.project.schema.BlockType;
import ija.project.schema.Schema;

import java.util.ArrayList;

public class Processor {

	/**
	 * Processed schema
	 */
	private Schema schema;

	/**
	 * Blocks that have unconnected output ports
	 */
	private ArrayList<Block> outputBlocks;

	/**
	 * Construct object
	 * @param schema schema to process
	 */
	public Processor(Schema schema) {
		this.schema = schema;
		this.outputBlocks = new ArrayList<>();
	}

	/**
	 * Find blocks that have unconnected output ports
	 */
	public void findOutputBlocks() {
		for (Block block : schema.getBlockCollection()) {
			if (block.hasUnconnectedOutputPort())
				outputBlocks.add(block);
		}
	}

	private void computeBlock(BlockType block) {
		return;
	}

	private void findLoops(BlockType root) throws Exception {
		//ArrayList<Block> blocksVisited = new ArrayList<>();
		//Stack<Block> branch = new Stack();
		//Stack<Integer> indexes = new Stack();
		//branch.push(root);
		//indexes.push(0);

		//Block block;
		//while(!branch.empty()) {
			//block = branch.pop();
			//Integer idx = indexes.pop();
			//Integer size = blocksVisited.size();
			//if (size > idx) {
				//blocksVisited.subList(idx, size-1).clear();
			//}
			//blocksVisited.add(block);
			//for (BlockPort port : block.getInputPorts()) {
				//BlockPort otherPort = port.getOpposite();
				//if (otherPort != null) {
					//BlockType otherBlock = otherPort.getBlockType();
					//if (blocksVisited.contains(otherBlock)) {
						//throw new Exception();
					//}
					//else {
						//branch.push(otherBlock);
						//indexes.push(blocksVisited.size());
					//}
				//}
			//}
		//}
	}

	public ArrayList<Block> getOutputBlocks() {
		return this.outputBlocks;
	}
}

package ija.project.processor;

import ija.project.schema.Schema;
import ija.project.schema.BlockPort;
import ija.project.schema.Block;

import java.util.ArrayList;
import java.util.Stack;

public class Processor {

	private Schema schema;
	private ArrayList<Block> outputBlocks;

	public Processor(Schema schema) {
		this.schema = schema;
		this.outputBlocks = new ArrayList<>();
	}

	public void findOutputBlocks() {
		for (Block block : schema.getBlocks()) {
			for (BlockPort port : block.getOutputPorts()) {
				if (port.getOpposite() == null) {
					this.outputBlocks.add(block);
					break;
				}
			}
		}
	}

	private void computeBlock(Block block) {
		return;
	}

	private void findLoops(Block root) throws Exception {
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
					//Block otherBlock = otherPort.getBlock();
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

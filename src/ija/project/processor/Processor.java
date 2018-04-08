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

	private void findOutputBlocks() {
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
		ArrayList<Block> blocksVisited = new ArrayList<>();
		Stack branch = new Stack();
		Stack indexes = new Stack();
		branch.push(root);
		indexes.push(0);

		Block block;
		while(!branch.empty()) {
			block = (Block)branch.pop();
			int idx = (int)indexes.pop();
			blocksVisited.subList(idx, blocksVisited.size() -1).clear();
			blocksVisited.add(block);
			for (BlockPort port : block.getInputPorts()) {
				BlockPort otherPort = port.getOpposite();
				if (otherPort != null) {
					Block otherBlock = otherPort.getBlock();
					if (blocksVisited.contains(otherBlock)) {
						throw new Exception();
					}
					else {
						branch.push(otherBlock);
						indexes.push(blocksVisited.size());
					}
				}
			}
		}
	}

	public void startComputation() throws Exception {
		findOutputBlocks();
		for (Block block : outputBlocks) {
			findLoops(block);
		}
	}
}

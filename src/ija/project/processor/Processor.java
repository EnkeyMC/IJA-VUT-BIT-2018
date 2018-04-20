package ija.project.processor;

import ija.project.exception.ApplicationException;
import ija.project.schema.Block;
import ija.project.schema.BlockType;
import ija.project.schema.BlockPort;
import ija.project.schema.Schema;

import java.util.HashMap;
import java.util.ArrayList;

public class Processor {

	/**
	 * Processed schema
	 */
	private Schema schema;

	/**
	 * Blocks in computation order
	 */
	private ArrayList<Block> compOrder;

	/**
	 * Construct object
	 * @param schema schema to process
	 */
	public Processor(Schema schema) {
		this.schema = schema;
		this.compOrder = new ArrayList<>();
	}

	/**
	 * Find blocks that have ALL input ports unplugged
	 * @return list of such a blocks
	 */
	private ArrayList<Block> getInputBlocks() {
		ArrayList<Block> inputBlocks = new ArrayList<>();
		for (Block block: schema.getBlockCollection()) {
			if (block.hasAllInputPortsUnplugged())
				inputBlocks.add(block);
		}
		return inputBlocks;
	}

	private void computeBlock(BlockType block) {
		return;
	}

	/**
	 * Find circular dependencies in the schema
	 *
	 * Fills compOrder attribute with blocks in order
	 * in which the computation will be performed.
	 */
	public void findCircularDeps() throws ApplicationException {
		ArrayList<Block> blockList = getInputBlocks();
		HashMap<Block, ArrayList<Block>> connections = new HashMap<>();
		for (Block block: schema.getBlockCollection()) {
			if (!blockList.contains(block)) {
				connections.put(block, new ArrayList<>());
			}
		}

		while(!blockList.isEmpty()) {
			Block block = blockList.remove(0);
			compOrder.add(block);
			for (BlockPort port: block.getBlockType().getOutputPorts()) {
				Block dstBlock = block.getPluggedBlock(port.getName());
				if (dstBlock != null) {
					connections.get(dstBlock).add(block);
					boolean add_flag = true;
					for (BlockPort tmpPort: dstBlock.getBlockType().getInputPorts()) {
						Block tmpBlock = dstBlock.getPluggedBlock(tmpPort.getName());
						if (tmpBlock != null
								&& !connections.get(dstBlock).contains(tmpBlock))
						{
							add_flag = false;
							break;
						}
					}
					if (add_flag)
						blockList.add(dstBlock);
				}
			}
		}

		for (Block block: schema.getBlockCollection()) {
			if (block.hasAllInputPortsUnplugged())
				continue;
			for (BlockPort port: block.getBlockType().getInputPorts()) {
				Block tmpBlock = block.getPluggedBlock(port.getName());
				if (tmpBlock != null && !connections.get(block).contains(tmpBlock)) {
					throw new ApplicationException("Circular dependency found");
				}
			}
		}
	}
}

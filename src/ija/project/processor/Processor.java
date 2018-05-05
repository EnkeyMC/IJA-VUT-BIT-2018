package ija.project.processor;

import ija.project.exception.ApplicationException;
import ija.project.schema.Block;
import ija.project.schema.ValueBlock;
import ija.project.schema.TypeValues;
import ija.project.schema.BlockPort;
import ija.project.schema.Schema;
import javafx.util.Pair;

import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.HashMap;
import java.util.ArrayList;
import java.security.KeyException;

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
	 * Processor is performing step by step calculation
	 */
	private boolean isRunning = false;

	/**
	 * Construct object
	 * @param schema schema to process
	 */
	public Processor(Schema schema) {
		this.schema = schema;
		this.compOrder = new ArrayList<>();
	}

	/**
	 * Find value blocks
	 * @return list of value blocks
	 */
	private ArrayList<Block> getStarterBlocks() {
		ArrayList<Block> starterBlocks = new ArrayList<>();
		for (Block block: schema.getBlockCollection()) {
			if (block.getBlockType().getInputPorts().isEmpty())
				starterBlocks.add(block);
		}
		return starterBlocks;
	}

	/**
	 * Initialize processor if it is not initialized already
	 */
	private void initCalculation() {
		if (!isRunning) {
			everyPortConnected();
			findCircularDeps();
			this.isRunning = true;
		}
	}

	private void everyPortConnected() {
		for (Block block: schema.getBlockCollection()) {
			if (block.hasUnpluggedInputPort())
					throw new ApplicationException(
							"Block '" + block.getBlockType().getDisplayName()
							+ "' (ID " + block.getId() + ")\nUnplugged input port");
			if (block.hasUnpluggedOutputPort())
				throw new ApplicationException(
						"Block '" + block.getBlockType().getDisplayName()
						+ "' (ID " + block.getId() + ")\nUnplugged output port");
		}
	}

	/**
	 * Perform one step of calculation, skip ValueBlocks
	 * @return block computed in current step
	 */
	public Block calculateStep() throws ApplicationException, ParseCancellationException {
		initCalculation();
		while (!compOrder.isEmpty()) {
			Block block = compOrder.remove(0);
			block.calculate();
			moveOutputToInput(block);
			if (ValueBlock.isValueBlock(block.getBlockType()))
				continue;
			return block;
		}

		this.isRunning = false;
		return null;
	}

	/**
	 * Move the results from block's output ports to input ports of connected blocks
	 * @param block block that has been processed already
	 */
	private void moveOutputToInput(Block block) throws ApplicationException {
		for (BlockPort port: block.getBlockType().getOutputPorts()) {
			Pair<Block, String> connected = block.getConnectedBlockAndPort(port.getName());
			if (connected == null || connected.getValue().equals(""))
				continue;

			TypeValues result = block.getOutputPortValues().get(port.getName());
			for (String key: result.getType().getKeys()) {
				try {
					result.getValue(key);
				} catch (KeyException e) {
					throw new ApplicationException(
							"Block '" + block.getBlockType().getDisplayName()
							+ "' (ID " + block.getId() + ")\nMissing formula for "
							+ "port '" + port.getName() + "'");
				}
			}
			connected.getKey().getInputPortValues().put(connected.getValue(), result);
		}
	}

	/**
	 * Perform calculation on blocks, which have not been processed already
	 */
	public void calculateAll() throws ApplicationException, ParseCancellationException {
		initCalculation();
		while (!compOrder.isEmpty()) {
			Block block = compOrder.remove(0);
			block.calculate();
			moveOutputToInput(block);
		}
	}

	/**
	 * Find circular dependencies in the schema
	 *
	 * Fills compOrder attribute with blocks in order
	 * in which the computation will be performed.
	 */
	private void findCircularDeps() {
		ArrayList<Block> blockList = getStarterBlocks();
		HashMap<Block, ArrayList<Block>> connections = new HashMap<>();
		for (Block block: schema.getBlockCollection()) {
			if (!blockList.contains(block)) {
				connections.put(block, new ArrayList<>());
			}
		}

		while (!blockList.isEmpty()) {
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
			if (ValueBlock.isValueBlock(block.getBlockType()))
				continue;
			for (BlockPort port: block.getBlockType().getInputPorts()) {
				Block tmpBlock = block.getPluggedBlock(port.getName());
				if (tmpBlock != null && !connections.get(block).contains(tmpBlock)) {
					throw new ApplicationException("Circular dependency detected");
				}
			}
		}
	}
}

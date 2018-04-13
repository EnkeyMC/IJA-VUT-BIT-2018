package test.ija.project.processor;

import ija.project.processor.Processor;
import ija.project.schema.Block;
import ija.project.schema.BlockType;
import ija.project.schema.Type;
import ija.project.schema.Schema;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class ProcessorTest {

	@Test
	public void testFindOutputBlocks1() {
		ArrayList<Block> outputBlocks = new ArrayList<>();
		Type type = new Type();
		BlockType blockType = new BlockType();

		blockType.addInputPort("blk1-input-1", type);
		blockType.addOutputPort("blk1-output-1", type);

		Block b1 = new Block(blockType);
		Schema schema = new Schema();
		schema.addBlock(b1);

		outputBlocks.add(b1);

		Processor proc = new Processor(schema);
		proc.findOutputBlocks();
		ArrayList<Block> blocks = proc.getOutputBlocks();
		for (Block blk : outputBlocks) {
			assertTrue(blocks.contains(blk));
		}
	}

	@Test
	public void testFindOutputBlocks2() {
		ArrayList<Block> outputBlocks = new ArrayList<>();
		Type type = new Type();

		BlockType blockType1 = new BlockType();
		BlockType blockType2 = new BlockType();

		blockType1.addInputPort("blk1-input-1", type);
		blockType1.addOutputPort("blk1-output-1", type);

		blockType2.addInputPort("blk2-input-1", type);
		blockType2.addOutputPort("blk2-output-1", type);

		Block b1 = new Block(blockType1);
		Block b2 = new Block(blockType2);
		b1.connectTo("blk1-output-1", b2, "blk2-input-1");

		Schema schema = new Schema();
		schema.addBlock(b1);
		schema.addBlock(b2);

		outputBlocks.add(b2);

		Processor proc = new Processor(schema);
		proc.findOutputBlocks();
		ArrayList<Block> blocks = proc.getOutputBlocks();
		for (Block blk : outputBlocks) {
			assertEquals(blocks.contains(blk), true);
		}
	}

	@Test
	public void testFindOutputBlocks3() {
		ArrayList<Block> outputBlocks = new ArrayList<>();
		Type type = new Type();

		BlockType blockType1 = new BlockType();
		BlockType blockType2 = new BlockType();
		BlockType blockType3 = new BlockType();

		blockType1.addInputPort("input-1", type);
		blockType1.addOutputPort("output-1", type);
		blockType1.addOutputPort("output-2", type);

		blockType2.addInputPort("input-1", type);
		blockType2.addOutputPort("output-1", type);

		blockType3.addInputPort("input-1", type);
		blockType3.addInputPort("input-2", type);
		blockType3.addOutputPort("output-1", type);

		Block b1 = new Block(blockType1);
		Block b2 = new Block(blockType2);
		Block b3 = new Block(blockType2);
		Block b4 = new Block(blockType3);

		b1.connectTo("output-1", b2, "input-1");
		b1.connectTo("output-2", b3, "input-1");
		b2.connectTo("output-1", b4, "input-1");
		b3.connectTo("output-1", b4, "input-2");

		Schema schema = new Schema();
		schema.addBlock(b1);
		schema.addBlock(b2);
		schema.addBlock(b3);
		schema.addBlock(b4);

		outputBlocks.add(b4);

		Processor proc = new Processor(schema);
		proc.findOutputBlocks();
		ArrayList<Block> blocks = proc.getOutputBlocks();
		for (Block blk : outputBlocks) {
			assertEquals(blocks.contains(blk), true);
		}
	}

	@Test
	public void testFindOutputBlocks4() {
		ArrayList<Block> outputBlocks = new ArrayList<>();
		Type type = new Type();

		BlockType blockType1 = new BlockType();

		blockType1.addInputPort("input-1", type);
		blockType1.addOutputPort("output-1", type);

		Block block1 = new Block(blockType1);
		Block block2 = new Block(blockType1);
		Block block3 = new Block(blockType1);

		block1.connectTo("output-1", block2, "input-1");
		block2.connectTo("output-1", block3, "input-1");

		Schema schema = new Schema();
		schema.addBlock(block1);
		schema.addBlock(block2);
		schema.addBlock(block3);

		outputBlocks.add(block3);

		Processor proc = new Processor(schema);
		proc.findOutputBlocks();
		ArrayList<Block> blocks = proc.getOutputBlocks();
		for (Block blk : outputBlocks) {
			assertEquals(blocks.contains(blk), true);
		}
	}
}

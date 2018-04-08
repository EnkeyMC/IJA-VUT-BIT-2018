package test.ija.project.processor;

import ija.project.processor.Processor;
import ija.project.schema.Block;
import ija.project.schema.Type;
import ija.project.schema.Schema;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class ProcessorTest {

	@Test
	public void testFindOutputBlocks1() {
		ArrayList<Block> outputBlocks = new ArrayList<>();
		Type type = new Type();

		Block block1 = new Block();

		block1.addInputPort("blk1-input-1", type);
		block1.addOutputPort("blk1-output-1", type);

		Schema schema = new Schema();
		schema.addBlock(block1);

		outputBlocks.add(block1);

		Processor proc = new Processor(schema);
		proc.findOutputBlocks();
		ArrayList<Block> blocks = proc.getOutputBlocks();
		for (Block blk : outputBlocks) {
			assertEquals(blocks.contains(blk), true);
		}
	}

	@Test
	public void testFindOutputBlocks2() {
		ArrayList<Block> outputBlocks = new ArrayList<>();
		Type type = new Type();

		Block block1 = new Block();
		Block block2 = new Block();

		block1.addInputPort("blk1-input-1", type);
		block1.addOutputPort("blk1-output-1", type);

		block2.addInputPort("blk2-input-1", type);
		block2.addOutputPort("blk2-output-1", type);
		block1.connectOutToIn("blk1-output-1", block2, "blk2-input-1");

		Schema schema = new Schema();
		schema.addBlock(block1);
		schema.addBlock(block2);

		outputBlocks.add(block2);

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

		Block block1 = new Block();
		Block block2 = new Block();
		Block block3 = new Block();
		Block block4 = new Block();

		block1.addInputPort("blk1-input-1", type);
		block1.addOutputPort("blk1-output-1", type);
		block1.addOutputPort("blk1-output-2", type);

		block2.addInputPort("blk2-input-1", type);
		block2.addOutputPort("blk2-output-1", type);

		block3.addInputPort("blk3-input-1", type);
		block3.addOutputPort("blk3-output-1", type);

		block4.addInputPort("blk4-input-1", type);
		block4.addInputPort("blk4-input-2", type);
		block4.addOutputPort("blk4-output-1", type);

		block1.connectOutToIn("blk1-output-1", block2, "blk2-input-1");
		block1.connectOutToIn("blk1-output-2", block3, "blk3-input-1");
		block2.connectOutToIn("blk2-output-1", block4, "blk4-input-1");
		block3.connectOutToIn("blk3-output-1", block4, "blk4-input-2");

		Schema schema = new Schema();
		schema.addBlock(block1);
		schema.addBlock(block2);
		schema.addBlock(block3);
		schema.addBlock(block4);

		outputBlocks.add(block4);

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

		Block block1 = new Block();
		Block block2 = new Block();
		Block block3 = new Block();

		block1.addInputPort("blk1-input-1", type);
		block1.addOutputPort("blk1-output-1", type);

		block2.addInputPort("blk2-input-1", type);
		block2.addOutputPort("blk2-output-1", type);

		block3.addInputPort("blk3-input-1", type);
		block3.addOutputPort("blk3-output-1", type);

		block1.connectOutToIn("blk1-output-1", block2, "blk2-input-1");
		block2.connectOutToIn("blk2-output-1", block3, "blk3-input-1");

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

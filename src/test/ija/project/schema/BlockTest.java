package test.ija.project.schema;

import ija.project.schema.Block;
import ija.project.schema.BlockPort;
import ija.project.schema.Type;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlockTest {

	@Test
	public void addInputPort() {
		Block b = new Block();
		Type t = new Type();
		b.addInputPort("inputPort", t);
		boolean found = false;

		for (BlockPort bp : b.getInputPorts()) {
			if (bp.getPort().equals("inputPort")) {
				found = true;
				break;
			}
		}
		assertTrue(found);
	}

	@Test
	public void addOutputPort() {
		Block b = new Block();
		Type t = new Type();
		b.addOutputPort("outputPort", t);
		boolean found = false;

		for (BlockPort bp : b.getOutputPorts()) {
			if (bp.getPort().equals("outputPort")) {
				found = true;
				break;
			}
		}
		assertTrue(found);
	}
}

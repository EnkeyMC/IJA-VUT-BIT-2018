package test.ija.project.schema;

import ija.project.schema.BlockType;
import ija.project.schema.BlockPort;
import ija.project.schema.Type;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlockTypeTest {

	@Test
	public void addInputPort() {
		BlockType b = new BlockType();
		Type t = new Type();
		b.addInputPort("inputPort", t);
		boolean found = false;

		for (BlockPort bp : b.getInputPorts()) {
			if (bp.getName().equals("inputPort")) {
				found = true;
				break;
			}
		}
		assertTrue(found);
	}

	@Test
	public void addOutputPort() {
		BlockType b = new BlockType();
		Type t = new Type();
		b.addOutputPort("outputPort", t);
		boolean found = false;

		for (BlockPort bp : b.getOutputPorts()) {
			if (bp.getName().equals("outputPort")) {
				found = true;
				break;
			}
		}
		assertTrue(found);
	}
}

package ija.project.ui.control.schema;

import ija.project.exception.ApplicationException;
import ija.project.schema.Block;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class BlockControlFactory {
	private static Map<String, Class<? extends BlockControl>> registeredControls = new HashMap<>();

	public static BlockControl create(Block block, SchemaControl schemaControl) {
		if (!registeredControls.containsKey(block.getClass().getName()))
			throw new ApplicationException("Block with class '" + block.getClass().getName() + "' does not have registered control");

		try {
			Constructor<? extends BlockControl> ctor = registeredControls.get(block.getClass().getName()).getConstructor(SchemaControl.class, Block.class);
			return ctor.newInstance(schemaControl, block);
		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			e.printStackTrace();
			return new BlockControl(schemaControl, block);
		}
	}

	public static void registerControl(Class<? extends Block> blockClass, Class<? extends BlockControl> blockControlClass) {
		registeredControls.put(blockClass.getName(), blockControlClass);
	}
}

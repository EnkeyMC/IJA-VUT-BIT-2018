package ija.project.ui.control.schema;

import ija.project.exception.ApplicationException;
import ija.project.schema.Block;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates instances of BlockControls from registered BlockControl subclasses
 */
public class BlockControlFactory {

	/** Registerd BlockControl sublcasses mapped by their Block class */
	private static Map<String, Class<? extends BlockControl>> registeredControls = new HashMap<>();

	/**
	 * Create BlockControl instance from subclass given by Block class
	 * @param block Block instance
	 * @param schemaControl blocks parent schema control
	 * @return new BlockControl instance
	 */
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

	/**
	 * Register BlockControl subclass mapped by Block class
	 * @param blockClass Block class (subclass)
	 * @param blockControlClass BlockControl subclass
	 */
	public static void registerControl(Class<? extends Block> blockClass, Class<? extends BlockControl> blockControlClass) {
		registeredControls.put(blockClass.getName(), blockControlClass);
	}
}

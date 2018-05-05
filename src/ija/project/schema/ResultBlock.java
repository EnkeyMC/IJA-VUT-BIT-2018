package ija.project.schema;

import java.security.KeyException;
import java.util.Map;

/**
 * This block show the calculation result to user
 */
public class ResultBlock extends Block {

	/** XML tag to use for saving/loading */
	public static final String XML_TAG = "resultblock";

	/** Internal values of block */
	private TypeValues values;

	/**
	 * Create blank object
	 */
	public ResultBlock() {
		super();
	}

	/**
	 * Create result block from BlockType. The BlockType has to have only one input port called "value".
	 * @param blockType BlockType
	 */
	public ResultBlock(BlockType blockType) {
		super(blockType);
	}

	@Override
	protected void initFromBlockType(BlockType blockType) {
		super.initFromBlockType(blockType);
		values = new TypeValues(blockType.getInputPort("value").getType());
	}

	@Override
	public void calculate() {
		try {
			for (Map.Entry<String, Double> entry : getInputPortValues().get("value").getValuesMap().entrySet()) {
				values.setValue(entry.getKey(), entry.getValue());
			}
		} catch (KeyException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get internal values
	 * @return values
	 */
	public TypeValues getValues() {
		return values;
	}
}

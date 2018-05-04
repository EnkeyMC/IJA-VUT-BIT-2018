package ija.project.schema;

import java.security.KeyException;
import java.util.Map;

public class ResultBlock extends Block {

	public static final String XML_TAG = "resultblock";

	private TypeValues values;

	public ResultBlock() {
		super();
	}

	public ResultBlock(BlockType blockType) {
		super(blockType);
		values = new TypeValues(blockType.getInputPort("value").getType());
	}

	@Override
	public void calculate() {
		try {
			for (Map.Entry<String, Double> entry : values.getValuesMap().entrySet()) {
				getInputPortValues().get("value").setValue(entry.getKey(), entry.getValue());
			}
		} catch (KeyException e) {
			e.printStackTrace();
		}
	}

	public TypeValues getValues() {
		return values;
	}
}

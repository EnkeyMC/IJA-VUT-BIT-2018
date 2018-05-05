package ija.project.ui.control.schema;

import ija.project.schema.Block;
import ija.project.schema.BlockType;
import ija.project.schema.ResultBlock;
import ija.project.schema.ValueBlock;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.Map;

/**
 * Displays ResultBlock
 */
public class ResultBlockControl extends BlockControl {

	/** Grid for displaying values */
	@FXML
	private GridPane valuesGrid;

	/**
	 * Get path to FXML file for this control
	 * @return path to FXML file relative to FXML root
	 */
	public static String getFXMLPath() {
		return "schema/ValueBlock.fxml";
	}

	/**
	 * Create control from block type, creates the block
	 * @param schemaControl parent schema control
	 * @param blockType BlockType
	 */
	public ResultBlockControl(SchemaControl schemaControl, BlockType blockType) {
		super(schemaControl, blockType);

		addValueListener();
		initBlockValuesLabels();
	}

	/**
	 * Create control from block
	 * @param schemaControl parent schema control
	 * @param block ResultBlock
	 */
	public ResultBlockControl(SchemaControl schemaControl, Block block) {
		super(schemaControl, block);

		addValueListener();
		initBlockValuesLabels();
	}

	/**
	 * Initialize labels for displaying values, can be called to update values
	 */
	private void initBlockValuesLabels() {
		valuesGrid.getChildren().clear();
		Label label;
		int row = 0;
		ResultBlock resultBlock = (ResultBlock) getBlock();
		for (Map.Entry<String, Double> value : resultBlock.getValues().getValuesMap().entrySet()) {
			label = new Label(value.getKey() + ": ");
			valuesGrid.add(label, 0, row);
			GridPane.setHalignment(label, HPos.RIGHT);

			if (value.getValue() != null)
				label = new Label(Double.toString(value.getValue()));
			else
				label = new Label("");
			valuesGrid.add(label, 1, row);

			row++;
		}
	}

	/**
	 * Adds change listener to result block values to update labels
	 */
	private void addValueListener() {
		ResultBlock resultBlock = (ResultBlock) getBlock();
		resultBlock.getValues().getValuesMap().addListener((observable, oldValue, newValue) -> initBlockValuesLabels());
	}
}

package ija.project.ui.control.schema;

import ija.project.schema.Block;
import ija.project.schema.BlockType;
import ija.project.schema.ValueBlock;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.Map;


public class ValueBlockControl extends BlockControl {

	@FXML
	private GridPane valuesGrid;

	public static String getFXMLPath() {
		return "schema/ValueBlock.fxml";
	}

	public ValueBlockControl(SchemaControl schemaControl, BlockType blockType) {
		super(schemaControl, blockType);
		assert ValueBlock.isValueBlock(blockType.getId());

		addValueListener();
		initBlockValuesLabels();
	}

	public ValueBlockControl(SchemaControl schemaControl, Block block) {
		super(schemaControl, block);

		addValueListener();
		initBlockValuesLabels();
	}

	private void initBlockValuesLabels() {
		valuesGrid.getChildren().clear();
		Label label;
		int row = 0;
		ValueBlock valueBlock = (ValueBlock) getBlock();
		for (Map.Entry<String, Double> value : valueBlock.getValues().getValuesMap().entrySet()) {
			label = new Label(value.getKey() + ": ");
			valuesGrid.add(label, 0, row);
			GridPane.setHalignment(label, HPos.RIGHT);

			if (value.getValue() == null)
				label = new Label("");
			else
				label = new Label(Double.toString(value.getValue()));
			valuesGrid.add(label, 1, row);

			row++;
		}
	}

	private void addValueListener() {
		ValueBlock valueBlock = (ValueBlock) getBlock();
		valueBlock.getValues().getValuesMap().addListener((observable, oldValue, newValue) -> initBlockValuesLabels());
	}
}

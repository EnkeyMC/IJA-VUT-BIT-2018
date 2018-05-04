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

public class ResultBlockControl extends BlockControl {

	@FXML
	private GridPane valuesGrid;

	public static String getFXMLPath() {
		return "schema/ValueBlock.fxml";
	}

	public ResultBlockControl(SchemaControl schemaControl, BlockType blockType) {
		super(schemaControl, blockType);

		addValueListener();
		initBlockValuesLabels();
	}

	public ResultBlockControl(SchemaControl schemaControl, Block block) {
		super(schemaControl, block);

		addValueListener();
		initBlockValuesLabels();
	}

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

	private void addValueListener() {
		ResultBlock resultBlock = (ResultBlock) getBlock();
		resultBlock.getValues().getValuesMap().addListener((observable, oldValue, newValue) -> initBlockValuesLabels());
	}
}

package ija.project.ui.controllers.schema;

import ija.project.schema.BlockType;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class BlockController {

	public static String getFXMLPath() {
		return "schema/BlockType.fxml";
	}

	@FXML
	private BorderPane blockPane;


	public void setX(double x) {
		blockPane.layoutXProperty();
	}

	public void setY(double y) {
		blockPane.setLayoutY(y);
	}

	public void setBlockType(BlockType blockType) {

	}
}

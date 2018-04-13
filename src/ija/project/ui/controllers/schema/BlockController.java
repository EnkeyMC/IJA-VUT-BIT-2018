package ija.project.ui.controllers.schema;

import ija.project.schema.Block;
import ija.project.schema.BlockType;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class BlockController {

	public static String getFXMLPath() {
		return "schema/Block.fxml";
	}

	@FXML
	private BorderPane blockPane;

	private Block block;

	public void setX(double x) {
		blockPane.setLayoutX(x);
	}

	public void setY(double y) {
		blockPane.setLayoutY(y);
	}

	public void setBlockType(BlockType blockType) {
		block = new Block(blockType);
		block.xProperty().bindBidirectional(blockPane.layoutXProperty());
		block.yProperty().bindBidirectional(blockPane.layoutYProperty());
	}
}

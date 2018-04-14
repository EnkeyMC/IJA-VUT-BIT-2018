package ija.project.ui.control.schema;

import ija.project.schema.Block;
import ija.project.schema.BlockType;
import ija.project.utils.UIComponentLoader;
import ija.project.utils.UIContolLoader;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class BlockControl extends BorderPane {

	private Block block;

	public static String getFXMLPath() {
		return "schema/Block.fxml";
	}

	public BlockControl(BlockType blockType) {
		super();
		UIContolLoader.load(this);

		block = new Block(blockType);
		block.xProperty().bindBidirectional(this.layoutXProperty());
		block.yProperty().bindBidirectional(this.layoutYProperty());
	}
}

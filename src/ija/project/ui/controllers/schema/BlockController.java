package ija.project.ui.controllers.schema;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class BlockController {

	@FXML
	private BorderPane block;

	public void setX(double x) {
		block.setLayoutX(x);
	}

	public void setY(double y) {
		block.setLayoutY(y);
	}

	public static String getFXMLPath() {
		return "schema/Block.fxml";
	}
}

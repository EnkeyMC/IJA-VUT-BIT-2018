package ija.project.ui.controllers.schema;

import ija.project.schema.Schema;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SchemaController implements Initializable {
	@FXML
	private AnchorPane schemaPane;

	private Schema schema;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.schema = new Schema();
	}

	public static String getFXMLPath() {
		return "schema/Schema.fxml";
	}
}

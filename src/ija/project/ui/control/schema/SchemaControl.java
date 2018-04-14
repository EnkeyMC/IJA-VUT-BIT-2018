package ija.project.ui.control.schema;

import ija.project.register.BlockTypeRegister;
import ija.project.schema.Block;
import ija.project.schema.Schema;
import ija.project.utils.UIComponentLoader;
import ija.project.utils.UIContolLoader;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SchemaControl extends VBox {

	@FXML
	private AnchorPane schemaPane;

	private Schema schema;

	public static String getFXMLPath() {
		return "schema/Schema.fxml";
	}

	public SchemaControl() {
		super();
		UIContolLoader.load(this);

		schema = new Schema();
	}

	public void bindDisplayNameTo(Property property) {
		property.bindBidirectional(schema.displayNameProperty());
	}

	@FXML
	public void mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			BlockControl blockControl = new BlockControl(BlockTypeRegister.getBlockRegistry("Built-in").get(0));
			schemaPane.getChildren().add(blockControl);
			blockControl.setLayoutX(event.getX());
			blockControl.setLayoutY(event.getY());
		}
	}
}

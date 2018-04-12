package ija.project.ui.controllers.schema;

import ija.project.schema.Schema;
import ija.project.utils.UIComponentLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SchemaController implements Initializable {

	public static String getFXMLPath() {
		return "schema/Schema.fxml";
	}

	@FXML
	private AnchorPane schemaPane;

	private Schema schema;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.schema = new Schema();
	}

	@FXML
	public void mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			try {
				UIComponentLoader<BlockController> loader = new UIComponentLoader<>(BlockController.class);
				schemaPane.getChildren().add(loader.load());
				BlockController controller = loader.getController();
				controller.setX(event.getX());
				controller.setY(event.getY());
				System.out.println("Placed block");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

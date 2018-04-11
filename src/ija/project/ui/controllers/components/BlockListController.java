package ija.project.ui.controllers.components;

import ija.project.register.BlockRegister;
import ija.project.schema.Block;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class BlockListController implements Initializable {
	@FXML
	private TitledPane pane;

	@FXML
	private ListView<Block> list;

	public void setBlockType(String type) {
		pane.setText(type);

		list.setItems(BlockRegister.getBlockRegistry(type));
	}

	public static String getFXMLPath() {
		return "components/BlockList.fxml";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		list.setCellFactory(new Callback<ListView<Block>, ListCell<Block>>() {
			@Override
			public ListCell<Block> call(ListView<Block> param) {
				return new ListCell<Block>() {
					@Override
					protected void updateItem(Block item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setText(null);
							setGraphic(null);
						} else {
							setText(item.getDisplayName());
						}
					}
				};
			}
		});
	}
}

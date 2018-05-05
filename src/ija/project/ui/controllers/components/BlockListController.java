package ija.project.ui.controllers.components;

import ija.project.register.BlockTypeRegister;
import ija.project.schema.BlockType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for list of block types in one category
 */
public class BlockListController implements Initializable {
	/** Root pane */
	@FXML
	private TitledPane pane;

	/** ListView of BlockTypes */
	@FXML
	private ListView<BlockType> list;

	/**
	 * Set category for this block list
	 * @param category category display name
	 */
	public void setCategory(String category) {
		pane.setText(category);

		list.setItems(BlockTypeRegister.getBlockRegistry(category));
	}

	/**
	 * Get path to FXML file for this controller
	 * @return path to FXML file relative to FXML root
	 */
	public static String getFXMLPath() {
		return "components/BlockList.fxml";
	}

	/**
	 * Initialize controller
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		list.setCellFactory(new Callback<ListView<BlockType>, ListCell<BlockType>>() {
			@Override
			public ListCell<BlockType> call(ListView<BlockType> param) {
				ListCell<BlockType> cell = new ListCell<BlockType>() {
					@Override
					protected void updateItem(BlockType item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setText(null);
							setGraphic(null);
						} else {
							setText(item.getDisplayName());
						}
					}
				};

				cell.setOnDragDetected(event -> {
						if (!cell.isEmpty()) {
							Dragboard db = cell.startDragAndDrop(TransferMode.COPY);
							ClipboardContent content = new ClipboardContent();
							BlockType blockType = cell.getItem();
							content.putString(pane.getText() + "@" + blockType.getId());
							db.setContent(content);
							event.consume();
						}
					}
				);

				return cell;
			}
		});
	}
}

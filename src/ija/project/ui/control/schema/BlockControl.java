package ija.project.ui.control.schema;

import ija.project.schema.Block;
import ija.project.schema.BlockType;
import ija.project.utils.UIContolLoader;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class BlockControl extends BorderPane {

	private Block block;

	private double dragStartX;
	private double dragStartY;

	public static String getFXMLPath() {
		return "schema/Block.fxml";
	}

	public BlockControl(BlockType blockType) {
		super();
		UIContolLoader.load(this);

		block = new Block(blockType);
		block.xProperty().bindBidirectional(this.layoutXProperty());
		block.yProperty().bindBidirectional(this.layoutYProperty());
		this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		this.setCursor(Cursor.MOVE);
		dragStartX = 0;
		dragStartY = 0;
	}

	public Block getBlock() {
		return block;
	}

	@FXML
	private void onMousePressed(MouseEvent event) {
		if (event.isPrimaryButtonDown()) {
			Point2D local = this.sceneToLocal(event.getSceneX(), event.getSceneY());
			dragStartX = event.getX();
			dragStartY = event.getY();
			event.consume();
		}
	}

	@FXML
	private void onMouseDragged(MouseEvent event) {
		Point2D local = this.getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
		if (local.getX() < 0 || local.getY() < 0)
			return;  // TODO a bit better handling

		this.setLayoutX(local.getX() - dragStartX);
		this.setLayoutY(local.getY() - dragStartY);
		event.consume();
	}
}

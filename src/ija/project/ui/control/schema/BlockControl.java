package ija.project.ui.control.schema;

import ija.project.schema.Block;
import ija.project.schema.BlockPort;
import ija.project.schema.BlockType;
import ija.project.ui.utils.UIContolLoader;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Map;

public class BlockControl extends BorderPane {

	private Block block;

	private SchemaControl schemaControl;

	private ContextMenu inputPortsMenu;
	private ContextMenu outputPortsMenu;

	private double dragStartX;
	private double dragStartY;

	private ReadOnlyDoubleProperty centerX;
	private ReadOnlyDoubleProperty centerY;

	public static String getFXMLPath() {
		return "schema/Block.fxml";
	}

	public BlockControl(SchemaControl schemaControl, BlockType blockType) {
		super();
		UIContolLoader.load(this);
		this.schemaControl = schemaControl;

		// Input port menu
		inputPortsMenu = new ContextMenu();
		ArrayList<BlockPort> ports = blockType.getInputPorts();
		MenuItem item;
		// Add ports to menu
		for (BlockPort port : ports) {
			item = new MenuItem(port.getName());
			// Set action
			item.setOnAction(event -> {
				MenuItem src = (MenuItem) event.getSource();
				schemaControl.endConnection(this, src.getText());
			});
			// Add to menu
			inputPortsMenu.getItems().add(item);
		}
		// Set event listener on showing
		inputPortsMenu.setOnShowing(event -> {
			ContextMenu menu = (ContextMenu) event.getSource();
			for (MenuItem menuItem : menu.getItems()) {
				menuItem.setDisable(this.getBlock().isConnected(menuItem.getText()));
			}
		});

		// Output port menu
		outputPortsMenu = new ContextMenu();
		ports = blockType.getOutputPorts();
		// Add ports to menu
		for (BlockPort port : ports) {
			item = new MenuItem(port.getName());
			// Set action
			item.setOnAction(event -> {
				MenuItem src = (MenuItem) event.getSource();
				schemaControl.startConnection(this, src.getText());
			});
			// Add to menu
			outputPortsMenu.getItems().add(item);
		}
		// Set event listener on showing
		outputPortsMenu.setOnShowing(event -> {
			ContextMenu menu = (ContextMenu) event.getSource();
			for (MenuItem menuItem : menu.getItems()) {
				menuItem.setDisable(this.getBlock().isConnected(menuItem.getText()));
			}
		});

		this.setOnContextMenuRequested(event -> {
			if (this.schemaControl.isBlockConnectingActive()) {
				inputPortsMenu.show(this, event.getScreenX(), event.getScreenY());
			} else {
				outputPortsMenu.show(this, event.getScreenX(), event.getScreenY());
			}
			event.consume();
		});

		block = new Block(blockType);
		block.xProperty().bindBidirectional(this.layoutXProperty());
		block.yProperty().bindBidirectional(this.layoutYProperty());
		this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		this.setCursor(Cursor.MOVE);
		dragStartX = 0;
		dragStartY = 0;

		DoubleProperty property = new SimpleDoubleProperty();
		property.bind(this.layoutXProperty().add(this.widthProperty().divide(2)));
		centerX = property;

		property = new SimpleDoubleProperty();
		property.bind(this.layoutYProperty().add(this.heightProperty().divide(2)));
		centerY = property;
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
		if (event.isPrimaryButtonDown()) {
			Point2D local = this.getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
			if (local.getX() < 0 || local.getY() < 0)
				return;  // TODO a bit better handling

			this.setLayoutX(local.getX() - dragStartX);
			this.setLayoutY(local.getY() - dragStartY);
			event.consume();
		}
	}

	public double getCenterX() {
		return centerX.get();
	}

	public ReadOnlyDoubleProperty centerXProperty() {
		return centerX;
	}

	public double getCenterY() {
		return centerY.get();
	}

	public ReadOnlyDoubleProperty centerYProperty() {
		return centerY;
	}
}

package ija.project.ui.controllers;

import ija.project.register.BlockTypeRegister;
import ija.project.schema.BlockType;
import ija.project.ui.controllers.components.BlockListController;
import ija.project.ui.control.schema.SchemaControl;
import ija.project.utils.UIComponentLoader;
import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainPanelController implements Initializable {

	public static String getFXMLPath() {
		return "MainPanel.fxml";
	}

	@FXML
	private VBox blockList;

	@FXML
	private TabPane tabs;

	private Map<String, BlockListController> blockListControllers;

	@FXML
	private void handleNewSchemaAction(ActionEvent event) {
		this.newScheme("Untitled");
	}

	@FXML
	private void handleCloseSchemaAction(ActionEvent event) {
		Tab tab = tabs.getSelectionModel().getSelectedItem();
		EventHandler<Event> handler = tab.getOnClosed();
		if (null != handler) {
			handler.handle(null);
		} else {
			tab.getTabPane().getTabs().remove(tab);
		}
	}

	@FXML
	private void handleQuitAction(ActionEvent event) {
		Platform.exit();
	}

	@Override
	@FXML
	public void initialize(URL location, ResourceBundle resources) {
		this.newScheme("Untitled");

		this.blockListControllers = new HashMap<>();
		ObservableMap<String, ObservableList<BlockType>> registers = BlockTypeRegister.getAllRegisters();
		registers.addListener((MapChangeListener<String, ObservableList<BlockType>>) change -> {
            if (change.wasAdded()) {  // BlockType type added
                BlockListController controller;
                UIComponentLoader<BlockListController> loader;

                loader = new UIComponentLoader<>(BlockListController.class);
                try {
                    blockList.getChildren().add(loader.load());
                    controller = loader.getController();
                    controller.setCategory(change.getKey());
                    blockListControllers.put(change.getKey(), controller);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {  // BlockType type removed
            	for (Node node : blockList.getChildren()) {
            		if (node instanceof TitledPane) {
						TitledPane pane = (TitledPane) node;
						if (pane.getText().equals(change.getKey())) {
							blockList.getChildren().remove(node);
							break;
						}
					}
				}
			}
        });
	}

	private void newScheme(String name) {
		Tab tab = new Tab(name);
		tabs.getTabs().add(tab);
		SchemaControl schemaControl = new SchemaControl();
		tab.setContent(schemaControl);
		schemaControl.bindDisplayNameTo(tab.textProperty());
	}
}

package ija.project.ui.controllers;

import ija.project.exception.ApplicationException;
import ija.project.exception.XMLWritingException;
import ija.project.register.BlockTypeRegister;
import ija.project.schema.BlockType;
import ija.project.schema.Schema;
import ija.project.ui.controllers.components.BlockListController;
import ija.project.ui.control.schema.SchemaControl;
import ija.project.ui.utils.UIComponentLoader;
import ija.project.xml.XmlDom;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
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

	private final FileChooser fileChooser = new FileChooser();

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
	private void handleSaveSchemaAction(ActionEvent event) {
		Tab tab = tabs.getSelectionModel().getSelectedItem();
		if (!(tab.getContent() instanceof  SchemaControl))
			throw new ApplicationException("Current tab is not schema");
		SchemaControl schemaControl = (SchemaControl) tab.getContent();
		Schema schema = schemaControl.getSchema();

		File file;
		if (schema.getFile() == null) {
			fileChooser.setTitle("Save Schema");
			fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("XML", "*.xml"),
				new FileChooser.ExtensionFilter("All", "*.*")
			);

			file = fileChooser.showSaveDialog(tabs.getScene().getWindow());

		} else {
			file = schema.getFile();
		}

		XmlDom xmlDom = new XmlDom();
		xmlDom.newDocument("root");
		schema.toXML(xmlDom);

		try {
			xmlDom.writeToFile(file);
			schema.setFile(file);
		} catch (XMLWritingException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error occurred");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
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
            if (change.wasAdded()) {  // BlockType types added
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
            } else {  // BlockType types removed
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

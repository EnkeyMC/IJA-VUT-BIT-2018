package ija.project.ui.controllers;

import ija.project.exception.ApplicationException;
import ija.project.exception.XMLWritingException;
import ija.project.register.BlockTypeRegister;
import ija.project.register.ComponentLoader;
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
		this.newSchema(new Schema());
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
		saveSchema((SchemaControl) tab.getContent(), false);
	}

	@FXML
	private void handleSaveAllSchemaAction(ActionEvent event) {
		for (Tab tab : tabs.getTabs()) {
			if (tab.getContent() instanceof SchemaControl) {
				saveSchema((SchemaControl) tab.getContent(), false);
			}
		}
	}

	private void saveSchema(SchemaControl schemaControl, boolean as) {
		Schema schema = schemaControl.getSchema();
		File file;
		if (as ||schema.getFile() == null) {
			fileChooser.setTitle("Save Schema" + (as ? " As" : ""));
			fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("XML", "*.xml"),
				new FileChooser.ExtensionFilter("All", "*.*")
			);

			file = fileChooser.showSaveDialog(tabs.getScene().getWindow());
			if (file == null)
				return;
		} else {
			file = schema.getFile();
		}

		XmlDom xmlDom = new XmlDom();
		xmlDom.newDocument("root");
		schema.toXML(xmlDom);

		try {
			xmlDom.writeToFile(file);
			schema.setFile(file);
			schemaControl.setChanged(false);
		} catch (XMLWritingException e) {
			exceptionAlert("Could not save schema " + schema.getDisplayName(), e);
		}
	}

	@FXML
	private void handleOpenSchemaAction(ActionEvent event) {
		fileChooser.setTitle("Open Schema");
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("XML", "*.xml"),
			new FileChooser.ExtensionFilter("All", "*.*")
		);

		File file = fileChooser.showOpenDialog(tabs.getScene().getWindow());
		if (file == null)
			return;

		Tab inTab = null;
		for (Tab tab : tabs.getTabs()) {
			if (tab.getContent() instanceof SchemaControl) {
				SchemaControl schemaControl = (SchemaControl) tab.getContent();
				if (schemaControl.getSchema().getFile() != null) {
					try {
						if (schemaControl.getSchema().getFile().getCanonicalPath().equals(file.getCanonicalPath())) {
							inTab = tab;
							break;
						}
					} catch (IOException e) {
						exceptionAlert("Could not load schema", e);
					}
				}
			}
		}

		try {
			XmlDom xmlDom = new XmlDom();
			xmlDom.parseFile(file);
			Schema schema = new Schema();
			xmlDom.getCurrentNode("root");
			xmlDom.firstChildNode();
			schema.fromXML(xmlDom);
			schema.setFile(file);
			if (inTab == null) {
				this.newSchema(schema);
			} else {
				SchemaControl schemaControl = new SchemaControl(schema);
				inTab.setContent(schemaControl);
				schemaControl.bindDisplayNameTo(inTab.textProperty());
				tabs.getSelectionModel().select(inTab);
			}
		} catch (Exception e) {
			exceptionAlert("Could not load schema", e);
		}
	}

	@FXML
	private void handleSchemaSaveAsAction(ActionEvent event) {
		Tab tab = tabs.getSelectionModel().getSelectedItem();
		if (!(tab.getContent() instanceof  SchemaControl))
			throw new ApplicationException("Current tab is not schema");
		saveSchema((SchemaControl) tab.getContent(), true);
	}

	@FXML
	private void handleQuitAction(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	private void handleComponentsLoadComponents(ActionEvent event) {
		fileChooser.setTitle("Load Components");
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("XML", "*.xml"),
			new FileChooser.ExtensionFilter("All", "*.*")
		);

		File file = fileChooser.showOpenDialog(tabs.getScene().getWindow());
		if (file == null)
			return;

		try {
			ComponentLoader.loadFromXML(file);
		} catch (Exception e) {
			exceptionAlert("Could not load components", e);
		}
	}

	private void exceptionAlert(String header, Exception e) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error occurred");
		alert.setHeaderText(header);
		alert.setContentText(e.getMessage());
		alert.showAndWait();
	}

	@Override
	@FXML
	public void initialize(URL location, ResourceBundle resources) {
		this.newSchema(new Schema());

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

	private void newSchema(Schema schema) {
		Tab tab = new Tab();
		tabs.getTabs().add(tab);
		tabs.getSelectionModel().select(tab);
		SchemaControl schemaControl = new SchemaControl(schema);
		tab.setContent(schemaControl);
		schemaControl.bindDisplayNameTo(tab.textProperty());
	}
}

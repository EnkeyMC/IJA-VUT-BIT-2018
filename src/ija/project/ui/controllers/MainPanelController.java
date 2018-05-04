package ija.project.ui.controllers;

import ija.project.exception.ApplicationException;
import ija.project.exception.XMLWritingException;
import ija.project.register.BlockTypeRegister;
import ija.project.register.ComponentLoader;
import ija.project.schema.BlockType;
import ija.project.schema.Schema;
import ija.project.ui.controllers.components.BlockListController;
import ija.project.ui.control.schema.SchemaControl;
import ija.project.ui.controllers.components.DetailsPanelController;
import ija.project.ui.utils.UIComponentLoader;
import ija.project.xml.XmlDom;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
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

	@FXML
	private StackPane detailsPane;

	private DetailsPanelController detailsPanelController;

	private Map<String, BlockListController> blockListControllers;

	private final FileChooser fileChooser = new FileChooser();

	@Override
	@FXML
	public void initialize(URL location, ResourceBundle resources) {
		hackTooltipStartTiming();
		UIComponentLoader<DetailsPanelController> uiComponentLoader = new UIComponentLoader<>(DetailsPanelController.class);
		try {
			Parent root = uiComponentLoader.load();
			detailsPane.getChildren().add(root);
			detailsPanelController = uiComponentLoader.getController();
			detailsPanelController.setTabSelectionModel(tabs.getSelectionModel());
		} catch (IOException e) {
			e.printStackTrace();

		}

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

//		root.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
//            if (saveCombination.match(event)) {
//            	Tab tab = tabs.getSelectionModel().getSelectedItem();
//            	if (tab.getContent() instanceof SchemaControl) {
//            		saveSchema((SchemaControl) tab.getContent(), false);
//				}
//			}
//        });
	}

	public void onExit(Event event) {
		boolean changed = false;
		for (Tab tab : tabs.getTabs()) {
			if (tab.getContent() instanceof SchemaControl) {
				SchemaControl schemaControl = (SchemaControl) tab.getContent();
				if (schemaControl.isChanged()) {
					changed = true;
					break;
				}
			}
		}

		if (changed) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Unsaved changes");
			alert.setContentText("You have unsaved changes. Do you want to save them before closing?");
			alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
			alert.showAndWait().ifPresent(buttonType -> {
				if (buttonType == ButtonType.YES) {
					saveAll();
				} else if (buttonType == ButtonType.NO) {
					Platform.exit();
				} else {
					event.consume();
				}
			});
		}
	}

	@FXML
	private void handleNewSchemaAction(ActionEvent event) {
		this.newSchema(new Schema());
	}

	@FXML
	private void handleCloseSchemaAction(ActionEvent event) {
		Tab tab = tabs.getSelectionModel().getSelectedItem();
		EventHandler<Event> handler = tab.getOnClosed();
		if (null != handler) {
			handler.handle(event);
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
		saveAll();
	}

	private void saveAll() {
		for (Tab tab : tabs.getTabs()) {
			if (tab.getContent() instanceof SchemaControl) {
				if (((SchemaControl) tab.getContent()).getSchema().getFile() == null)
					tabs.getSelectionModel().select(tab);
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
		onExit(event);
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

	@FXML
	private void handleAboutAction(ActionEvent event) {
		Stage aboutStage = new Stage();
		aboutStage.setTitle("About");
		aboutStage.getIcons().add(new Image(getClass().getResourceAsStream("/ija/project/resources/images/info.png")));
		UIComponentLoader<AboutController> loader = new UIComponentLoader<>(AboutController.class);
		try {
			aboutStage.setScene(new Scene(loader.load(), 350, 200));
			aboutStage.setMinWidth(350);
			aboutStage.setMinHeight(200);
			aboutStage.setMaxWidth(350);
			aboutStage.setMaxHeight(200);
			aboutStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void exceptionAlert(String header, Exception e) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error occurred");
		alert.setHeaderText(header);
		alert.setContentText(e.getMessage());
		alert.showAndWait();
	}

	private void newSchema(Schema schema) {
		Tab tab = new Tab();
		tabs.getTabs().add(tab);
		tabs.getSelectionModel().select(tab);

		SchemaControl schemaControl = new SchemaControl(schema);
		tab.setContent(schemaControl);
		schemaControl.bindDisplayNameTo(tab.textProperty());

		tab.setOnCloseRequest(event -> {
			if (schemaControl.isChanged()) {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Unsaved changes");
				alert.setContentText("You have unsaved changes. Do you want to save them before closing?");
				alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
				alert.showAndWait().ifPresent(buttonType -> {
					if (buttonType == ButtonType.YES) {
						saveSchema(schemaControl, false);
					} else if (buttonType == ButtonType.CANCEL) {
						event.consume();
					}
				});
			}
		});

		detailsPanelController.addSchemaSelectionModel(schemaControl.getSelectionModel());
	}

	/**
	 * Hack tooltip show delay (taken from https://stackoverflow.com/questions/26854301/how-to-control-the-javafx-tooltips-delay)
	 */
	public static void hackTooltipStartTiming() {
		try {
			Tooltip tooltip = new Tooltip();
			Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
			fieldBehavior.setAccessible(true);
			Object objBehavior = fieldBehavior.get(tooltip);

			Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
			fieldTimer.setAccessible(true);
			Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

			objTimer.getKeyFrames().clear();
			objTimer.getKeyFrames().add(new KeyFrame(new Duration(250)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

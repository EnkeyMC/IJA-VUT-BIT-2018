package ija.project.ui.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainPanelController {

	@FXML
	private TabPane tabs;

	@FXML
	private void handleNewSchemaAction(ActionEvent event) {
		tabs.getTabs().add(new Tab("Test"));
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
}

package ija.project.ui.controllers;

import ija.project.utils.UIComponentLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPanelController implements Initializable {

	@FXML
	private TabPane tabs;

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
	}

	private void newScheme(String name) {
		Tab tab = new Tab(name);
		tabs.getTabs().add(tab);
		try {
			tab.setContent(UIComponentLoader.loadComponent("schema/Schema.fxml"));
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}

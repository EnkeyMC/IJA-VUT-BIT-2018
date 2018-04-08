package ija.project.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class UIComponentLoader {
	public static Parent loadComponent(String path) throws IOException {
		return FXMLLoader.load(UIComponentLoader.class.getResource("/ija/project/ui/fxml/" + path));
	}
}

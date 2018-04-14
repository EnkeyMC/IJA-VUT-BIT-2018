package ija.project.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UIComponentLoader<T> {

	private Class<T> componentClass;
	private FXMLLoader loader;

	public UIComponentLoader(Class<T> componentClass) {
		this.componentClass = componentClass;
		loader = new FXMLLoader(UIComponentLoader.class.getResource("/ija/project/ui/fxml/" + this.getComponentFXML()));
	}

	public FXMLLoader getLoader() {
		return loader;
	}

	public Parent load() throws IOException {
		return loader.load();
	}

	public T getController() {
		return loader.getController();
	}

	private String getComponentFXML() {
		String path = "";
		try {
			Method m = this.componentClass.getMethod("getFXMLPath");
			path = (String) m.invoke(null);

		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return path;
	}
}

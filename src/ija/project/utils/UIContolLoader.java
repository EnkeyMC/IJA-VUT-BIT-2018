package ija.project.utils;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UIContolLoader {
	public static void load(Object obj) {
		FXMLLoader loader = new FXMLLoader(UIComponentLoader.class.getResource("/ija/project/ui/fxml/" + getComponentFXML(obj)));
		loader.setRoot(obj);
		loader.setController(obj);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getComponentFXML(Object obj) {
		String path = "";
		try {
			Method m = obj.getClass().getMethod("getFXMLPath");
			path = (String) m.invoke(null);

		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return path;
	}
}

package ija.project.ui.utils;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Load control from FXML file
 */
public class UIContolLoader {

	/**
	 * Load component from given root node controller
	 * @param obj root node that is controller for this control
	 */
	public static void load(Object obj) {
		FXMLLoader loader = new FXMLLoader(UIComponentLoader.class.getResource("/ija/project/resources/fxml/" + getComponentFXML(obj)));
		loader.setRoot(obj);
		loader.setController(obj);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get FXML file for component using it's static method getFXMLPath
	 * @param obj controller object
	 * @return path to FXML file
	 */
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

package ija.project.ui.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Load UI component from FXML
 *
 * Example usage:
 * <code>
 *     UIComponentLoader&lt;MainPanelController&gt; loader = new UIComponentLoader&lt;&gt;(MainPanelController.class);
 *     Parent root = loader.load();
 *     ...
 * </code>
 *
 * @param <T> Components controller class (has to have public static getFXMLPath method that returns path to it's FXML file)
 */
public class UIComponentLoader<T> {

	/** Component class */
	private Class<T> componentClass;
	/** FXMLLoader instance */
	private FXMLLoader loader;

	/**
	 * Create component loader
	 * @param componentClass Component controller class (has to be same as parameter T)
	 */
	public UIComponentLoader(Class<T> componentClass) {
		this.componentClass = componentClass;
		loader = new FXMLLoader(UIComponentLoader.class.getResource("/ija/project/resources/fxml/" + this.getComponentFXML()));
	}

	/**
	 * Get loader instance
	 * @return loader
	 */
	public FXMLLoader getLoader() {
		return loader;
	}

	/**
	 * Load component
	 * @return root node
	 * @throws IOException if FXML file is not found
	 */
	public Parent load() throws IOException {
		return loader.load();
	}

	/**
	 * Get controller instance for loaded component. Component has to be loaded first!
	 * @return controller instance
	 */
	public T getController() {
		return loader.getController();
	}

	/**
	 * Get path to FXML file from controller class (static method getFXMLPath)
	 * @return path to FXML file
	 */
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

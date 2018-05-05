package ija.project.ui.control;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Extends horizontally if placed in HBox or vertically if placed in VBox
 */
public class Spacer extends Region {
	/**
	 * Constructor
	 */
	public Spacer() {
		super();
		HBox.setHgrow(this, Priority.ALWAYS);
		VBox.setVgrow(this, Priority.ALWAYS);
	}
}

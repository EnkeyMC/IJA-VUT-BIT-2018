package ija.project.ui.control.schema;

/**
 * Allows node to be selected in schema that has {@link SchemaSelectionModel}
 */
public interface Selectable {
	/** Class that should be added to selected nodes */
	String SELECTED_CLASS = "selected";
	/** Called when node is selected */
	void onSelected();
	/** Called when node is deselected */
	void onDeselected();
}

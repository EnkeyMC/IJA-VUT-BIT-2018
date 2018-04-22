package ija.project.ui.control.schema;

public interface Selectable {
	String SELECTED_CLASS = "selected";
	void onSelected();
	void onDeselected();
}

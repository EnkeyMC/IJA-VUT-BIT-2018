package ija.project.ui.control.schema;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Handles node selection in schema, only {@link Selectable} objects can be selected
 */
public class SchemaSelectionModel {
	/** Currently selected node property */
	private ObjectProperty<Selectable> selectedNode;

	/**
	 * Create selection model on given schema pane
	 * @param schemaPane schema pane
	 */
	public SchemaSelectionModel(Pane schemaPane) {
		selectedNode = new SimpleObjectProperty<>();

		selectedNode.addListener((observable, oldValue, newValue) -> {
			if (oldValue != null)
				oldValue.onDeselected();
			if (newValue != null)
				newValue.onSelected();
		});

		schemaPane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				if (event.getTarget() instanceof Node) {
					Node node = (Node) event.getTarget();

					while (!(node instanceof Selectable)) {
						node = node.getParent();

						if (node == null || node == schemaPane) {
							selectedNode.setValue(null);
							return;
						}
					}

					selectedNode.setValue((Selectable) node);
				}
			}
		});
	}

	/**
	 * Get currently selected node
 	 * @return selected node
	 */
	public Selectable getSelectedNode() {
		return selectedNode.get();
	}

	/**
	 * Set selected node
	 * @param selectedNode new node to select
	 */
	public void setSelectedNode(Selectable selectedNode) {
		this.selectedNode.set(selectedNode);
	}

	/**
	 * Get selected node property
	 * @return selected node property
	 */
	public ObjectProperty<Selectable> selectedNodeProperty() {
		return selectedNode;
	}
}

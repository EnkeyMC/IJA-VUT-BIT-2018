package ija.project.ui.control.schema;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class SchemaSelectionModel {
	private ObjectProperty<Selectable> selectedNode;

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

	public Selectable getSelectedNode() {
		return selectedNode.get();
	}

	public void setSelectedNode(Selectable selectedNode) {
		this.selectedNode.set(selectedNode);
	}

	public ObjectProperty<Selectable> selectedNodeProperty() {
		return selectedNode;
	}
}

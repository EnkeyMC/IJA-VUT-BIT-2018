package ija.project.ui.control.schema;

import javafx.beans.property.ReadOnlyDoubleProperty;

/**
 * Dummy port control for connection preview
 */
public class DummyBlockPortControl extends BlockPortControl {

	/**
	 * Create dummy port control
	 */
	public DummyBlockPortControl() {

	}

	@Override
	public ReadOnlyDoubleProperty connectionXProperty() {
		return layoutXProperty();
	}

	@Override
	public ReadOnlyDoubleProperty connectionYProperty() {
		return layoutYProperty();
	}
}

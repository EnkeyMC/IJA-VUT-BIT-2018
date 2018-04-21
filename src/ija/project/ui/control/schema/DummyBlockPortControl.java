package ija.project.ui.control.schema;

import javafx.beans.property.ReadOnlyDoubleProperty;

public class DummyBlockPortControl extends BlockPortControl {
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

package ija.project.ui.controllers.components;

import ija.project.schema.Block;
import ija.project.schema.BlockPort;
import ija.project.schema.ValueBlock;
import ija.project.ui.control.schema.*;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.security.KeyException;
import java.util.HashMap;
import java.util.Map;

public class DetailsPanelController {

	@FXML
	private GridPane blockInfo;
	@FXML
	private GridPane blockPorts;

	private static final Label BLOCK_NAME_LABEL = new Label("Name");
	private static final Label BLOCK_TYPE_ID = new Label("Type ID");
	private static final Label BLOCK_SCHEMA_ID = new Label("Schema ID");
	private static final Label BLOCK_FORMULAS_LABEL = new Label("Formulas");
	private static final Label BLOCK_INPUT_PORTS = new Label("Input ports:");
	private static final Label BLOCK_OUTPUT_PORTS = new Label("Output ports:");
	private static final Label VALUE_BLOCK_VALUES = new Label("Values:");

	private Block attachedBlock;
	private Map<String, TextField> portFields = new HashMap<>();
	private Map<String, TextField> valueFields = new HashMap<>();

	private ChangeListener<Selectable> schemaChangeListener = (observable, oldValue, newValue) -> {
		if (newValue instanceof BlockControl)
			changeDetails((BlockControl) newValue);
		else
			changeDetails(null);
	};

	private ChangeListener<Tab> tabChangeListener = (observable, oldValue, newValue) -> {
		if (newValue.getContent() instanceof SchemaControl) {
			SchemaControl schemaControl = (SchemaControl) newValue.getContent();
			Selectable node = schemaControl.getSelectionModel().getSelectedNode();
			if (node instanceof BlockControl)
				changeDetails((BlockControl) node);
			else
				changeDetails(null);
		} else {
			changeDetails(null);
		}
	};

	private MapChangeListener<String, Pair<Block, String>> portMapChangeListener = change -> {
		BlockPort blockPort;
		if (attachedBlock.isInputPort(change.getKey()))
			blockPort = attachedBlock.getBlockType().getInputPort(change.getKey());
		else
			blockPort = attachedBlock.getBlockType().getOutputPort(change.getKey());
		updateTextField(portFields.get(change.getKey()), attachedBlock, blockPort);
	};

	private EventHandler<ActionEvent> textFieldActionListener = event -> {
		TextField field = (TextField) event.getSource();
		ValueBlock valueBlock = (ValueBlock) attachedBlock;
		try {
			Double value = Double.valueOf(field.getText());
			valueBlock.getValues().setValue(field.getId(), value);
		} catch (NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Invalid value");
			alert.setHeaderText("Invalid input value");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		} catch (KeyException e) {
			e.printStackTrace();
		}
	};

	public static String getFXMLPath() {
		return "components/DetailsPanel.fxml";
	}

	public void addSchemaSelectionModel(SchemaSelectionModel schemaSelectionModel) {
		schemaSelectionModel.selectedNodeProperty().addListener(this.schemaChangeListener);
	}

	public void setTabSelectionModel(SingleSelectionModel<Tab> tabSelectionModel) {
		tabSelectionModel.selectedItemProperty().addListener(this.tabChangeListener);
	}

	private void changeDetails(BlockControl blockControl) {
		blockInfo.getChildren().clear();
		blockPorts.getChildren().clear();

		if (attachedBlock != null) {
			attachedBlock.getConnections().removeListener(this.portMapChangeListener);
		}

		if (blockControl == null)
			return;

		attachedBlock = blockControl.getBlock();
		attachedBlock.getConnections().addListener(this.portMapChangeListener);

		blockInfo.add(BLOCK_NAME_LABEL, 0, 0);
		blockInfo.add(new Label(blockControl.getBlock().getBlockType().getDisplayName()), 1, 0);
		blockInfo.add(BLOCK_TYPE_ID, 0, 1);
		blockInfo.add(new Label(blockControl.getBlock().getBlockType().getId()), 1, 1);
		blockInfo.add(BLOCK_SCHEMA_ID, 0, 2);
		blockInfo.add(new Label(Long.toString(blockControl.getBlock().getId())), 1, 2);
		blockInfo.add(BLOCK_FORMULAS_LABEL, 0, 3);

		int row = 1;
		if (blockControl instanceof ValueBlockControl) {
			ValueBlock valueBlock = (ValueBlock) attachedBlock;

			TextField textField;
			Label label;
			blockPorts.add(VALUE_BLOCK_VALUES, 0, 0, 2, 1);
			for (Map.Entry<String, Double> value : valueBlock.getValues().getValuesMap().entrySet()) {
				textField = new TextField();
				if (value.getValue() != null)
					textField.setText(Double.toString(value.getValue()));
				valueFields.put(value.getKey(), textField);
				textField.setId(value.getKey());
				textField.setOnAction(this.textFieldActionListener);

				label = new Label(value.getKey());
				blockPorts.add(label, 0, row);
				blockPorts.add(textField, 1, row);
				GridPane.setHalignment(label, HPos.RIGHT);
				row++;
			}
		} else {
			Label label;
			blockPorts.add(BLOCK_INPUT_PORTS, 0, 0, 2, 1);
			for (BlockPort blockPort : blockControl.getBlock().getBlockType().getInputPorts()) {
				label = new Label(blockPort.getName() + " (" + blockPort.getType().getDisplayName() + ")");
				blockPorts.add(label, 0, row);
				blockPorts.add(createPortField(blockControl.getBlock(), blockPort), 1, row);
				GridPane.setHalignment(label, HPos.RIGHT);
				row++;
			}
		}

		blockPorts.add(BLOCK_OUTPUT_PORTS, 0, row, 2, 1);
		row++;
		for (BlockPort blockPort : blockControl.getBlock().getBlockType().getOutputPorts()) {
			blockPorts.add(new Label(blockPort.getName() + " (" + blockPort.getType().getDisplayName() + ")"), 0, row);
			blockPorts.add(createPortField(blockControl.getBlock(), blockPort), 1, row);
			row++;
		}
	}

	private TextField createPortField(Block block, BlockPort blockPort) {
		TextField textField = new TextField();
		updateTextField(textField, block, blockPort);
		portFields.put(blockPort.getName(), textField);
		return textField;
	}

	private void updateTextField(TextField textField, Block block, BlockPort blockPort) {
		if (block.getConnectedBlockAndPort(blockPort.getName()) != null) {
			Pair<Block, String> connectedBlockPort = block.getConnectedBlockAndPort(blockPort.getName());
			textField.setText(connectedBlockPort.getKey().getBlockType().getId()
					+ "(" + block.getId() + ")" + ":" + connectedBlockPort.getValue());
		} else {
			textField.setText("UNCONNECTED");
		}
		textField.setEditable(false);
	}
}

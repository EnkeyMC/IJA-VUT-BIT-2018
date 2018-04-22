package ija.project.schema;

import ija.project.exception.XMLParsingException;
import ija.project.xml.XmlActiveNode;
import ija.project.xml.XmlRepresentable;

import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.ParseTree;

import ija.project.parser.FormulaParser;
import ija.project.parser.EvalVisitor;

import java.util.Map;

/**
 * Formula represents a way to transform input values to output value
 */
public class Formula implements XmlRepresentable {

	/* Formula text */
	private String formulaText;

	/**
	 * Transform input values to output values
	 * @param inputPorts input ports with values
	 * @param outputPorts output ports to transform values to
	 */
	public void transform(Map<String, TypeValues> inputPorts, Map<String, TypeValues> outputPorts) throws ParseCancellationException {
		ParseTree parseTree = FormulaParser.getParseTree(this.formulaText);
		EvalVisitor visitor = new EvalVisitor(inputPorts, outputPorts);
		visitor.visit(parseTree);
	}

	@Override
	public void fromXML(XmlActiveNode xmlDom) throws XMLParsingException {
		xmlDom.getCurrentNode("formula");
		this.formulaText = xmlDom.getText();
	}

	@Override
	public void toXML(XmlActiveNode xmlDom) {
		xmlDom.createChildElement("formula");
		xmlDom.setText(this.formulaText);
		xmlDom.parentNode();
	}
}

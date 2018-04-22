package test.ija.project.parser;

import ija.project.parser.FormulaParser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import org.junit.Test;
import static org.junit.Assert.*;

public class FormulaParserTest {

	@Test(expected = ParseCancellationException.class)
	public void invalidSymbol() {
		String expr = "port.a = $a + $b";
		ParseTree parseTree = FormulaParser.getParseTree(expr);
	}

	@Test(expected = ParseCancellationException.class)
	public void invalidNumber1() {
		String expr = "port.a = 00 + b.x";
		ParseTree parseTree = FormulaParser.getParseTree(expr);
	}

	@Test(expected = ParseCancellationException.class)
	public void invalidNumber2() {
		String expr = "port.a = 0.e2 + b.x";
		ParseTree parseTree = FormulaParser.getParseTree(expr);
	}

	@Test(expected = ParseCancellationException.class)
	public void invalidNumber3() {
		String expr = "port.a = 12e--2 + b.x";
		ParseTree parseTree = FormulaParser.getParseTree(expr);
	}

	@Test(expected = ParseCancellationException.class)
	public void missingValueKey() {
		String expr = "port.a = a.x + b";
		ParseTree parseTree = FormulaParser.getParseTree(expr);
	}

	@Test(expected = ParseCancellationException.class)
	public void missingOperator() {
		String expr = "port.a = a.x b.y";
		ParseTree parseTree = FormulaParser.getParseTree(expr);
	}

	@Test(expected = ParseCancellationException.class)
	public void missingAssignment() {
		String expr = "c.a  a.x ^ b.y";
		ParseTree parseTree = FormulaParser.getParseTree(expr);
	}

	@Test(expected = ParseCancellationException.class)
	public void missingOperand() {
		String expr = "port.a = a.x * ";
		ParseTree parseTree = FormulaParser.getParseTree(expr);
	}
}

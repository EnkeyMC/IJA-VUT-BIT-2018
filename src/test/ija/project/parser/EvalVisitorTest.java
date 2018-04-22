package test.ija.project.parser;

import ija.project.parser.FormulaParser;
import ija.project.parser.EvalVisitor;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.misc.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class EvalVisitorTest {

	@Test
	public void testSimpleAssignment() {
		Double d = 42.;
		assertEquals(expr("port.key = 42.00000000"), d);
	}

	@Test
	public void testAddPortValues() {
		Double d = 0.;
		// Port1.x and port2.y returns 0
		assertEquals(expr("port.key = port1.x + port2.y - port3.z"), d);
	}

	@Test
	public void testAdd() {
		Double d = 24.8;
		assertEquals(expr("port.key = 24 + .8"), d);
	}

	@Test
	public void testSub() {
		Double d = -30.;
		assertEquals(expr("port.key = 0e+10-30"), d);
	}

	@Test
	public void testUnaryMinus() {
		Double d = -2.0;
		assertEquals(expr("port.key = ---2."), d);
	}

	@Test
	public void testUnaryPlus() {
		Double d = 2.0;
		assertEquals(expr("port.key = +++++++2."), d);
	}

	@Test
	public void testPow() {
		Double d = 16.;
		assertEquals(expr("port.key = 2e0 ^4"), d);
	}

	@Test
	public void testMul() {
		Double d = 9.;
		assertEquals(expr("port.key = 3*3"), d);
	}

	@Test
	public void testMod() {
		Double d = 3.;
		assertEquals(expr("port.key = 15%4"), d);
	}

	@Test
	public void testDiv() {
		Double d = 15/4.025;
		assertEquals(expr("port.key = 15.0/4.025"), d);
	}

	@Test(expected = ParseCancellationException.class)
	public void ZeroDivision() {
		expr("a.x = 1/0");
	}

	@Test
	public void testAddSub() {
		Double d = 3.;
		assertEquals(expr("port.key = 10\t - 10\t + 3"), d);
	}

	@Test
	public void testAddMul() {
		Double d = 14.;
		Double f = 6.;
		assertEquals(expr("port.key = 2 + 3 * 4"), d);
		assertEquals(expr("\t port.key = 2.*2.+2.0"), f);
	}

	@Test
	public void testSingleParens() {
		Double d = 15.;
		Double f = 12.;
		assertEquals(expr("a.x = (2+3)*3"), d);
		assertEquals(expr("b.y = 2*(3+3)"), f);
	}

	@Test
	public void testCompoundExpressions() {
		Double d = 2.0;
		Double f = -2.0;
		assertEquals(expr("a.x = 2^4/2/2/2"), d);
		assertEquals(expr("b.y = -(10 - ((9))) * 2"), f);
	}

	private Double expr(String expression) {
		ParseTree parseTree = FormulaParser.getParseTree(expression);
		return new EvalVisitor()
			.visit(parseTree);
	}
}

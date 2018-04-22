package ija.project.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;

import antlr.parse.ExpressionParser;
import antlr.parse.ExpressionBaseVisitor;

import ija.project.schema.TypeValues;

import java.security.KeyException;
import java.util.Map;

/**
 * Expression evaluator
 */
public class EvalVisitor extends ExpressionBaseVisitor<Double> {

	/** Load data from */
	private Map<String, TypeValues> inputPorts;
	/** Store result in */
	private Map<String, TypeValues> outputPorts;

	/** Ease unittesting */
	public EvalVisitor() {  }

	public EvalVisitor(Map<String, TypeValues> inputPorts,Map<String, TypeValues> outputPorts) {
		this.inputPorts = inputPorts;
		this.outputPorts = outputPorts;
	}

	/** port.key = expr */
	@Override
	public Double visitParse(ExpressionParser.ParseContext ctx) {
		String port = ctx.port.getText();
		String key = ctx.key.getText();
		Double result = visit(ctx.expr());
		if (this.outputPorts != null) {
			if (!outputPorts.containsKey(port))
				throw new ParseCancellationException(
						"Output port '" + port + "' does not exist");
			try {
					this.outputPorts.get(port).setValue(key, result);
			}
			catch (KeyException e) {
				throw new ParseCancellationException(
						"Value name '" + key + "' is not valid for port '" + port + "'");
			}
		}
		return result;
	}

	/** expr = expr ('*'|'/'|'%') expr */
	@Override
	public Double visitMulDivMod(ExpressionParser.MulDivModContext ctx) {
		Double left = visit(ctx.left);
		Double right = visit(ctx.right);
		Double result = 0.;

		if (ctx.MUL() != null) {
			result = left * right;
		}
		if (ctx.DIV() != null) {
			if (right == 0.)
				throw new ParseCancellationException("Zero division");

			result = left / right;
		}
		if (ctx.MOD() != null) {
			result = left % right;
		}

		return result;
	}

	/** NUMBER */
	@Override
	public Double visitNumber(ExpressionParser.NumberContext ctx) {
		return Double.valueOf(ctx.value.getText());
	}

	/** ( expr ) */
	@Override
	public Double visitParen(ExpressionParser.ParenContext ctx) {
		return visit(ctx.expr());
	}

	/** port.key */
	@Override
	public Double visitName(ExpressionParser.NameContext ctx) {
		String port = ctx.port.getText();
		String key = ctx.key.getText();
		if (inputPorts != null) {
			if (!inputPorts.containsKey(port))
				throw new ParseCancellationException(
						"Input port '" + port + "' does not exist");
			try {
				return this.inputPorts.get(port).getValue(key);
			}
			catch (KeyException e) {
				throw new ParseCancellationException(
						"Value name '" + key + "' is not valid for port '" + port + "'");
			}
		}
		return 0.;
	}

	/** expr ^ expr */
	@Override
	public Double visitPow(ExpressionParser.PowContext ctx) {
		Double left = visit(ctx.left);
		Double right = visit(ctx.right);
		return Double.valueOf(Math.pow(left, right));
	}

	/** epxr ('+'|'-') expr */
	@Override
	public Double visitAddSub(ExpressionParser.AddSubContext ctx) {
		Double left = visit(ctx.left);
		Double right = visit(ctx.right);
		Double result = 0.;

		if (ctx.ADD() != null) {
			result = left + right;
		}
		if (ctx.SUB() != null) {
			result = left - right;
		}

		return result;
	}

	/** ('+'|'-') expr */
	@Override
	public Double visitUnary(ExpressionParser.UnaryContext ctx) {
		Double value = visit(ctx.value);
		if (ctx.SUB() != null) {
			return -value;
		}
		// Do nothing in case of unary plus
		return value;
	}
}

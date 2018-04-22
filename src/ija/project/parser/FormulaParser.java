package ija.project.parser;

import ija.project.exception.ExpressionException;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import antlr.parse.ExpressionLexer;
import antlr.parse.ExpressionParser;

import java.util.HashMap;

/**
 * Formula parser creates parse tree for the given formula
 */
public class FormulaParser {

	/* Store parse trees of already parsed formulas. */
	private static HashMap<String, ParseTree> parseTrees = new HashMap<>();

	public static ParseTree getParseTree(String formula) throws ExpressionException {
		if (parseTrees.containsKey(formula)) {
			return parseTrees.get(formula);
		}

		CodePointCharStream chars = CharStreams.fromString(formula);
		SimpleLexer lexer = new SimpleLexer(chars);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ExpressionParser parser = new ExpressionParser(tokens);
		parser.setBuildParseTree(true);
		parser.removeErrorListeners();
		parser.addErrorListener(new SyntaxErrorListener());
		ParseTree tree = parser.parse();
		parseTrees.put(formula, tree);
		return tree;
	}

	/** Do not recover from lexical error */
	public static class SimpleLexer extends ExpressionLexer {

		public SimpleLexer(CharStream input) {
			super(input);
		}

		public void recover(LexerNoViableAltException e) {
			throw new ExpressionException(e.getMessage());
		}
	}

	/** Throw exception or syntax error */
	public static class SyntaxErrorListener extends BaseErrorListener {

		@Override
		public void syntaxError(Recognizer<?, ?> recognizer,
								Object offendingSymbol,
								int line, int charPositionInLine,
								String msg,
								RecognitionException e)
		{
			throw new ExpressionException(msg);
		}
	}
}

package ija.project.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;

import antlr.parse.ExpressionLexer;
import antlr.parse.ExpressionParser;

import java.util.HashMap;

/**
 * Formula parser creates parse tree for the given formula
 */
public class FormulaParser {

	/** Store parse trees of already parsed formulas. */
	private static HashMap<String, ParseTree> parseTrees = new HashMap<>();

	public static ParseTree getParseTree(String formula) throws ParseCancellationException {
		if (parseTrees.containsKey(formula)) {
			return parseTrees.get(formula);
		}

		CodePointCharStream chars = CharStreams.fromString(formula);

		ExpressionLexer lexer = new ExpressionLexer(chars);
		lexer.removeErrorListeners();
		lexer.addErrorListener(ThrowingErrorListener.INSTANCE);

		CommonTokenStream tokens = new CommonTokenStream(lexer);

		ExpressionParser parser = new ExpressionParser(tokens);
		parser.setBuildParseTree(true);
		parser.removeErrorListeners();
		parser.addErrorListener(ThrowingErrorListener.INSTANCE);

		ParseTree tree = parser.parse();
		parseTrees.put(formula, tree);
		return tree;
	}

	/** Throw exception on syntax/lexical error */
	public static class ThrowingErrorListener extends BaseErrorListener {

		public static final ThrowingErrorListener INSTANCE = new ThrowingErrorListener();

		@Override
		public void syntaxError(Recognizer<?, ?> recognizer,
								Object offendingSymbol,
								int line, int charPositionInLine,
								String msg,
								RecognitionException e) throws ParseCancellationException
		{
			throw new ParseCancellationException(msg);
		}
	}
}

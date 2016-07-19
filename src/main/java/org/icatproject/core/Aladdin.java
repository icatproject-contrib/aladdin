package org.icatproject.core;

import java.io.Console;

import org.icatproject.core.oldparser.OldInput;
import org.icatproject.core.oldparser.OldLexerException;
import org.icatproject.core.oldparser.OldParserException;
import org.icatproject.core.oldparser.OldSearchQuery;
import org.icatproject.core.oldparser.OldTokenizer;

public class Aladdin {

	public static void main(String[] args) {
		Console c = System.console();
		if (c == null) {
			System.err.println("No console.");
			System.exit(1);
		}

		String query = c.readLine("Enter your old style query: ");

		try {
			OldSearchQuery oldSearchQuery = new OldSearchQuery(new OldInput(OldTokenizer.getTokens(query)));
			query = oldSearchQuery.getNewQuery();
		} catch (OldLexerException e) {
			System.err.println("Lexer exception: " + e.getMessage());
			System.exit(1);
		} catch (OldParserException e) {
			System.err.println("Lexer exception: " + e.getMessage());
			System.exit(1);
		} catch (IcatException e) {
			System.err.println("Exception: " + e.getMessage());
			System.exit(1);
		}
		System.out.println(query);
	}

}

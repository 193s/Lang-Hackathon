package lang.lexer;
import lang.token.Token;
import lang.util.Extension;

import static lang.token.TokenKind.*;

public class Lexer implements ILexer {
	private static String[] reserved
            = new String[] {"if","else", "while", "print"};

    @Override
	public Token[] tokenize(String s) {
		String[][] m = Extension.matchAll(s,
				"\\s*("
				+ '(' + Extension.getRegularExpressionOrString(reserved) + ')' + '|'
				+ "(==|[-+*/%=:;,<>\\{\\}\\(\\)])" + "|"
				+ "([0-9]+)" + "|"
				+ "([a-zA-z]+)" + ")");
		
		Token[] ret = new Token[m.length];
		for (int i=0; i<m.length; i++) {
			if		(m[i][2] != null) ret[i] = new Token(m[i][2], Reserved);
			else if	(m[i][3] != null) ret[i] = new Token(m[i][3], Operator);
			else if (m[i][4] != null) ret[i] = new Token(m[i][4], Literal);
			else if (m[i][5] != null) ret[i] = new Token(m[i][5], Identifier);
		}
		return ret;
	}
}

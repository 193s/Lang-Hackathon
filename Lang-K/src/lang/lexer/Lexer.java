package lang.lexer;
import lang.lexer.Token.*;
import lang.util.Extension;

public class Lexer {
	public static String[] reserved;
	
	static {
		reserved = new String[] {"if","else", "while"};
	}
	
	// 字句解析器
	public static Token[] tokenize(String s) {
		String[][] m = Extension.matchAll(s,
				"\\s*("
				+ '(' + Extension.getRegularExpressionOrString(reserved) + ')' + '|'
				+ "(==|[-+*/%=;<>\\{\\}\\(\\)])" + "|"
				+ "([0-9]+)" + "|"
				+ "([a-zA-z]+)"
				+ ")\\s*");
		
		Token[] ret = new Token[m.length];
		for (int i=0; i<m.length; i++) {
			if		(m[i][2] != null) ret[i] = new Token.Reserved(m[i][2]);
			else if	(m[i][3] != null) ret[i] = new Token.Operator(m[i][3]);
			else if (m[i][4] != null) ret[i] = new Token.Num(m[i][4]);
			else if (m[i][5] != null) ret[i] = new Token.Name(m[i][5]);
		}
		return ret;
	}
}

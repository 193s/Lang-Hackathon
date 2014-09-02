package lang.lexer;
import lang.lexer.Token.*;
import lang.util.Extension;

public class Lexer {
	// 字句解析器
	public static Token[] tokenize(String s) {
		String[][] m = Extension.matchAll(s,
				"\\s*((==|[-+*/=;<>\\{\\}\\(\\)])|([0-9]+)|([a-zA-z]+))\\s*");
		
		Token[] ret = new Token[m.length];
		for (int i=0; i<m.length; i++) {
			if		(m[i][2] != null) ret[i] = new Token.Operator(m[i][2]);
			else if	(m[i][3] != null) ret[i] = new Token.Num(Integer.parseInt(m[i][3]));
			else if (m[i][4] != null) ret[i] = new Token.Name(m[i][4]);
		}
		return ret;
	}
}

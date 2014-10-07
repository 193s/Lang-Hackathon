package marg.lexer;
import java.util.ArrayList;

import marg.token.*;

import static marg.token.TokenKind.*;
import static marg.util.CharExtension.*;

public class Lexer implements ILexer {
    @Override
	public Token[] tokenize(String input)
            throws NullPointerException {
		ArrayList<Token> ls = new ArrayList<>();
		int offset = 0;
		while (offset < input.length()) {
			Token t = getToken(input, offset);
			if (t == null) break;
            // ignore Space and Comment.
			TokenKind kind = t.kind;
			if (kind != Space &&
				kind != OneLineComment &&
				kind != MultiLineComment)
				ls.add(t);
			offset += t.string.length();
		}
        if (ls.isEmpty()) throw new NullPointerException();
        return ls.toArray(new Token[ls.size()]);
	}
	
	
	private static Token getToken(String str, int offset) {
		String s = take(str, offset, 1);
		if (s.isEmpty()) return null;
		char c = s.charAt(0);
		
		// Comment
		if (c == '#') {
			String comment = "#";
			String sr = take(str, offset + 1, 2);
			if (sr.equals("--")) {
				// Multi line comment (#-- comment --#)
				comment += "--";
				for (int i=3; ; i++) {
					String string = take(str, offset + i, 3);
					comment += take(string, 0, 1);
					if (string.equals("--#") || string.isEmpty())
                        return new Token(comment + "--#", MultiLineComment);
				}
			}
			else {
				// One line comment (# comment)
				for (int i=1; ; i++) {
					String string = take(str, offset + i, 1);
					comment += string;
					if (string.equals("\n") || string.isEmpty())
                        return new Token(comment, OneLineComment);
				}
			}
		}

        if (isSpace(c))
            return new Token(c, Space);

        if (isSymbol(c))
            return new Token(c, Symbol);

		if (isAlpha(c)) {
			String ident = c + takeWhileIdent(str, offset + 1);
			if (isReserved(ident))
				return new Token(ident, Reserved);   // Reserved
			if (ident.equals("true"))
				return new Token(ident, True);       // true
			if (ident.equals("false"))
				return new Token(ident, False);      // false

            return new Token(ident, Identifier);     // Identifier
		}

		// Integer Literal
		if (isNum(c)) {
			String ident = c + takeWhileIdent(str, offset + 1);
			return new Token(ident, Literal);
		}

		// Operator
		if (isOperatorSign(c)) {
			String ident =
                    c + takeWhileOperatorSign(str, offset + 1);
			return new Token(ident, Operator);
	    }

		// Undefined Token
        return null;
	}
	
	
	
	private static String takeWhileOperatorSign(String str, int offset) {
		String string = "";
		int i = 0;
		while (true) {
			String s = take(str, offset + i, 1);
			if (s.isEmpty()) break;
			char c = s.charAt(0);
			if (isOperatorSign(c)) string += c;
			else break;
			i++;
		}
		return string;
	}
	
	private static String takeWhileIdent(String str, int offset) {
		String string = "";
		int i = 0;
		while (true) {
			String s = take(str, offset + i, 1);
			if (s.isEmpty()) break;
			char c = s.charAt(0);
			if (isAlphaNum(c)) string += c;
			else break;
			i++;
		}
		return string;
	}
	
	private static String take(String str, int offset, int num) {
		String s = "";
		for (int i=0; i<num; i++) {
			if (str.length() <= offset + i) break;
			else s += str.charAt(offset + i);
		}
		return s;
	}
	
	private static boolean isOperatorSign(char c) {
		for (OperatorSign o: OperatorSign.values())
			if (c == o.character) return true;
		return false;
	}
	
	private static boolean isReserved(String str) {
		for (ReservedKind r : ReservedKind.values())
			if (str.equals(r.toString())) return true;
		return false;
	}
    private static boolean isSymbol(char c) {
        for (SymbolKind s : SymbolKind.values()) {
            if (c == s.charactor) return true;
        }
        return false;
    }
}

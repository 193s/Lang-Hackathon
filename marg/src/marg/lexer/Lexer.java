package marg.lexer;
import java.util.ArrayList;
import java.util.List;

import marg.debug.Console;
import marg.token.*;

import static marg.token.TokenKind.*;
import static java.lang.Character.*;

public class Lexer implements ILexer {
    @Override
	public List<Token> tokenize(String input)
            throws NullPointerException {
		List<Token> ls = new ArrayList<>();
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
        ls.add(new Token(null, EOF));
        return ls;
	}
	
	
    private static Token getToken(String str, int offset) {
        String s = take(str, offset, 1);
        if (s.isEmpty()) return null;
		char c = s.charAt(0);

		// Comment
		if (c == '#') {
			String comment = "#";
			String sr = take(str, offset + 1, 2);
			if ("--".equals(sr)) {
				// Multi line comment (#-- comment --#)
				comment += "--";
				for (int i=3; ; i++) {
					String string = take(str, offset + i, 3);
					comment += take(string, 0, 1);
					if ("--#".equals(string) || string.isEmpty())
                        return new Token(comment + "--#", MultiLineComment);
				}
			}
			else {
				// One line comment (# comment)
				for (int i=1; ; i++) {
					String string = take(str, offset + i, 1);
					comment += string;
                    if ("\n".equals(string) || string.isEmpty())
                        return new Token(comment, OneLineComment);
				}
			}
		}


        if (isWhitespace(c))
            return new Token(c, Space);


        if (isSymbol(c))
            return new Token(c, Symbol);


        if (isOperatorSign(c)) {
            String ident =
                    c + takeWhileOperatorSign(str, offset + 1);
            return new Token(ident, Operator);
        }


        // Integer Literal
        if (isDigit(c)) {
            String ident = c + takeWhileIdent(str, offset + 1);
            return new Token(ident, IntLiteral);
        }


        if (isLetter(c)) {
            String ident = c + takeWhileIdent(str, offset + 1);

            if ("o".equals(ident) || "x".equals(ident))
                return new Token(ident, BoolLiteral); // Bool literal
            if (isReserved(ident))
                return new Token(ident, Reserved);    // Reserved

            return new Token(ident, Identifier);      // Identifier
        }


		// Undefined Token
        Console.out.println("Undefined token : " + c);
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
			if (isLetterOrDigit(c)) string += c;
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
			if (r.toString().equals(str)) return true;
		return false;
	}

    private static boolean isSymbol(char c) {
        for (SymbolKind s : SymbolKind.values())
            if (c == s.character) return true;
        return false;
    }
}

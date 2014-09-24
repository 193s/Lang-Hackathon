package lang.lexer;
import java.util.ArrayList;

import lang.debug.Debug;
import lang.token.Token;
import lang.token.TokenKind;
import lang.token.OperatorSign;
import lang.token.ReservedKind;

import static lang.token.TokenKind.*;

public class LegacyLexer implements ILexer {
    @Override
	public Token[] tokenize(String input) {
        Debug.out.println("tokenize(string input) -> Token[]:");
        Debug.out.println(input);
		ArrayList<Token> ls = new ArrayList<>();
		int offset = 0;
		while (offset < input.length()) {
			Token t = getToken(input, offset);
			if (t == null) break;
			// 空白・コメントなら読み飛ばす
			TokenKind kind = t.kind;
			if (kind != Space &&
				kind != OneLineComment &&
				kind != MultiLineComment)
				ls.add(t);
			offset += t.string.length();
		}
        try {
            return (Token[]) ls.toArray();
        }
        catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	
	private static Token getToken(String str, int offset) {
		String s = take(str, offset, 1);
		if (s.isEmpty()) return null;
		char c = s.charAt(0);
		
		// コメント
		if (c == '#') {
			String comment = "#";
			String sr = take(str, offset + 1, 2);
			if (sr.equals("--")) {
				// 複数行コメント (#-- comment --#)
				comment += "--";
				for (int i=3; ; i++) {
					String string = take(str, offset + i, 3);
					comment += take(string, 0, 1);
					if (string.equals("--#") || string.isEmpty())
                        return new Token(comment + "--#", MultiLineComment);
				}
			}
			else {
				// 一行コメント (# comment)
				for (int i=1; ; i++) {
					String string = take(str, offset + i, 1);
					comment += string;
					if (string.equals("\n") || string.isEmpty())
                        return new Token(comment, OneLineComment);
				}
			}
		}
		
		else if (c == ' ' || c == '\t' || c == '\n') return new Token(c, Space);
		else if (c == '[')  return new Token(c, LeftBracket);
		else if (c == ']')  return new Token(c, RightBracket);
		else if (c == '(')  return new Token(c, LeftParentheses);
		else if (c == ')')  return new Token(c, RightParentheses);
		else if (c == '{')  return new Token(c, LeftBrace);
		else if (c == '}')  return new Token(c, RightBrace);
		else if (c == '\'')	return new Token(c, Quotation);
		else if (c == '\"')	return new Token(c, DoubleQuotation);
		
		else if (isAlpha(c)) {
			String ident = c + takeWhileIdent(str, offset + 1);
			if (isReserved(ident))
				return new Token(ident, Reserved);	// 予約語
			else if (ident.equals("true"))
				return new Token(ident, True);		// true
			else if (ident.equals("false"))
				return new Token(ident, False);		// false
			else if (ident.equals("null"))
				return new Token(ident, Null);		// null
			else
				return new Token(ident, Identifier);// 識別子
		}
		// 整数リテラル
		else if (isNum(c)) {
			String ident = c + takeWhileIdent(str, offset + 1);
			return new Token(ident, Literal);
		}
		// 演算子 ($ + 識別子)
		else if (c == '$') {
			String ident;
			ident = c + takeWhileIdent(str, offset + 1);
			return new Token(ident, Operator2);
		}
		// 演算子
		else if (isOperatorSign(c)) {
			String ident;
			ident = c + takeWhileOperatorSign(str, offset + 1);
			return new Token(ident, Operator1);
		}
		// 未定義のトークン
		else {
			return new Token(c, Undefined);
		}
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
	
	private static boolean isAlphaNum(char c) {
		return isAlpha(c) || isNum(c);
	}
	
	private static boolean isNum(char c) { 
		return c >= '0' && c <= '9';
	}
	
	private static boolean isAlpha(char c) {
		return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_';
	}
}

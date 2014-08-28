package lang.lexer;
import java.util.ArrayList;
import java.util.List;

import lang.Token;
import lang.Token.OperatorSign;
import lang.Token.ReservedKind;
import lang.Token.TokenKind;
import static lang.Token.TokenKind.*;

public class Lexer {
	public static Token[] analyze(String sourceCode) {
		List<Token> tokens = new ArrayList<Token>();
		int offset = 0;
		while (offset < sourceCode.length()) {
			Token t = getToken(sourceCode, offset);
			if (t == null) break;
			// 空白・コメントなら読み飛ばす
			TokenKind kind = t.getKind();
			if (kind != Space &&
				kind != OneLineComment &&
				kind != MultiLineComment)
				tokens.add(t);
			offset += t.getString().length();
		}
		return (Token[]) tokens.toArray();
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
					if (string.equals("--#") || string.isEmpty()) return new Token(MultiLineComment, comment + "-#");
				}
			}
			else {
				// 一行コメント (# comment)
				for (int i=1; ; i++) {
					String string = take(str, offset + i, 1);
					comment += string;
					if (string.equals("\n") || string.isEmpty()) return new Token(OneLineComment, comment);
				}
			}
		}
		
		else if (c == ' ' || c == '\t' || c == '\n') return new Token(Space, c);
		else if (c == '[')	return new Token(LeftBracket, c);
		else if (c == ']')	return new Token(RightBracket, c);
		else if (c == '(')	return new Token(LeftParentheses, c);
		else if (c == ')')	return new Token(RightParentheses, c);
		else if (c == '{')	return new Token(LeftBrace, c);
		else if (c == '}')	return new Token(RightBrace, c);
		else if (c == '\'')	return new Token(Quotation, c);
		else if (c == '\"')	return new Token(DoubleQuotation, c);
		
		else if (isAlpha(c)) {
			String ident = c + takeWhileIdent(str, offset + 1);
			if (isReserved(ident))
				return new Token(Reserved, ident);	// 予約語
			else if (ident.equals("true"))
				return new Token(True, ident);		// true
			else if (ident.equals("false"))
				return new Token(False, ident);		// false
			else if (ident.equals("null"))
				return new Token(Null, ident);		// null
			else {
				return new Token(Ident, ident);		// 識別子
			}
		}
		// 整数リテラル
		else if (isNum(c)) {
			String ident = c + takeWhileIdent(str, offset + 1);
			return new Token(Num, ident);
		}
		// 演算子 ($ + 識別子)
		else if (c == '$') {
			String ident;
			ident = c + takeWhileIdent(str, offset + 1);
			return new Token(Operator2, ident);
		}
		// 演算子
		else if (isOperatorSign(c)) {
			String ident;
			ident = c + takeWhileOperatorSign(str, offset + 1);
			return new Token(Operator1, ident);
		}
		// 未定義のトークン
		else {
			return new Token(Undefined, c);
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
			if (c == o.getChar()) return true;
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

package lang.lexer;

import sun.security.util.Debug;


public class TokenSet {
	private final Token[] list;
	private final int length;
	private int offset = 0;
	
	public TokenSet(Token[] tokens) {
		this.list = tokens;
		length = tokens.length;
	}
	
	public void unget() {
		if (offset > 0) offset--;
	}
	public void unget(int k) {
		if (offset >= k) offset -= k;
	}
	public Token next() {
		return list[offset++];
	}
	private Token getNext() {
		return list[offset+1];
	}
	public Token get() {
		return list[offset];
	}
	
	public boolean isEOF() {
		return offset >= length;
	}
	
//	public boolean isMatch(String s) {
//		return s.equals(get().string);
//	}
	
	public boolean isMatch(String... args) {
		String t = get().string;
		for (String str: args) {
			if (str.equals(t)) return true;
		}
		return false;
	}
	public boolean read(String... args) {
		for (String t : args) {
	 		Token next = next();
	 		if (!t.equals(next.string)) return false;
		}
		return true;
	}
	
	public boolean isName() {
		return isEOF()? false : get().kind == TokenKind.Identifier;
	}
	
	@Deprecated
	public Token readName() {
		return isName()? next() : null;
	}
	
	
	public boolean isNumber() {
		return isEOF()? false : get().kind == TokenKind.Literal;
	}
	
	@Deprecated
	public Token readNumber() {
		return isNumber()? next() : null;
	}
	
	
	
	public boolean isOperator() {
		return isEOF()? false : get().kind == TokenKind.Operator;
	}
	
	public boolean isOperator(String str) {
		return isOperator() && isMatch(str);
	}
	
	@Deprecated
	public Token readOperator() {
		return isOperator()? next() : null;
	}
	
	
	
	public boolean isReserved() {
		return isEOF()? false : get().kind == TokenKind.Reserved;
	}
	
	public boolean isReserved(String str) {
		return isReserved() && str.equals(get().string);
	}
	
	@Deprecated
	public Token readReserved() {
		return isReserved()? next() : null;
	}
}

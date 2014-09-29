package marg.token;


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
	public Token get() {
		return list[offset];
	}

    public boolean is(String s) {
        return s.equals(get().string);
    }

	public boolean isEOF() {
		return offset >= length;
	}
	
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

	public boolean isNumber() {
		return isEOF()? false : get().kind == TokenKind.Literal;
	}


    public boolean isSymbol() {
        return isEOF()? false : get().kind == TokenKind.Symbol;
    }
    public boolean isSymbol(SymbolKind kind) {
        return isSymbol() && is(String.valueOf(kind.charactor));
    }

	public boolean isOperator() {
		return isEOF()? false : get().kind == TokenKind.Operator;
	}
	
	public boolean isOperator(String str) {
		return isOperator() && isMatch(str);
	}

	public boolean isReserved() {
		return isEOF()? false : get().kind == TokenKind.Reserved;
	}
	
	public boolean isReserved(String str) {
		return isReserved() && str.equals(get().string);
	}
}

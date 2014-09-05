package lang.lexer;


public class TokenSet {
	private final Token[] list;
	private final int length;
	private int offset = 0;
	
	public TokenSet(Token[] tokens) {
		this.list = tokens;
		length = tokens.length;
	}
	
	public void back() {
		if (offset > 0) offset--;
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
	
	public boolean isName() {
		return isEOF()? false : get() instanceof Token.Name;
	}
	
	public Token.Name readName() {
		return isName()? (Token.Name) next() : null;
	}
	
	
	public boolean isNumber() {
		return isEOF()? false : get() instanceof Token.Num;
	}
	
	public Token.Num readNumber() {
		return isNumber()? (Token.Num) next() : null;
	}
	
	
	
	public boolean isOperator() {
		return isEOF()? false : get() instanceof Token.Operator;
	}
	
	public boolean isOperator(String str) {
		return isOperator() && str.equals((String) get().getValue());
	}
	
	public Token.Operator readOperator() {
		return isOperator()? (Token.Operator) next() : null;
	}
	
	
	
	public boolean isReserved() {
		return isEOF()? false : get() instanceof Token.Reserved;
	}
	
	public boolean isReserved(String str) {
		return isReserved() && str.equals((String) get().getValue());
	}
	
	public Token.Reserved readReserved() {
		return isReserved()? (Token.Reserved) next() : null;
	}
}

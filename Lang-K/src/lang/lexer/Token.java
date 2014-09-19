package lang.lexer;

public class Token {
	public Token(String string, TokenKind kind) {
		this.string = string;
		this.kind = kind;
	}
	
	public String string;
	public TokenKind kind;
	
	@Override
	public String toString() {
		return string + "\t: " + kind;
	}
}

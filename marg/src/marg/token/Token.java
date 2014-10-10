package marg.token;

public class Token {
	public Token(String string, TokenKind kind) {
		this.string = string;
		this.kind = kind;
	}
    public Token(char c, TokenKind kind) {
        string = String.valueOf(c);
        this.kind = kind;
    }
	
	public String string;
	public TokenKind kind;
	
	@Override
	public String toString() {
		return string + "\t: " + kind;
	}

    public boolean isEOF() {
        return kind == TokenKind.EOF;
    }
}

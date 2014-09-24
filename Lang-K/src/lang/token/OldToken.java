package lang.token;

@Deprecated
public class OldToken {
	private TokenKind kind;
	private String string;
	
	public OldToken(TokenKind tokenKind, String string) {
		this.kind = tokenKind;
		this.string = string;
	}
	
	public OldToken(TokenKind tokenKind, char c) {
		this.kind = tokenKind;
		string = String.valueOf(c);
	}
	
	
	public TokenKind getKind() {
		return kind;
	}
	
	public String getString() {
		return string;
	}
}

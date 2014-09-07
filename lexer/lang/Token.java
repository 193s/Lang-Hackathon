package lang;

public class Token {
	private TokenKind kind;
	private String string;
	
	public Token(TokenKind tokenKind, String string) {
		this.kind = tokenKind;
		this.string = string;
	}
	
	public Token(TokenKind tokenKind, char c) {
		this.kind = tokenKind;
		string = String.valueOf(c);
	}
	
	
	public TokenKind getKind() {
		return kind;
	}
	
	public String getString() {
		return string;
	}
	
	
	public static enum TokenKind {
		Space,				// 空白
		NumberSign,			// #, 一行コメント
		LeftBracket,		// [
		RightBracket,		// ]
		LeftParentheses,	// (
		RightParentheses,	// )
		LeftBrace,			// {
		RightBrace,			// }
		Quotation,			// '
		DoubleQuotation,	// "
		BackSlash,			// \
		Comma,				// ,
		Num,				// 整数/小数リテラル
		True,				// true
		False,				// false
		Null,				// null
		Operator1,			// 演算子
		Operator2,			// 演算子($+識別子)
		Ident,				// 識別子
		Reserved,			// 予約語
		OneLineComment,		// 一行コメント
		MultiLineComment,	// 複数行コメント
		Undefined;			// 未定義のトークン
	}
	
	
	public static enum ReservedKind {
		Return,		// return
		If,			// if
		Else,		// else
		Switch,		// switch
		Case,		// case
		Break,		// break
		Continue,	// continue
		Default,	// default
		For,		// for
		While,		// while
		Int,		// int
		Float,		// float
		Double,		// double
		Void,		// void
		Bool,		// bool
		Char,		// char
		String,		// string
		Class;		// class
		
		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}
	
	
	public static enum OperatorSign {
		ExclamationMark	('!'),
		QuestionMark	('?'),
		Percent			('%'),
		Ampersand		('&'),
		Asterisk		('*'),
		Plus			('+'),
		Minus			('-'),
		Comma			(','),
		Dot				('.'),
		Slash			('/'),
		Colon			(':'),
		LessThanSign	('<'),
		MoreThanSign	('>'),
		Equal			('='),
		AtSign			('@'),
		Caret			('^'),
		BackQuote		('`'),
		VerticalBar		('|'),
		Tilde			('~');
		
		private char character;
		private OperatorSign(char c) {
			character = c;
		}
		
		public char getChar() {
			return character;
		}
	}
}

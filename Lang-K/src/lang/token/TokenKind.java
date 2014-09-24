package lang.token;

public enum TokenKind {
    Space,              // space (space, tab, ...)
    OneLineComment,     // one line comment
    MultiLineComment,   // multiline comment
    Reserved,			// Reserved Token
    True,				// true
    False,				// false
    Null,				// null
    Identifier,			// Identifier
    Literal,			// Integer Literal
    Operator1,			// Normally Operator Token
    Operator2,			// Operator Token ($ + Identifier)
    NumberSign,			// #
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

    Undefined;			// Undefined Token
}


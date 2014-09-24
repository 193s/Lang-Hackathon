package lang.token;

public enum TokenKind {
    Space,              // space (space, tab, ...)
    OneLineComment,     // one line comment
    MultiLineComment,   // multiline comment
    Reserved,			// Reserved Token
    True,				// true
    False,				// false
    Identifier,			// Identifier
    Literal,			// Integer Literal
    Operator,			// Operator Token
    Symbol,             // Symbol Token
}


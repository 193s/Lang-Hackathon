package marg.token;

public enum SymbolKind {
    LeftBracket('['),
    RightBracket(']'),
    LeftParenthesis('('),
    RightParenthesis(')'),
    LeftBrace('{'),
    RightBrace('}'),
    SingleQuotation('\''),
    DoubleQuotation('\"'),
    Comma(','),
    Colon(':'),
    Semicolon(';'),
    ;

    public char character;
    private SymbolKind(char c) {
        character = c;
    }
}

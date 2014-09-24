package lang.token;

public enum SymbolKind {
    LeftBracket('['),
    RightBracket(']'),
    LeftParentheses('('),
    RightParentheses(')'),
    LeftBrace('{'),
    RightBrace('}'),
    SingleQuotation('\''),
    DoubleQuotation('\"'),
    Comma(','),
    Colon(':'),
    Semicolon(';'),
    ;

    public char charactor;
    private SymbolKind(char c) {
        charactor = c;
    }
}

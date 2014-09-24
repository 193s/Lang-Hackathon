package lang.lexer;

import lang.token.Token;

public interface ILexer {
    public Token[] tokenize(String input);
}

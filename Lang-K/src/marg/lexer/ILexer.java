package marg.lexer;

import marg.token.Token;

public interface ILexer {
    public Token[] tokenize(String input);
}

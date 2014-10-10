package marg.lexer;

import marg.token.Token;

import java.util.List;

public interface ILexer {
    public List<Token> tokenize(String input);
}

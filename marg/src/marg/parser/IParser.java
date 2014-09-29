package marg.parser;

import marg.token.TokenSet;

public interface IParser {
    public AST parse(TokenSet ls);
}

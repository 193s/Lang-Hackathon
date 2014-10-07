package marg.parser;

import marg.token.TokenSet;

public interface IParser {
    public ASTree parse(TokenSet ls);
}

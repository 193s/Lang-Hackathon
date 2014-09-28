package lang.parser;

import lang.token.TokenSet;

public interface IParser {
    public AST parse(TokenSet ls);
}

package lang.parser;

import lang.parser.AST;
import lang.token.TokenSet;

public interface IParser {
    public AST parse(TokenSet ls);
}

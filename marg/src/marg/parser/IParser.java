package marg.parser;

import marg.exception.ParseException;
import marg.ast.ASTree;
import marg.token.TokenSet;

public interface IParser {
    public ASTree parse(TokenSet ls) throws ParseException;
}

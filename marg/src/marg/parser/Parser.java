package marg.parser;

import marg.exception.ParseException;
import marg.ast.*;
import marg.ast.other.Program;
import marg.token.TokenSet;


public class Parser implements IParser {
    @Override
    public ASTree parse(TokenSet ls) throws ParseException {
        ASTree ast;
        ast = new Program(ls);
        return ast;
	}
}







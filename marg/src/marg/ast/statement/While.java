package marg.ast.statement;

import marg.exception.ParseException;
import marg.debug.Debug;
import marg.ast.ASTree;
import marg.lang.data.IType;
import marg.parser.Environment;
import marg.ast.other.Block;
import marg.token.TokenSet;

import java.io.EOFException;


public class While extends ASTree {
    private Condition condition;
    private ASTree program;
    public While(TokenSet ls) throws ParseException, EOFException {
        Debug.log("while");
        if (!ls.read("while", "("))
            throw new ParseException("Syntax Error: invalid syntax", ls);
        condition = new Condition(ls);
        if (!ls.read(")"))
            throw new ParseException("Syntax Error: invalid syntax", ls);
        program = new Block(ls);
    }

    @Override
    public IType eval(int k, Environment e) {
        Debug.log(k, "While");
        while (condition.eval(k + 1, e).get())
            program.eval(k + 1, e);
        return null;
    }
}

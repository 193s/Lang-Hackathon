package marg.ast.statement;

import marg.exception.ParseException;
import marg.debug.Debug;
import marg.ast.ASTree;
import marg.lang.type.IType;
import marg.parser.Environment;
import marg.ast.other.Block;
import marg.token.TokenSet;

import java.io.EOFException;


public class If extends ASTree {
    private Condition condition;
    private ASTree program;

    public If(TokenSet ls) throws ParseException, EOFException {
        Debug.log("if");
        if (!ls.read("if", "(")) throw new ParseException();
        condition = new Condition(ls);
        if (!ls.read(")")) throw new ParseException();
        program = new Block(ls);

    }

    @Override
    public IType eval(int k, Environment e) {
        Debug.log(k, "If");
        boolean cond = condition.eval(k + 1, e).get();
        if (cond) program.eval(k + 1, e);
        return null;
    }
}

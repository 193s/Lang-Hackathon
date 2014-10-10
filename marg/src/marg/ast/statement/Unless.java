package marg.ast.statement;

import marg.ast.ASTree;
import marg.ast.other.Block;
import marg.debug.Debug;
import marg.exception.ParseException;
import marg.lang.type.IType;
import marg.parser.Environment;
import marg.token.TokenSet;

import java.io.EOFException;

public class Unless extends ASTree {
    private Condition condition;
    private ASTree program;

    public Unless(TokenSet ls) throws ParseException, EOFException {
        Debug.log("unless");
        if (!ls.read("unless", "(")) throw new ParseException();
        condition = new Condition(ls);
        if (!ls.read(")")) throw new ParseException();
        program = new Block(ls);

    }

        @Override
        public IType eval(int k, Environment e) {
        Debug.log(k, "Unless");
        boolean cond = condition.eval(k + 1, e).get();
        if (!cond) program.eval(k + 1, e);
        return null;
    }
}

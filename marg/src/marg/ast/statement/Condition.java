package marg.ast.statement;

import marg.ast.ASTree;
import marg.ast.other.Expr;
import marg.exception.ParseException;
import marg.lang.data.IType;
import marg.lang.data.MBool;
import marg.parser.Environment;
import marg.token.TokenSet;

import java.io.EOFException;

public class Condition {
    private ASTree child;
    public Condition(TokenSet ls) throws ParseException, EOFException {
        child = new Expr(ls);
    }

    public MBool eval(int k, Environment e) {
        IType condition = child.eval(k + 1, e);
        if (!(condition instanceof MBool)) return null;
        return (MBool) condition;
    }
}

package marg.ast.statement;

import marg.ast.ASTLeaf;
import marg.ast.ASTree;
import marg.ast.other.Expr;
import marg.exception.ParseException;
import marg.lang.type.IType;
import marg.lang.type.MBool;
import marg.parser.Environment;
import marg.token.TokenSet;

import java.io.EOFException;

public class Condition {
    ASTree child;
    public Condition(TokenSet ls) throws ParseException, EOFException {
        child = new Expr(ls);
    }

    public MBool eval(int k, Environment e) {
        IType condition = child.eval(k + 1, e);
        if (!(condition instanceof MBool)) return null;
        return (MBool) condition;
    }
}

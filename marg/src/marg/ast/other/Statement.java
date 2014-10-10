package marg.ast.other;

import marg.ast.statement.*;
import marg.debug.Console;
import marg.exception.ParseException;
import marg.debug.Debug;
import marg.lang.type.IType;
import marg.parser.Environment;
import marg.ast.ASTree;
import marg.token.TokenSet;

import java.io.EOFException;

public class Statement extends ASTree {
    ASTree child;
    public Statement(TokenSet ls) throws ParseException {
        try {
            child =
              ls.is("while")  ? new While(ls)
            : ls.is("if")     ? new If(ls)
            : ls.is("unless") ? new Unless(ls)
            : ls.is("echo")   ? new Echo(ls)
            : ls.isName()     ? new Assign(ls)
            :                   new Expr(ls)
            ;
        }
        catch (EOFException e) {
            return;
        }
        catch (Exception e) {
            Console.out.println("Unexpected error.");
            e.printStackTrace(Console.out);
        }
    }

    @Override
    public IType eval(int k, Environment e) {
        Debug.log(k, "Statement");
        return child.eval(k + 1, e);
    }
}

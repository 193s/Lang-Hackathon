package marg.ast.statement;

import marg.exception.ParseException;
import marg.debug.Console;
import marg.debug.Debug;
import marg.ast.ASTree;
import marg.lang.data.IType;
import marg.parser.Environment;
import marg.ast.ASTList;
import marg.ast.other.Expr;
import marg.token.TokenSet;

import java.io.EOFException;

public class Echo extends ASTList {
    public Echo(TokenSet ls) throws ParseException, EOFException {
        Debug.log("echo");
        ls.read("echo");
        ASTree ast = new Expr(ls);
        children.add(ast);
    }

    @Override
    public IType eval(int k, Environment e) {
        Debug.log(k, "Echo");
        Console.out.println(children.get(0).eval(k + 1, e).get());
        return null;
    }
}

package marg.ast.statement;

import marg.exception.ParseException;
import marg.debug.Debug;
import marg.ast.ASTree;
import marg.lang.data.IType;
import marg.parser.Environment;
import marg.ast.ASTList;
import marg.ast.leaf.Operator;
import marg.ast.other.Statement;
import marg.ast.leaf.Variable;
import marg.token.TokenSet;

import java.io.EOFException;

public class Assign extends ASTList {
    public Assign(TokenSet ls) throws ParseException, EOFException {
        Debug.log("assign");
        ls.checkEOF();
        ASTree left = new Variable(ls.next().string);

        ls.checkEOF();
        Operator operator = new Operator(ls);

//        if (!operator.assignment) throw new ParseException();

        ASTree right = new Statement(ls);

        children.add(left);
        children.add(operator);
        children.add(right);

    }

    @Override
    public IType eval(int k, Environment e) {
        Debug.log(k, "Assign");
        IType ret = children.get(2).eval(k + 1, e);
        String identifier = ((Variable) children.get(0)).string;
//        e.put(identifier, ret);
        e.put(identifier, ret);
        return ret;
    }
}

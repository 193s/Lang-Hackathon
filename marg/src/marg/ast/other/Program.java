package marg.ast.other;

import marg.exception.ParseException;
import marg.debug.Debug;
import marg.lang.type.IType;
import marg.parser.Environment;
import marg.ast.ASTList;
import marg.ast.ASTree;
import marg.token.TokenSet;

import java.io.EOFException;


public class Program extends ASTList {
    public Program(TokenSet ls) throws ParseException {
        Debug.log("program");
        ASTree s = new Statement(ls);
        children.add(s);

        try {
            while (!ls.isEOF() && ls.is(",")) {
                ls.read(",");
                ASTree right = new Statement(ls);
                children.add(right);
            }
        }
        catch (EOFException e) {}
    }

    @Override
    public IType eval(int k, Environment e) {
        Debug.log(k, "Program");

        for (int i=0; i<children.size(); i++) {
            get(i).eval(k + 1, e);
        }
        return null;
    }
}

package marg.ast.other;

import marg.ast.leaf.BoolLiteral;
import marg.ast.leaf.IntLiteral;
import marg.exception.ParseException;
import marg.debug.Debug;
import marg.lang.data.IType;
import marg.parser.Environment;
import marg.ast.ASTList;
import marg.ast.ASTree;
import marg.ast.leaf.Variable;
import marg.token.TokenSet;

import java.io.EOFException;


public class Value extends ASTList {
    ASTree child;
    Value(TokenSet ls) throws ParseException, EOFException {
        // ( Expression )
        if (ls.is("(")) {
            Debug.log("( expression )");
            ls.read("(");
            ASTree s = new Expr(ls);
            if (!ls.read(")"))
                throw new ParseException("Syntax Error: ')' not found", ls);
            child = s;
        }

        // Literal: Int
        else if (ls.isNumber()) {
            Debug.log("int literal");
            child = new IntLiteral(ls);
        }

        // Literal: Bool
        else if (ls.isBool()) {
            Debug.log("bool literal");
            child = new BoolLiteral(ls);
        }

        // Variable
        else if (ls.isName()) {
            Debug.log("variable");
            String id = ls.next().string;
            child = new Variable(id);
        }
        else throw new ParseException("Internal Error: invalid <Value>", ls);
    }

    @Override
    public IType eval(int k, Environment e) {
        return child.eval(k + 1, e);
    }
}

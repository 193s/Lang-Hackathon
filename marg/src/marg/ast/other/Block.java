package marg.ast.other;

import marg.exception.ParseException;
import marg.debug.Debug;
import marg.lang.type.IType;
import marg.parser.Environment;
import marg.ast.ASTList;
import marg.ast.ASTree;
import marg.token.TokenSet;

import java.io.EOFException;


public class Block extends ASTList {
    public Block(TokenSet ls) throws ParseException, EOFException {
        Debug.log("block");
        if (!ls.read(":")) return;
        ASTree program = new Program(ls);
        if (!ls.read(";")) return;
        children.add(program);
    }

    @Override
    public IType eval(int k, Environment e) {
        Debug.log(k, "block");
        return children.get(0).eval(k + 1, e);
    }
}

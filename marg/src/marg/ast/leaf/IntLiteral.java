package marg.ast.leaf;

import marg.debug.Debug;
import marg.lang.type.IType;
import marg.lang.type.MInt;
import marg.parser.Environment;
import marg.ast.ASTLeaf;
import marg.token.Token;
import marg.token.TokenSet;

import java.io.EOFException;

public final class IntLiteral extends ASTLeaf {
    public MInt value;

    public IntLiteral(TokenSet ls) throws EOFException {
        Token t = ls.next();
//        if(ls.isEOF()) return;
        string = t.string;
        value = new MInt(Integer.parseInt(string));
    }

    @Override
    public IType eval(int k, Environment e) {
        Debug.log(k, "Literal: " + value);
        return value;
    }
}

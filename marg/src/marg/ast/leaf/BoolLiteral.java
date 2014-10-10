package marg.ast.leaf;

import marg.ast.ASTLeaf;
import marg.debug.Debug;
import marg.lang.type.IType;
import marg.lang.type.MBool;
import marg.parser.Environment;
import marg.token.Token;
import marg.token.TokenSet;

import java.io.EOFException;

public class BoolLiteral extends ASTLeaf {
    public MBool value;

    public BoolLiteral(TokenSet ls) throws EOFException {
        Token t = ls.next();
        string = t.string;
        value = new MBool("o".equals(string));
    }

    @Override
    public IType eval(int k, Environment e) {
        Debug.log(k, "BoolLiteral: " + value);
        return value;
    }
}

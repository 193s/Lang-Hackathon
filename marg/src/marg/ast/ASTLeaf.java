package marg.ast;


import marg.lang.type.IType;
import marg.parser.Environment;

public abstract class ASTLeaf extends ASTree {
    public String string;

    public IType eval(int k, Environment e) {
        return null;
    }
}

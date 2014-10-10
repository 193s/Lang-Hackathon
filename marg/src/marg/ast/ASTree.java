package marg.ast;

import marg.lang.type.IType;
import marg.parser.Environment;

public abstract class ASTree {
    public abstract IType eval(int k, Environment e);
}

package marg.ast.leaf;


import marg.lang.data.IType;
import marg.parser.Environment;
import marg.ast.ASTLeaf;

public final class Variable extends ASTLeaf {
    public Variable(String name) {
        string = name;
    }

    @Override
    public IType eval(int k, Environment e) {
        return e.get(string);
    }
}

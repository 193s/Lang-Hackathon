package marg.ast;

import java.util.ArrayList;
import java.util.List;

public abstract class ASTList extends ASTree {
    protected List<ASTree> children = new ArrayList<>();

    protected ASTree get(int i) {
        return children.get(i);
    }
}

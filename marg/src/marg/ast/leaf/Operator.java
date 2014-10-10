package marg.ast.leaf;

import marg.ast.ASTLeaf;
import marg.lang.operator.BinaryOperators;
import marg.token.Token;
import marg.token.TokenSet;

import java.io.EOFException;
import java.util.EnumSet;
import java.util.function.BiFunction;

public final class Operator extends ASTLeaf {
    public int level;
    public BiFunction eval;
    public boolean assignment;

    public Operator(TokenSet ls) throws EOFException {
        Token t = ls.next();
        string = t.string;

        EnumSet<BinaryOperators> enumSet =
                EnumSet.allOf(BinaryOperators.class);

        enumSet.forEach(b -> {
            if (string.equals(b.string)) {
                level = b.level;
                eval = b.eval;
                return;
            }
        });

        if ("=".equals(string)) assignment = true;
    }
}

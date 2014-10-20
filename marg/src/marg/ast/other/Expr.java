package marg.ast.other;

import marg.exception.ParseException;
import marg.debug.Debug;
import marg.lang.data.IType;
import marg.lang.operator.BiOperator;
import marg.parser.Environment;
import marg.ast.ASTList;
import marg.ast.ASTree;
import marg.ast.leaf.Operator;
import marg.token.TokenSet;

import java.io.EOFException;
import java.util.ArrayList;

public class Expr extends ASTList {
    private ArrayList<Operator> operators = new ArrayList<>();

    public Expr(TokenSet ls) throws ParseException, EOFException {
        Value n = new Value(ls);
        children.add(n);

        while (ls.isOperator()) {
            Operator op = new Operator(ls);
            Value n2 = new Value(ls);

            operators.add(op);
            children.add(n2);
        }
    }

    @Override
    public IType eval(int k, Environment e) {
        Debug.log(k, "Expr");
        ArrayList<IType> vals = new ArrayList<>();

        int count = 0;
        for (ASTree v: children) {
            vals.add(v.eval(k + 1, e));
            if (count >= operators.size()) break;
            Debug.log(operators.get(count).string);
            count++;
        }

        ArrayList<Operator> ops_cpy = new ArrayList<>(operators),
                ops_cpy2 = new ArrayList<>();
        ArrayList vals_ = new ArrayList();

        for (int il=0; il< BiOperator.maxLevel; il++) {
            ops_cpy2.clear();
            vals_.clear();

            vals_.add(vals.get(0));
            for (int i=0; i<ops_cpy.size(); i++) {
                if (ops_cpy.get(i).level == il) {
                    vals_.add(ops_cpy.get(i).eval.apply(
                        vals_.remove(vals_.size()-1), vals.get(i+1)));
                }
                else {
                    vals_.add(vals.get(i + 1));
                    ops_cpy2.add(ops_cpy.get(i));
                }
            }
            ops_cpy = new ArrayList<>(ops_cpy2);
            vals = new ArrayList(vals_);
        }
        return vals.get(0);
    }
}

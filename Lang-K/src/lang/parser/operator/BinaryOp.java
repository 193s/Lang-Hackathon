package lang.parser.operator;

import java.util.function.BinaryOperator;

public class BinaryOp<T> implements BinaryOperator<T> {
    public int level;
    private BinaryOperator<T> eval;
    public BinaryOp(int level, BinaryOperator<T> eval) {
        this.level = level;
        this.eval = eval;
    }

    @Override
    public T apply(T t, T u) {
        return eval.apply(t, u);
    }
}

package lang.parser.operator;
import java.util.function.BinaryOperator;

public enum IntegerBinaryOperator implements BinaryOperatorIF<Integer> {
    Mod		(2, '%', (left, right) -> left % right),
    Plus	(1, '+', (left, right) -> left + right),
    Minus	(1, '-', (left ,right) -> left - right),
    Multiple(0, '*', (left, right) -> left * right),
    Div		(0, '/', (left, right) -> left / right),
    ;

    private int level;	// 優先順位
    private BinaryOperator<Integer> eval;
    private String sign;

    private IntegerBinaryOperator(int level, String sign, BinaryOperator<Integer> function) {
        this.level = level;
        this.sign = sign;
        this.eval = function;
    }
    private IntegerBinaryOperator(int level, char sign, BinaryOperator<Integer> function) {
        this.level = level;
        this.sign = String.valueOf(sign);
        this.eval = function;
    }

    @Override public Integer eval(Integer a, Integer b) {
        return eval.apply(a, b);
    }
    @Override public int getLevel() {
        return level;
    }
    @Override public String getSign() {
        return sign;
    }
}

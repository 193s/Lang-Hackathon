package lang.parser.operator;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public enum BinaryOperators {
    Mod      (2, '%', (left, right) -> left % right),
    Plus     (1, '+', (left, right) -> left + right),
    Minus    (1, '-', (left, right) -> left - right),
    Multiple (0, '*', (left, right) -> left * right),
    Div      (0, '/', (left, right) -> left / right),
    Equal    (3, "==", (left, right) -> left == right ? 1 : 0),
    MoreThan (3, '>', (left, right) -> left > right ? 1 : 0),
    LessThan (3, '<', (left, right) -> left < right ? 1 : 0),;

    public static int maxLevel = 5;
    public int level;
    public BiFunction eval;
    public String string;

    private BinaryOperators(int level, String sign, BinaryOperator<Integer> function) {
        this.level = level;
        this.string = sign;
        this.eval = function;
    }

    private BinaryOperators(int level, char sign, BinaryOperator<Integer> function) {
        this.level = level;
        this.string = String.valueOf(sign);
        this.eval = function;
    }
}

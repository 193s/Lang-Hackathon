package lang.parser.operator;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public enum BinaryOperators {
    Multiple (0, "*",  (a, b) -> a * b),
    Div      (0, "/",  (a, b) -> a / b),
    Plus     (1, "+",  (a, b) -> a + b),
    Minus    (1, "-",  (a, b) -> a - b),
    Mod      (2, "%",  (a, b) -> a % b),

    NotEqual (3, "!=", (a, b) -> a != b? 1 : 0),
    Equal    (3, "==", (a, b) -> a == b? 1 : 0),
    MoreThan (3, ">",  (a, b) -> a > b ? 1 : 0),
    LessThan (3, "<",  (a, b) -> a < b ? 1 : 0),
    ;


    public static int maxLevel = 5;
    public int level;
    public BiFunction eval;
    public String string;


    private void init(int level, String string, BiFunction func) {
        this.level = level;
        this.string = string;
        eval = func;
    }

    private BinaryOperators(int level, String sign, BinaryOperator<Integer> func) {
        init(level, sign, (BiFunction)func);
    }
}

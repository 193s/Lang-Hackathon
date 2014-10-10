package marg.lang.operator;
import marg.lang.type.MBool;
import marg.lang.type.MInt;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public enum BinaryOperators {
    Multiple (0, "*", (MInt a, MInt b) -> {
        return new MInt(a.get() * b.get());
    }),
    Div (0, "/", (MInt a, MInt b) -> {
        return new MInt(a.get() / b.get());
    }),
    Plus (1, "+", (MInt a, MInt b) -> {
        return new MInt(a.get() + b.get());
    }),
    Minus (1, "-", (MInt a, MInt b) -> {
        return new MInt(a.get() - b.get());
    }),
    Mod (2, "%", (MInt a, MInt b) -> {
        return new MInt(a.get() % b.get());
    }),
    Exp (2, "!",  (MInt a, MInt b) -> {
        int ret = 1;
        for (int i = 2; i <= a.get(); i++) {
            ret *= i;
        }
        return new MInt(ret);
    }),

    Equal    (3, "==", (MInt a, MInt b) -> new MBool(a.get() == b.get())),
    NotEqual (3, "!=", (MInt a, MInt b) -> new MBool(a.get() != b.get())),
    MoreThan (3, ">",  (MInt a, MInt b) -> new MBool(a.get() > b.get())),
    LessThan (3, "<",  (MInt a, MInt b) -> new MBool(a.get() < b.get())),
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

    private BinaryOperators(int level, String sign, BinaryOperator<MInt> func) {
        init(level, sign, (BiFunction)func);
    }
    private BinaryOperators(int level, String sign, BiFunction<MInt, MInt, MBool> func) {
        init(level, sign, func);
    }
}

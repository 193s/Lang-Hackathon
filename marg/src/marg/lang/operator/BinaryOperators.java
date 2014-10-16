package marg.lang.operator;
import marg.lang.data.MBool;
import marg.lang.data.SInt;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public enum BinaryOperators {
    Multiple (0, "*", (SInt a, SInt b) -> {
        return new SInt(a.get() * b.get());
    }),
    Div (0, "/", (SInt a, SInt b) -> {
        return new SInt(a.get() / b.get());
    }),
    Plus (1, "+", (SInt a, SInt b) -> {
        return new SInt(a.get() + b.get());
    }),
    Minus (1, "-", (SInt a, SInt b) -> {
        return new SInt(a.get() - b.get());
    }),
    Mod (2, "%", (SInt a, SInt b) -> {
        return new SInt(a.get() % b.get());
    }),
    Exp (2, "!",  (SInt a, SInt b) -> {
        int ret = 1;
        for (int i = 2; i <= a.get(); i++) {
            ret *= i;
        }
        return new SInt(ret);
    }),

    Equal    (3, "==", (SInt a, SInt b) -> new MBool(a.get() == b.get())),
    NotEqual (3, "!=", (SInt a, SInt b) -> new MBool(a.get() != b.get())),
    MoreThan (3, ">",  (SInt a, SInt b) -> new MBool(a.get() > b.get())),
    LessThan (3, "<",  (SInt a, SInt b) -> new MBool(a.get() < b.get())),
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

    private BinaryOperators(int level, String sign, BinaryOperator<SInt> func) {
        init(level, sign, (BiFunction)func);
    }
    private BinaryOperators(int level, String sign, BiFunction<SInt, SInt, MBool> func) {
        init(level, sign, func);
    }
}

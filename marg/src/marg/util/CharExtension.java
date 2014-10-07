package marg.util;

public class CharExtension {
    public static boolean isSpace(char c) {
        return c == ' ' || c == '\n' || c == '\t';
    }
    public static boolean isAlphaNum(char c) {
        return isAlpha(c) || isNum(c);
    }

    public static boolean isNum(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isAlpha(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_';
    }
}

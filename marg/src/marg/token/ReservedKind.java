package marg.token;

public enum ReservedKind {
    Return("return"),
    If("if"),
    Unless("unless"),
    Else("else"),
    While("while"),
    Print("print"),
    ;


    public String string;
    private ReservedKind(String string) {
        this.string = string;
    }
}

package lang.token;

public enum ReservedKind {
    Return("return"),
    If("if"),
    Else("else"),
    Break("break"),
    Continue("continue"),
    Default("default"),
    For("for"),
    While("while"),
    Bool("bool"),
    ;


    public String string;
    private ReservedKind(String string) {
        this.string = string;
    }
}

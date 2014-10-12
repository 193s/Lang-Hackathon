package marg.exception;

import marg.token.TokenSet;

public class ParseException extends Exception {
    public ParseException(String message, TokenSet ls) {
        this.message = message;
        this.offset = ls.getOffset();
    }

    String message;
    int offset;

    @Override
    public String getMessage() {
        return message;
    }

    public int getErrorOffset() {
        return offset;
    }
}

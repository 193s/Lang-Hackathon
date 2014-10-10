package marg.token;


import marg.exception.ParseException;

import java.io.EOFException;
import java.util.List;

import static marg.token.TokenKind.*;

public class TokenSet {
	private final List<Token> list;
	private final int length;
	private int offset = 0;
	
    public TokenSet(List<Token> tokens) {
        list = tokens;
        length = tokens.size();
    }
	
	public void unget() {
		if (offset > 0) offset--;
	}
	public void unget(int k) {
		if (offset >= k) offset -= k;
	}
	public Token next() throws EOFException {
        checkEOF();
        return list.get(offset++);
	}
	public Token get() {
		return list.get(offset);
	}

    public boolean is(String s) {
        return s.equals(get().string);
    }

	public boolean isEOF() {
        return get().isEOF();
	}
    public void checkEOF() throws EOFException {
        if (isEOF()) throw new EOFException();
    }
	
    public boolean read(String... args) throws EOFException {
	    for (String t : args) {
	 		Token next = next();
	 		if (!t.equals(next.string)) return false;
		}
		return true;
	}

    public void readP(String... args) throws ParseException, EOFException {
        for (String t : args) {
            if (isEOF()) throw new EOFException();
            Token next = next();
            if (!t.equals(next.string))
                throw new ParseException();
        }
    }
	
	public boolean isName() {
		return isEOF()? false : get().kind == Identifier;
	}

	public boolean isNumber() {
		return isEOF()? false : get().kind == IntLiteral;
	}

    public boolean isBool() {
        return isEOF()? false : get().kind == BoolLiteral;
    }

	public boolean isOperator() {
		return isEOF()? false : get().kind == Operator;
	}

	public boolean isReserved() {
		return isEOF()? false : get().kind == Reserved;
	}
}

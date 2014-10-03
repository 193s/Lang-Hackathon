package marg.parser;

public abstract class AST {
	public boolean succeed = false;
    public abstract int eval(int k, Environment e);
}

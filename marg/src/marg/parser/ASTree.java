package marg.parser;

public abstract class ASTree {
	public boolean succeed = false;
    public abstract int eval(int k, Environment e);
}

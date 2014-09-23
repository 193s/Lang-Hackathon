package lang.parser;

public abstract class AST {
	// 構文木の構築に成功したかどうか
	public boolean succeed = false;
	public abstract int eval(int k, Environment e);
}

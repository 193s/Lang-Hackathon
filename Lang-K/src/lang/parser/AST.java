package lang.parser;

public abstract class AST {
	// 構文木の構築に成功したかどうか
	boolean ok = false;
	
	public int eval(int k, Environment e) {
		return 0;
	}
}

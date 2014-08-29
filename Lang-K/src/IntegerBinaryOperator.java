import java.util.function.BinaryOperator;

public enum IntegerBinaryOperator implements BinaryOperatorIF<Integer> {
    Mod		(0, (left, right) -> left % right),
	Plus	(1, (left, right) -> left + right),
    Minus	(1, (left ,right) -> left - right),
    Mult	(2, (left, right) -> left * right),
    Div		(2, (left, right) -> left / right),
    ;
    
    private int level;	// 優先順位
    private BinaryOperator<Integer> eval;
    
    private IntegerBinaryOperator(int level, BinaryOperator<Integer> function) {
        this.level = level;
        this.eval = function;
    }
    
    @Override public Integer eval(Integer left, Integer right) {
    	return eval.apply(left, right);
    }
    @Override public int getLevel() {
    	return level;
    }
}

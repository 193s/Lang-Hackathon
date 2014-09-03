import java.util.function.BinaryOperator;

public enum IntegerBinaryOperator implements BinaryOperatorIF<Integer> {
    Mod		(3, '%', (left, right) -> left % right),
	Plus	(2, '+', (left, right) -> left + right),
    Minus	(2, '-', (left ,right) -> left - right),
    Mult	(1, '*', (left, right) -> left * right),
    Div		(1, '/', (left, right) -> left / right),
    ;
    
    private int level;	// 優先順位
    private BinaryOperator<Integer> eval;
    private String sign;
    
    private IntegerBinaryOperator(int level, String sign, BinaryOperator<Integer> function) {
        this.level = level;
        this.eval = function;
        this.sign = sign;
    }
    private IntegerBinaryOperator(int level, char sign, BinaryOperator<Integer> function) {
    	this.level = level;
    	this.eval = function;
    	this.sign = String.valueOf(sign);
    }
    
    @Override public Integer eval(Integer left, Integer right) {
    	return eval.apply(left, right);
    }
    @Override public int getLevel() {
    	return level;
    }
    @Override public String getSign() {
    	return sign;
    }
}

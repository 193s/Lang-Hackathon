package lang.parser.operator;
import java.util.function.BinaryOperator;


@Deprecated
public enum RelationalBinaryOperator implements BinaryOperatorIF<Integer> {
//    Equal	(0, "==", (left, right) -> left == right? 1: 0),
//    MoreThan(0, '>',  (left, right) -> left >  right? 1: 0),
//    LessThan(0, '<',  (left, right) -> left <  right? 1: 0),
    ;
    
    private int level;	// 優先順位
    private BinaryOperator<Integer> eval;
    private String sign;
    
    private RelationalBinaryOperator(int level, String sign, BinaryOperator<Integer> function) {
        this.level = level;
        this.eval = function;
        this.sign = sign;
    }
    private RelationalBinaryOperator(int level, char sign, BinaryOperator<Integer> function) {
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

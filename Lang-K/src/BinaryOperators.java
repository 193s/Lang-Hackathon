import java.util.function.BinaryOperator;

public enum BinaryOperators implements BinaryOperatorIF {
    Mod(0, (left, right) ->
    	left instanceof Integer && right instanceof Integer
    	?	(Integer)left % (Integer)right
    	:	null),
    	
	Plus(1, (left, right) ->
    	left instanceof Integer && right instanceof Integer
        ?   (Integer)left + (Integer)right
        :   null),
            
    Minus(1, (left ,right) ->
        left instanceof Integer && right instanceof Integer
        ?   (Integer)left - (Integer)right
        :   null),
            
    Mult(2, (left, right) ->
        left instanceof Integer && right instanceof Integer
        ?   (Integer)left * (Integer)right
        :   null),
            
    Div(2, (left, right) ->
        left instanceof Integer && right instanceof Integer
        ?   (Integer)left / (Integer)right
        :   null),
    ;

    
    // 優先順位
    public int level;
    private BinaryOperator<Object> f;
    
    private BinaryOperators(int level, BinaryOperator<Object> e) {
        this.level = level;
        this.f = e;
    }
    
    
    @Override public Object eval(Object left, Object right) {
    	return f.apply(left, right);
    }
}

package lang.parser.operator;

import java.util.HashMap;
import java.util.function.BinaryOperator;

public class IntegerBinaryOperator2 {
	public static final HashMap<String, BinaryOp<Integer>> map;
	
	static {
		map = new HashMap<String, BinaryOp<Integer>>() {
			{
//				map.put("+", new BinaryOp(1, (a,b) -> (Integer)a + (Integer)b));
//				map.put("-", new BinaryOp<Integer>(1, (a,b) -> a - b));
			}
		};
//		map.put("*", (a,b) -> a * b);
//		map.put("/", (a,b) -> a / b);
//		map.put("%", (a,b) -> a % b);
	}
	
	public class BinaryOp<T> implements BinaryOperator<T> {
		public int level;
		private BinaryOperator<T> eval;
		private BinaryOp(int level, BinaryOperator<T> eval) {
			this.level = level;
			this.eval = eval;
		}

		@Override
		public T apply(T t, T u) {
			return eval.apply(t, u);
		}
	}
}

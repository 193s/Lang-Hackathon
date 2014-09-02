package lang.lexer;

public abstract class Token<T> {
	protected Token(T value) {
		this.value = value;
	}
	
	private T value;
	
	public T getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value + " : " + getClass().toString().substring(6);
	}
	
	
	
	public static class Name extends Token<String> {
		public Name(String value) {
			super(value);
		}
	}
	
	public static class Num extends Token<Integer> {
		public Num(int value) {
			super(value);
		}
	}
	
	public static class Operator extends Token<String> {
		public Operator(String value) {
			super(value);
		}
	}
}

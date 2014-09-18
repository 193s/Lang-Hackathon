package lang.lexer;

public abstract class Token {
	public Token(String string) {
		this.string = string;
	}
	
	public String string;
	
	@Override
	public String toString() {
		return string + "\t: " + getClass().getSimpleName();
	}
	
	
	public static class Reserved extends Token {
		public Reserved(String value) {
			super(value);
		}
	}
	public static class Name extends Token {
		public Name(String value) {
			super(value);
		}
	}
	
	public static class Num extends Token {
		public int num;
		public Num(String value) {
			super(value);
			num = Integer.parseInt(value);
		}
	}
	
	public static class Operator extends Token {
		public Operator(String value) {
			super(value);
		}
	}
}

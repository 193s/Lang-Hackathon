package lang.parser.operator;

public interface BinaryOperatorIF<T> {
	public T eval(T left, T right);
	public int getLevel();
	public String getSign();
}
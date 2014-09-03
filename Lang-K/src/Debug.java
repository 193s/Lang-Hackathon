import java.io.PrintStream;

import lang.util.Extension;


public class Debug {
	public PrintStream out;
	public Debug(PrintStream out) {
		this.out = out;
	}
	
	public void print(int k, String string) {
		out.println(Extension.getSpace(k) + string);
	}
}

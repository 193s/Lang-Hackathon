package lang.debug;
import java.io.PrintStream;

import lang.util.Extension;


public class Debug {
	public PrintStream out;
	public Debug(PrintStream out) {
		this.out = out;
	}
	
	public void log(int k, String string) {
		out.println(Extension.getChar(' ', k) + string);
	}
	
	public void brank() {
		out.println();
	}
	public void brank(int times) {
		out.println(Extension.getChar('\n', times));
	}
}

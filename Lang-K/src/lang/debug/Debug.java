package lang.debug;
import java.io.PrintStream;

import lang.util.Extension;


public class Debug {
	static public PrintStream out = System.out;
	
	static public void log(int k, String string) {
		out.println(Extension.getChar(' ', k) + string);
	}
	
	static public void blank() {
		out.println();
	}
	static public void blank(int times) {
		out.println(Extension.getChar('\n', times));
	}
}

package lang.debug;
import java.io.PrintStream;

import lang.util.Extension;


public class Debug {
    public static boolean enabled = true;
	public static PrintStream out = System.out;
	
	public static void log(int k, String string) {
        if (!enabled) return;
		out.println(Extension.getChar(' ', k) + string);
	}
	
	public static void blank() {
        if (!enabled) return;
		out.println();
	}
	public static void blank(int times) {
        if (!enabled) return;
		out.println(Extension.getChar('\n', times));
	}
}

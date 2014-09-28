package lang.debug;
import java.io.PrintStream;

import lang.util.Extension;


public class Debug {
    public static boolean enabled = false;
	public static final PrintStream out = System.out;

    public static void setEnabled(boolean bool) {
        enabled = bool;
    }

    public static void log(String string) {
        if (!enabled) return;
        out.println(string);
    }
    public static void log(Object o) {
        if (!enabled) return;
        out.println(o);
    }
	public static void log(int k, String string) {
        if (!enabled) return;
		out.println(Extension.getChar(' ', k) + string);
	}
    public static void logf(String format, Object... objects) {
        if (!enabled) return;
        out.printf(format, objects);
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

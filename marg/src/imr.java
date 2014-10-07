import marg.debug.Debug;
import marg.lexer.ILexer;
import marg.lexer.Lexer;
import marg.parser.ASTree;
import marg.parser.Environment;
import marg.parser.IParser;
import marg.parser.Parser;
import marg.token.Token;
import marg.token.TokenSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static marg.debug.Console.*;

public class imr {
    // Interactive Marg
    public static void main(String[] args) {
        Debug.setEnabled(false);

        Environment e = new Environment(null);
        ILexer lexer = new Lexer();
        IParser parser = new Parser();
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(isr);

        try {
            while (true) {
                out.print("Marg> ");
                String s = reader.readLine();
                if ("exit".equals(s)) {
                    out.println("Program will exit.");
                    return;
                }
                if ("values".equals(s)) {
                    out.println("Values:");
                    e.map.entrySet().forEach(out::println);
                    continue;
                }

                Token[] ls;
                try {
                    ls = lexer.tokenize(s);
                }
                catch (NullPointerException ex) {
                    continue;
                }
                try {
                    ASTree ast = parser.parse(new TokenSet(ls));
                    ast.eval(0, e);
                }
                catch (Exception ex) {
                    ex.printStackTrace(out);
                    continue;
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

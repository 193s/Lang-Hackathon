package marg.main;

import marg.exception.ParseException;
import marg.debug.Debug;
import marg.lexer.ILexer;
import marg.lexer.Lexer;
import marg.ast.ASTree;
import marg.parser.Environment;
import marg.parser.IParser;
import marg.parser.Parser;
import marg.token.Token;
import marg.token.TokenSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static marg.debug.Console.*;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class IMR {
    // Interactive Shell
    public static void main(String[] args) {
        Debug.setEnabled(false);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            out.print(ansi().reset());
        }));

        Environment e = new Environment(null);
        ILexer lexer = new Lexer();
        IParser parser = new Parser();
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(isr);

        try {
            while (true) {
                out.print("Marg> ");

                out.print(ansi().fg(GREEN));
                String s = reader.readLine();
                out.print(ansi().reset());

                if ("exit".equals(s)) {
                    out.println("Program will exit...");
                    System.exit(0);
                }
                if ("values".equals(s)) {
                    out.println("Values:");
                    e.map.entrySet().forEach(set ->
                            out.println(set.getKey() + " : " +set.getValue().get()));
                    continue;
                }
                if ("clear".equals(s)) {
                    out.println(ansi().eraseScreen());
                    continue;
                }

                List<Token> ls;
                try {
                    ls = lexer.tokenize(s);
                }
                catch (NullPointerException ex) {
                    // No input
                    continue;
                }

                ASTree ast;
                try {
                    ast = parser.parse(new TokenSet(ls));
                }
                catch (ParseException ex) {
                    out.println("Parse error.");
                    out.println(ex.getMessage());
                    ex.printStackTrace(out);
                    continue;
                }
                catch (Exception ex) {
                    out.println("Unexpected error.");
                    out.println(ansi().fg(RED).a(ex.getStackTrace()).reset());
                    continue;
                }

                try {
                    ast.eval(0, e);
                }
                catch (Exception ex) {
                    out.println("Runtime error.");
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

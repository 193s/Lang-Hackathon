package marg;

import java.io.*;
import java.util.InputMismatchException;

import marg.debug.Debug;
import marg.lexer.ILexer;
import marg.lexer.Lexer;
import marg.parser.IParser;
import marg.token.Token;
import marg.token.TokenSet;
import marg.parser.AST;
import marg.parser.Environment;
import marg.parser.Parser;

import static marg.debug.Console.*;

public class Interpreter {

	public static void main(String[] args) {
        Debug.setEnabled(true);
        CommandLineOption option;
        try {
            option = new CommandLineOption(args);
        }
        catch (FileNotFoundException e) {
            out.println("File not found.");
            return;
        }
        catch (InputMismatchException e) {
            out.println("No input files.");
            return;
        }

        switch(option.type) {
            case Run:
                run(option);
                break;
            case Version:
                out.println("Version: ß");
                break;
            default:
                out.println("Undefined option.");
                break;
        }
    }


    public static void run(CommandLineOption option) {
        String s;
        try {
            s = option.read();
        } catch (IOException e) {
            out.println("ERROR: IOException.");
            return;
        }


    /* ========== Lexical Analyze ========== */
        ILexer lexer = new Lexer();
        Token[] ls = lexer.tokenize(s);
		if (ls.length == 0) {
			Debug.log("ERROR: no input.");
			return;
		}
		else {
            Debug.log("--SUCCEED TOKENIZE--");
        }

        // print all tokens in 'ls'
		for (Token t: ls) Debug.logf(" | %s%n", t);
		Debug.logf("%s Tokens.%n", ls.length);
        Debug.blank(3);



	/* ========== Parse ========== */
        IParser parser = new Parser();
		AST ast = parser.parse(new TokenSet(ls));
        if (ast.succeed) {
            Debug.log("--Parse finished--");
        }
        else {
            Debug.log("Failed to parse. Program will exit.");
            return;
        }
		Debug.blank(3);


    /* ========== Interpret ========== */
		Environment e = new Environment(null);
        Debug.log("--- RUNNING ---");
        Debug.blank();
        try {
            // interpret
            ast.eval(0, e);

            Debug.blank(2);

            // print all variables in Environment.
            Debug.log("Environment:");
            e.map.entrySet().forEach(entry -> Debug.log(entry.getKey() + " : "  + entry.getValue()));
            Debug.blank();
        }
		catch (Exception ex) {
            out.println("RUNTIME ERROR:");
            ex.printStackTrace(Debug.out);
        }
        finally {
            out.println("Process finished.");
        }
	}
}

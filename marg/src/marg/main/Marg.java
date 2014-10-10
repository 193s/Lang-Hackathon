package marg.main;

import java.io.*;
import java.util.InputMismatchException;
import java.util.List;

import marg.command.CommandLineOption;
import marg.exception.ParseException;
import marg.debug.Debug;
import marg.lexer.ILexer;
import marg.lexer.Lexer;
import marg.ast.ASTree;
import marg.parser.IParser;
import marg.token.Token;
import marg.token.TokenSet;
import marg.parser.Environment;
import marg.parser.Parser;

import static marg.debug.Console.*;

public class Marg {

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
        List<Token> ls;
        try {
            ls = lexer.tokenize(s);
        }
        catch (NullPointerException e) {
            out.println("no input.");
            return;
        }

        // print all tokens in 'ls'
        ls.forEach(t -> Debug.logf(" | %s%n", t));
		Debug.logf("%s Tokens.%n", ls.size());
        Debug.blank(3);



	/* ========== Parse ========== */
        IParser parser = new Parser();
        ASTree ast;
        try {
            ast = parser.parse(new TokenSet(ls));
        }
        catch (ParseException e) {
            out.println("Failed to parse. Program will exit.");
            e.printStackTrace();
            return;
        }
        Debug.log("--Parse finished--");
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
            e.map.entrySet().forEach(
                entry -> Debug.log(entry.getKey() + " : "  + entry.getValue())
            );
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

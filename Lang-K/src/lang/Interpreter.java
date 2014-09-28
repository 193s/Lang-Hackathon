package lang;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Map.Entry;

import lang.debug.Debug;
import lang.debug.Console;
import lang.lexer.ILexer;
import lang.lexer.NewLexer;
import lang.parser.IParser;
import lang.token.Token;
import lang.token.TokenSet;
import lang.parser.AST;
import lang.parser.Environment;
import lang.parser.Parser;


public class Interpreter {
	
	public static void main(String[] args) {
        Debug.setEnabled(true);
        String s;
        if (args.length == 0) {
            Console.out.println("Input mismatch.");
            return;
        }
        CommandLineOption option;
        try {
            option = new CommandLineOption(args);
        }
        catch (FileNotFoundException e) {
            Console.out.println("File not found.");
            return;
        }
        catch (InputMismatchException e) {
            Console.out.println("Input mismatch.");
            return;
        }

        try {
            s = option.read();
        }
        catch (IOException e) {
            Console.out.println("ERROR: IOException.");
            return;
        }


	/* ========== Input ========== */
        Debug.log(s);
//        Debug.out.println("input:");
//		String s = new String();
//        try {
//			InputStreamReader isr = new InputStreamReader(System.in);
//			BufferedReader br = new BufferedReader(isr);
//            String input;
//            do {
//                input = br.readLine();
//				s += input + '\n';
//			}
//			while (!input.isEmpty());
//		}
//		catch (IOException e) {
//			blank();
//			Debug.out.println("ERROR: IOException");
//		}
//        finally {
//            if ("\n".equals(s)) {
//                Debug.out.println("ERROR: input string is empty.");
//                return;
//            }
//        }


    /* ========== Lexical Analyze ========== */
        ILexer lexer = new NewLexer();
        Token[] ls = lexer.tokenize(s);
		if (ls == null) {
			Debug.log("ERROR: tokenize failed!");
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
		Debug.blank(3);


    /* ========== Interpret ========== */
		Environment e = new Environment(null);
		try {
			Debug.log("--- RUNNING ---");
            Debug.blank();
            // interpret
            Debug.log((int) ast.eval(0, e));

            Debug.blank(2);

            // print all variables in Environment.
            Debug.log("Environment:");
            for (Entry<String, Object> entry : e.map.entrySet()) {
                Debug.log(entry.getKey() +
                                 " : " + entry.getValue());
            }
            Debug.blank();
        }
		catch (Exception ex) {
            Debug.log("RUNTIME ERROR:");
            ex.printStackTrace(Debug.out);
        }
        finally {
            Debug.log("Process finished.");
        }
	}
}

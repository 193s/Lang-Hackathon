package lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map.Entry;

import lang.debug.Debug;
import lang.lexer.ILexer;
import lang.lexer.NewLexer;
import lang.parser.IParser;
import lang.token.Token;
import lang.token.TokenSet;
import lang.parser.AST;
import lang.parser.Environment;
import lang.parser.Parser;

import static lang.debug.Debug.blank;


public class Interpreter {
	
	public static void main(String[] args) {
	/* ========== Input ========== */
        Debug.out.println("input:");
		String s = new String();
        try {
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
            String input;
            do {
                input = br.readLine();
				s += input + '\n';
			}
			while (!input.isEmpty());
		}
		catch (IOException e) {
			blank();
			Debug.out.println("ERROR: IOException");
		}
        finally {
            if ("\n".equals(s)) {
                Debug.out.println("ERROR: input string is empty.");
                return;
            }
        }


    /* ========== Lexical Analyze ========== */
        ILexer lexer = new NewLexer();
        Token[] ls = lexer.tokenize(s);
		if (ls == null) {
			Debug.out.println("ERROR: tokenize failed!");
			return;
		}
		else {
            Debug.out.println("--SUCCEED TOKENIZE--");
        }
		
		for (Token t: ls) Debug.out.printf(" | %s%n", t); // 字句解析の結果を出力
		Debug.out.printf("%s Tokens.%n", ls.length);
        blank(3);



	/* ========== Parse ========== */
        IParser parser = new Parser();
		AST ast = parser.parse(new TokenSet(ls));
		blank(3);


    /* ========== Interpret ========== */
		Environment e = new Environment();
		try {
			Debug.out.println("--- RUNNING ---");
            blank();
            // Interpret
            Debug.out.println(ast.eval(0, e));

            blank(2);

            // print all variables in Environment.
            Debug.out.println("Environment:");
            for (Entry<String, Integer> entry : e.hashMap.entrySet()) {
                Debug.out.println(entry.getKey() +" : " + entry.getValue());
            }
            blank();
        }
		catch (Exception ex) {
            Debug.out.println("RUNTIME ERROR:");
            ex.printStackTrace();
        }
        finally {
            Debug.out.println("Process finished.");
        }
	}
}

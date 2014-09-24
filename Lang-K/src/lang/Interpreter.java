package lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map.Entry;

import lang.debug.Debug;
import lang.lexer.ILexer;
import lang.lexer.LegacyLexer;
import lang.lexer.Lexer;
import lang.parser.operator.IParser;
import lang.token.Token;
import lang.token.TokenSet;
import lang.parser.AST;
import lang.parser.Environment;
import lang.parser.Parser;


public class Interpreter {
	
	public static void main(String[] args) {
	/* ========== 入力 ========== */
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
			Debug.blank();
			Debug.out.println("ERROR: IOException");
		}
        finally {
            if ("\n".equals(s)) {
                Debug.out.println("ERROR: input string is empty.");
                return;
            }
        }


    /* ========== 字句解析 ========== */
        ILexer lexer = new Lexer();
        Token[] ls = lexer.tokenize(s);
		if (ls == null) {
			Debug.out.println("ERROR: tokenize failed!");
			return;
		}
		else {
            Debug.out.println("--SUCCEED TOKENIZE--");
        }
		
		for (Token t: ls) Debug.out.printf(" [ %s ]%n", t); // 字句解析の結果を出力
		Debug.blank(3);

	/* ========== 構文解析 ========== */
        IParser parser = new Parser();
		AST ast = parser.parse(new TokenSet(ls));
		Debug.blank(3);
		
    /* ========== 実行 ========== */
		Environment e = new Environment();	// 環境
		try {
			Debug.out.println("--- RUNNING ---");
            Debug.blank();
            Debug.out.println(ast.eval(0, e)); // 実行

            Debug.blank(2);

            // Environmentに保存されている変数を列挙
            Debug.out.println("Environment:");
            for (Entry<String, Integer> entry : e.hashMap.entrySet()) {
                Debug.out.println(entry.getKey() +" : " + entry.getValue());
            }
			Debug.blank();
			Debug.out.println(ast.eval(0, e)); // 実行
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

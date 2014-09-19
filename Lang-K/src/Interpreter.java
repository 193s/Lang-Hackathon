import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import lang.debug.Debug;
import lang.lexer.Lexer;
import lang.lexer.Token;
import lang.lexer.TokenSet;
import lang.operator.BinaryOperatorIF;
import lang.operator.IntegerBinaryOperator;
import lang.parser.Environment;
import lang.util.Extension;


public class Interpreter {
	static Debug debug = new Debug(System.out);
	
	public static void main(String[] args) {
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
			debug.brank();
			debug.out.println("ERROR: IOException");
		}
		
		if ("\n".equals(s)) {
			debug.out.println("ERROR: input string is empty.");
			return;
		}
		
		Token[] ls = Lexer.tokenize(s);		// 字句解析
		if (ls == null) {
			debug.out.println("ERROR: tokenize failed!");
			return;
		}
		else debug.out.println("--SUCCEED TOKENIZING--");
		
		for (Token t: ls) debug.out.printf(" [ %s ]%n", t); // 字句解析の結果を出力
		debug.brank(3);
		
		
		AST ast = new Program(new TokenSet(ls));	// 構文解析
		if (ast.ok) {
			debug.out.println("--SUCCEED PARSING--");
		}
		else {
			debug.out.println("ERROR: Parsing failed!");
			return;
		}
		debug.brank(3);
		
		Environment e = new Environment();	// 環境
		try {
			debug.out.println("--- RUNNING ---");
			debug.brank();
			debug.out.println(ast.eval(0, e)); // 実行
		}
		catch (Exception ex) {
			debug.out.println("RUNTIME ERROR:");
			ex.printStackTrace();
		}
		
		debug.brank(2);
		
		// Environmentに保存されている変数を列挙
		debug.out.println("Environment:");
		for(Entry<String, Integer> entry : e.hashMap.entrySet())
			debug.out.println(entry.getKey() +" : " + entry.getValue());
	}
	
	
	static abstract class AST {
		// 構文木の構築に成功したかどうか
		boolean ok = false;
		
		int eval(int k, Environment e) {
			return 0;
		}
	}
	
	
	static class ASTList extends AST {
		ArrayList<AST> children;
		ASTList() {
			children = new ArrayList<AST>();
		}
	}
	
	@Deprecated
	static class ASTLeaf extends AST {
		Token child;
		ASTLeaf() {}
		ASTLeaf(Token l) {
			child = l;
		}
	}

	
	/*	
	 *	Number	::= '(' Expr ')' | NumberToken | Variable
	 *	Expr ::= Number { BinaryOperator Number }
	 *
	 *	Assign	 ::= Variable '=' Statement
	 *	
	 *	Condition ::= Expr RelationalOperator Expr
	 *	While	  ::= 'while' '(' Condition ')' '{' Program '}'
	 *	If		  ::= 'if' '(' Condition ')' '{' Program '}'
	 *
	 *
	 *	Statement ::= Assign | Expr | While | If
	 *	Program   ::= Statement {';' Statement? }
	 *
	 */
	
	static class Variable extends AST {
		String name;
		Variable(String name) {
			this.name = name;
		}
	
		@Override
		int eval(int k, Environment e) {
			Integer v = e.hashMap.get(name);
			int value = (v == null)? 0: v;
			debug.log(k, "Variable : " + name + '(' + value + ')');
			return value;
		}
	}
	
	static class Literal extends AST {
		int value;
		Literal(TokenSet ls) {
			Token t = ls.next();
			value = Integer.parseInt(t.string);
		}
		Literal(Token token) {
			value = Integer.parseInt(token.string);
		}
		Literal(String str) {
			value = Integer.parseInt(str);
		}
		
		@Override
		int eval(int k, Environment e) {
			debug.log(k, "Literal");
			return value;
		}
	}
	
	static class Expr extends ASTList {
		ArrayList<BinaryOperatorIF> operators;
		
		Expr(TokenSet ls) {
			operators = new ArrayList<BinaryOperatorIF>();
			Num n = new Num(ls);
			if (!n.ok) return;
			children = new ArrayList<AST>();
			children.add(n);
			
			while (ls.isOperator()) {
				String opstr = ls.readOperator().string;
				
				BinaryOperatorIF binaryOperator = null;
				for (BinaryOperatorIF b: IntegerBinaryOperator.values()) {
					if (b.getSign().equals(opstr)) {
						binaryOperator = b;
						break;
					}
				}
				if (binaryOperator == null) {
					ls.unget();
					break;
				}
				operators.add(binaryOperator);
				
				Num n2 = new Num(ls);
				if (!n2.ok) return;
				children.add(n2);
			}
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			debug.log(k, "Expr");
			ArrayList<Object> vals = new ArrayList<Object>();
			
			int count = 0;
			for (AST v: children) {
				vals.add(((Num)v).eval(k+1, e));
				if (count >= operators.size()) break;
				debug.out.println(operators.get(count).getSign());
				count++;
			}
			
			ArrayList<BinaryOperatorIF> ops_cpy = new ArrayList<BinaryOperatorIF>(operators);
			ArrayList<BinaryOperatorIF> ops_cpy2 = new ArrayList<BinaryOperatorIF>();
			ArrayList<Object> vals_ = new ArrayList<Object>();
			
			int max_level = 5;
			for (int il=0; il<max_level; il++) {
				ops_cpy2.clear();
				vals_.clear();
				
				vals_.add(vals.get(0));
				for (int i=0; i<ops_cpy.size(); i++) {
					if (ops_cpy.get(i).getLevel() == il) {
						vals_.add(ops_cpy.get(i).eval(vals_.remove(vals_.size()-1), vals.get(i+1)));
					} else {
						vals_.add(vals.get(i+1));
						ops_cpy2.add(ops_cpy.get(i));
					}
				}
				ops_cpy = new ArrayList<BinaryOperatorIF>(ops_cpy2);
				vals = new ArrayList<Object>(vals_);
			}
			return (Integer) vals.get(0);
		}
	}
	
	static class Num extends ASTList {
		Num(TokenSet ls) {
			debug.out.println("num");
			// ( Expression )
			if (ls.isOperator()) {
				debug.out.println("( expression )");
				if (!ls.read("(")) return;
				AST s = new Expr(ls);
				if (!s.ok) return;
				if (!ls.read(")")) return;
				children.add(s);
			}

			// Literal
			else if (ls.isNumber()) {
				debug.out.println("literal");
				children.add(new Literal(ls));
			}
			
			// Variable
			else if (ls.isName()) {
				debug.out.println("variable");
				String ident = ls.next().string;
				Variable v = new Variable(ident);
				children.add(v);
			}
			else return;
			
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			debug.log(k, "Num");
			AST child = children.get(0);
			return child.eval(k+1, e);
		}
	}

	
	static class Statement extends ASTList {
		Statement(TokenSet ls) {
			AST child = 
			  ls.isReserved("while")?	new While(ls)
			: ls.isReserved("if")?		new If(ls)
			: ls.isName()?				new Assign(ls)
			: 							new Expr(ls)
			;
			children.add(child);
			ok = true;
		}

		@Override
		int eval(int k, Environment e) {
			debug.log(k, "Statement");
			AST child = children.get(0);
			return child.eval(k+1, e);
		}
	}
	
	
	static class Program extends ASTList {
		Program(TokenSet ls) {
			AST s = new Statement(ls);
			if (!s.ok) return;
			children.add(s);

			while (true) {
				if (ls.isEOF()) break;
				if (!ls.isMatch(",")) break;
				Token operator = ls.next();
				
				AST right = new Statement(ls);
				if (!right.ok) continue;

				children.add(new ASTLeaf(operator));
				children.add(right);
			}
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			debug.log(k, "Program");
			int ret = children.get(0).eval(k+1, e);
			
			for (int i=1; i<children.size(); i++) {
				if (children.get(i) instanceof Statement) ret = children.get(i).eval(k+1, e);
				else {
					debug.log(k, ((ASTLeaf)children.get(i)).child.string);
				}
			}
			return ret;
		}
	}

	
	static class Assign extends ASTList {
		Assign(TokenSet ls) {
			debug.out.println("assign");
			if (ls.isEOF()) return;
			AST left = new Variable(ls.next().string);
			
			if (ls.isEOF()) return;
			Token operator = ls.next();
			if (!"=".equals(operator.string)) return;
			
			AST right = new Statement(ls);
			if (!right.ok) return;
			
			children.add(left);
			children.add(new ASTLeaf(operator));
			children.add(right);

			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			debug.log(k, "Assign");
			int ret = ((Statement)children.get(2)).eval(k+1, e);
			// 代入
			String identifier = ((Variable)children.get(0)).name;
			e.hashMap.put(identifier, ret);
			return ret;
		}
	}
	
	
	static class Condition extends ASTList {
		Condition(TokenSet ls) {
			debug.out.println("condition");
			AST left = new Expr(ls);
			if (!left.ok) return;
			children.add(left);
			
			if (!ls.isMatch("==", ">", "<")) return;
			children.add(new ASTLeaf(ls.next()));
			AST right = new Expr(ls);
			if (!right.ok) return;
			children.add(right);
			
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			String op = ((ASTLeaf) children.get(1)).child.string;
			debug.log(k, "Condition" + op);
			
			int left = children.get(0).eval(k+1, e);
			int right = children.get(2).eval(k+1, e);
			
			int ret = 0;
			if		(">".equals(op))  ret = left > right ? 1:0;
			else if ("==".equals(op)) ret = left == right? 1:0;
			else if ("<".equals(op))  ret = left < right ? 1:0;
			
			return ret;
		}
	}


	static class While extends ASTList {
		While(TokenSet ls) {
			debug.out.println("while");
			if (!ls.read("while", "(")) return;
			AST condition = new Condition(ls);
			if (!condition.ok) return;
			if (!ls.read(")")) return;
			AST block = new Block(ls);
			
			children.add(condition);
			children.add(block);
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			debug.log(k, "While");
			
			AST condition = children.get(0);
			AST program = children.get(1);
			
			int ret = 0;
			while (condition.eval(k+1, e) == 1) {
				ret = program.eval(k+1, e);
			}
			return ret;
		}
	}
	static class Block extends ASTList {
		Block(TokenSet ls) {
			debug.out.println("block");
			if (!ls.read(":")) return;
			AST program = new Program(ls);
			if (!ls.read(";")) return;
			children.add(program);
			ok = true;
		}
		
		@Override
		public int eval(int k, Environment e) {
			debug.log(k, "block");
			return children.get(0).eval(k+1, e);
		}
	}
	
	
	static class If extends ASTList {
		If(TokenSet ls) {
			debug.out.println("if");
			if (!ls.read("if", "(")) return;
			
			AST condition = new Condition(ls);
			if (!condition.ok) return;
			if (!ls.read(")")) return;
			AST block = new Block(ls);
			
			children.add(condition);
			children.add(block);
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			debug.log(k, "If");
			
			int ret = 0;
			AST condition = children.get(0);
			AST program = children.get(1);
			
			if (condition.eval(k+1, e) == 1) {
				ret = program.eval(k+1, e);
			}
			return ret;
		}
	}
}

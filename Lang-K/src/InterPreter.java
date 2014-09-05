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
import lang.util.Extension;


public class InterPreter {
	static Debug debug = new Debug(System.out);
	
	public static void main(String[] args) {
		String s = new String();
		try {	
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			String tokenSet;
			do {
				tokenSet = br.readLine();
				s += tokenSet + '\n';
			}
			while (!tokenSet.isEmpty());
		}
		catch (IOException e) {
			e.printStackTrace();
			debug.out.println("\nInput Error");
		}
		
		debug.brank(3);
		Token[] ls = Lexer.tokenize(s);		// 字句解析
		for (Token t: ls) debug.out.printf(" [%s]%n", t); // 字句解析の結果を出力
		debug.out.println();	//改行
		
		AST ast = new Program(new TokenSet(ls));	// 構文解析
		if(!ast.ok) {
			debug.out.println("Parsing failed!");
			return;
		}
		
		Environment e = new Environment();	// 環境
		try {
			debug.out.println(ast.eval(0, e)); // 実行
		}
		catch(Exception ex) {
			debug.out.println("Runtime Error:");
			ex.printStackTrace();
		}
		
		// Environmentに保存されている変数を列挙
		for(Entry<String, Integer> entry : e.hashMap.entrySet())
			debug.out.println(entry.getKey() +" : " + entry.getValue());
	}
	
	
	
	

	static class Environment { 
		HashMap<String, Integer> hashMap;
		Environment() {
			hashMap = new HashMap<String, Integer>();
		}
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
	
	
	static class ASTLeaf extends AST {
		Token child;
		ASTLeaf() {}
		ASTLeaf(Token l) {
			child = l;
		}
	}

	
	/*	
	 *	Variable ::= NameToken
	 *	
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
	
	static class Variable extends ASTLeaf {
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
	
	static class Expr extends ASTList {
		ArrayList<BinaryOperatorIF> operators;
		Expr(TokenSet tokenSet) {
			operators = new ArrayList<BinaryOperatorIF>();
			Num n = new Num(tokenSet);
			if (!n.ok) return;
			children = new ArrayList<AST>();
			children.add(n);
			
			while (tokenSet.isOperator()) {
				String opstr = tokenSet.readOperator().getValue();
				
				BinaryOperatorIF binaryOperator = null;
				for (BinaryOperatorIF b: IntegerBinaryOperator.values()) {
					if (b.getSign().equals(opstr)) {
						binaryOperator = b;
						break;
					}
				}
				if (binaryOperator == null) {
					tokenSet.back();
					break;
				}
				operators.add(binaryOperator);
				
				Num n2 = new Num(tokenSet);
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
		Num(TokenSet tokenSet) {
			if (tokenSet.isOperator()) {
				String op = (tokenSet.readOperator()).getValue();
				if (! "(".equals(op)) return;
				AST s = new Expr(tokenSet);
				if (!s.ok) return;
				
				if (! tokenSet.isOperator()) return;
				if (! ")".equals(tokenSet.readOperator().getValue())) return;
				children.add(s);
			}

			else if (tokenSet.isNumber()) {
				children.add(new ASTLeaf(tokenSet.readNumber()));
			}
			
			else if (tokenSet.isName()) {
				String ident = tokenSet.readName().getValue();
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
			if (child instanceof Expr || child instanceof Variable)
				return child.eval(k + 1, e);
			else {
				int value = ((Token.Num)((ASTLeaf)child).child).getValue();
				debug.log(k, " : " + value);
				return value;
			}
		}
	}

	
	static class Statement extends ASTList {
		Statement(TokenSet tokenSet) {
			AST child = null;
			if (tokenSet.isReserved("while"))
				child = new While(tokenSet);
			else if (tokenSet.isReserved("if"))
				child = new If(tokenSet);
			else if (tokenSet.isName())
				child = new Assign(tokenSet);
			else child = new Expr(tokenSet);

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
		Program(TokenSet tokenSet) {
			AST s = new Statement(tokenSet);
			if (!s.ok) return;
			children.add(s);

			while (true) {
				if (!tokenSet.isOperator()) break;
				Token.Operator operator = tokenSet.readOperator();
				String op = operator.getValue();
				if (!";".equals(op)) {
					tokenSet.back();
					break;
				}
				
				AST right = new Statement(tokenSet);
				if (!right.ok) continue;

				children.add(new ASTLeaf(operator));
				children.add(right);
			}
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			debug.log(k, "Program");
			int ret = children.get(0).eval(k + 1, e);
			
			for (int i=1; i<children.size(); i++) {
				if (children.get(i) instanceof Statement) ret = children.get(i).eval(k+1, e);
				else {
					debug.log(k, (String) ((ASTLeaf)children.get(i)).child.getValue());
				}
			}
			return ret;
		}
	}

	
	static class Assign extends ASTList {
		Assign(TokenSet tokenSet) {
			if (!tokenSet.isName()) return;
			AST left = new Variable(tokenSet.readName().getValue());
			
			if (!tokenSet.isOperator()) return;
			Token.Operator operator = tokenSet.readOperator();
			if (!"=".equals(operator.getValue())) return;
			
			AST right = new Statement(tokenSet);
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
			e.hashMap.put(((Variable)children.get(0)).name, ret);
			return ret;
		}
	}
	
	
	static class Condition extends ASTList {
		Condition(TokenSet tokenSet) {
			AST left = new Expr(tokenSet);
			if (!left.ok) return;
			children.add(left);
			
			if (!tokenSet.isOperator()) return;
			Token.Operator operator = tokenSet.readOperator();
			String op = operator.getValue();
			
			if (!( "==".equals(op) || ">".equals(op) || "<".equals(op)) ) return;
			children.add(new ASTLeaf(operator));
			
			AST right = new Expr(tokenSet);
			if (!right.ok) return;
			children.add(right);
			
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			String op = (String) (((ASTLeaf) children.get(1)).child.getValue());
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
		While(TokenSet tokenSet) {
			if (!tokenSet.isReserved()) return;
			if (!tokenSet.isOperator("while")) debug.out.println("error:::");
			tokenSet.readOperator();
//			if (!"while".equals((String) tokenSet.readReserved().getValue())) return;

			if (!tokenSet.isOperator()) return;
			if (!"(".equals((String) (tokenSet.readOperator()).getValue())) return;
			
			AST condition = new Condition(tokenSet);
			if (!condition.ok) return;
			
			if (!tokenSet.isOperator()) return;
			if (!")".equals((String) tokenSet.readOperator().getValue())) return;
			
			if (!tokenSet.isOperator()) return;
			if (!"{".equals((String) tokenSet.readOperator().getValue())) return;
			
			AST program = new Program(tokenSet);
			if (!program.ok) return;
			
			if (!tokenSet.isOperator()) return;
			if (!"}".equals((String) tokenSet.readOperator().getValue())) return;
			
			children.add(condition);
			children.add(program);

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
	
	
	static class If extends ASTList {
		If(TokenSet tokenSet) {
			if (!tokenSet.isReserved()) return;
			if (!"if".equals((String) tokenSet.readReserved().getValue())) return;

			if (!tokenSet.isOperator()) return;
			if (!"(".equals((String) tokenSet.readOperator().getValue())) return;
			
			AST condition = new Condition(tokenSet);
			if (!condition.ok) return;
			
			if (!tokenSet.isOperator()) return;
			if (!")".equals((String) tokenSet.readOperator().getValue())) return;
			
			if (!tokenSet.isOperator()) return;
			if (!"{".equals((String) tokenSet.readOperator().getValue())) return;
			
			AST program = new Program(tokenSet);
			if (!program.ok) return;
			
			if (!tokenSet.isOperator()) return;
			if (!"}".equals((String) tokenSet.readOperator().getValue())) return;
			
			children.add(condition);
			children.add(program);

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

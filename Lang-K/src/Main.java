import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import lang.lexer.Lexer;
import lang.lexer.Token;
import lang.util.Extension;


public class Main {
	public static void main(String args[]) {
		String s = "";
		
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		while (true) {
			try {
				String input = br.readLine();
				if (input.isEmpty()) break;
				s += input + "\n";
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("\nInput Error");
			}
		}
		
		System.out.println(s + '\n');	// 入力 + 改行
		Token[] ls = Lexer.tokenize(s);		// 字句解析
		for (Token t: ls) System.out.println(" [" + t + ']');	// 字句解析の結果を出力
		System.out.println();	//改行
		
		AST ast = new Program(new TokenSet(ls));	// 構文解析
		if(!ast.ok) {
			System.out.println("Parsing failed!");
			return;
		}
		
		Environment e = new Environment();	// 環境
		System.out.println(ast.eval(0, e));
		
		// Environmentに保存されている変数を列挙
		for(Entry<String, Integer> entry : e.hashMap.entrySet())
			System.out.println(entry.getKey() +" : " + entry.getValue());
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
			System.out.println(Extension.getSpace(k) + "Variable : " + name + '(' + value + ')');
			return value;
		}
	}
	
	static class Expr extends ASTList {
		ArrayList<BinaryOperatorIF> operators;
		Expr(TokenSet input) {
			operators = new ArrayList<BinaryOperatorIF>();
			Num n = new Num(input);
			if (!n.ok) return;
			children = new ArrayList<AST>();
			children.add(n);
			
			while (true) {
				if (!input.isOperator()) break;
				String opstr = input.readOperator().getValue();
				
				BinaryOperatorIF binaryOperator = null;
				for (BinaryOperatorIF b: IntegerBinaryOperator.values()) {
					if (b.getSign().equals(opstr)) {
						binaryOperator = b;
						break;
					}
				}
				if (binaryOperator == null) break;
				operators.add(binaryOperator);
				
				Num n2 = new Num(input);
				if(!n2.ok) return;
				children.add(n2);
			}
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			System.out.println(Extension.getSpace(k) + "Expr");
			ArrayList<Object> vals = new ArrayList<Object>();
			
			int count = 0;
			for (AST v: children) {
				vals.add(((Num)v).eval(k+1, e));
				if (count >= operators.size()) break;
				System.out.println(operators.get(count).getSign());
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
		Num(TokenSet input) {
			if (input.isOperator()) {
				String op = (input.readOperator()).getValue();
				if (! "(".equals(op)) return;
				AST s = new Expr(input);
				if (!s.ok) return;
				
				if (! input.isOperator()) return;
				if (! ")".equals(input.readOperator().getValue())) return;
				children.add(s);
			}

			else if (input.isNumber()) {
				children.add(new ASTLeaf(input.readNumber()));
			}
			
			else if (input.isName()) {
				String ident = input.readName().getValue();
				Variable v = new Variable(ident);
				children.add(v);
			}
			else return;
			
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			System.out.println(Extension.getSpace(k) + "Num");
			AST child = children.get(0);
			if (child instanceof Expr || child instanceof Variable)
				return child.eval(k + 1, e);
			else {
				int value = ((Token.Num)((ASTLeaf)child).child).getValue();
				System.out.println(Extension.getSpace(k) + " : " + value);
				return value;
			}
		}
	}

	
	static class Statement extends ASTList {
		Statement(TokenSet input) {
			AST child = new While(input);
			if (!child.ok) {
				child = new If(input);
				if (!child.ok) {
					child = new Assign(input);
					if (!child.ok) {
						child = new Expr(input);
						if (!child.ok) return;
					}
				}
			}

			children.add(child);
			ok = true;
		}

		@Override
		int eval(int k, Environment e) {
			System.out.println(Extension.getSpace(k) + "Statement");

			AST child = children.get(0);
			return child.eval(k+1, e);
		}
	}
	
	
	static class Program extends ASTList {
		Program(TokenSet input) {
			AST s = new Statement(input);
			if (!s.ok) return;
			children.add(s);

			while (true) {
				if (!input.isOperator()) break;
				Token.Operator operator = input.readOperator();
				String op = operator.getValue();
				if (!";".equals(op)) break;
				
				AST right = new Statement(input);
				if (!right.ok) continue;

				children.add(new ASTLeaf(operator));
				children.add(right);
			}
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			System.out.println(Extension.getSpace(k) + "Program");
			int ret = children.get(0).eval(k + 1, e);
			
			for (int i=1; i<children.size(); i++) {
				if (children.get(i) instanceof Statement) ret = children.get(i).eval(k+1, e);
				else {
					System.out.println(Extension.getSpace(k) +
							((ASTLeaf)(children.get(i))).child.getValue().toString());
				}
			}
			return ret;
		}
	}

	
	static class Assign extends ASTList {
		Assign(TokenSet input) {
			if (!input.isName()) return;
			AST left = new Variable(input.readName().getValue());
			
			if (!input.isOperator()) return;
			if (!("=".equals(input.readOperator().getValue()))) return;
			
			AST right = new Statement(input);
			if (!right.ok) return;
			
			children.add(left);
			children.add(new ASTLeaf(input.getNext()));
			children.add(right);

			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			System.out.println(Extension.getSpace(k) + "Assign");
			int ret = ((Statement)children.get(2)).eval(k+1, e);
			e.hashMap.put(((Variable)children.get(0)).name, ret);
			return ret;
		}
	}
	
	
	static class Condition extends ASTList {
		Condition(TokenSet input) {
			AST left = new Expr(input);
			if (!left.ok) return;
			children.add(left);
			
			if (!input.isOperator()) return;
			Token.Operator operator = input.readOperator();
			String op = operator.getValue();
			
			if (!( "==".equals(op) || ">".equals(op) || "<".equals(op)) ) return;
			children.add(new ASTLeaf(operator));
			
			AST right = new Expr(input);
			if (!right.ok) return;
			children.add(right);
			
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			String op = (String) (((ASTLeaf) children.get(1)).child.getValue());
			System.out.println(Extension.getSpace(k) + "Condition : " + op);
			
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
		While(TokenSet input) {
			Token nextToken = input.get();
			if (!input.isReserved()) return;
			if (!"while".equals((String) (((Token.Reserved) input.get()).getValue()))) return;

			if (!input.isOperator()) return;
			if (!"(".equals((String) (input.readOperator()).getValue())) return;
			
			AST condition = new Condition(input);
			if (!condition.ok) return;
			
			if (!input.isOperator()) return;
			if (!")".equals((String) input.readOperator().getValue())) return;
			
			if (!input.isOperator()) return;
			if (!"{".equals((String) input.readOperator().getValue())) return;
			
			AST program = new Program(input);
			if (!program.ok) return;
			
			if (!input.isOperator()) return;
			if (!"}".equals((String) input.readOperator().getValue())) return;
			
			children.add(condition);
			children.add(program);

			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			System.out.println(Extension.getSpace(k) + "While");
			
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
		If(TokenSet input) {
			if (input.offset + 7 >= input.length) return;
			
			if (!input.isReserved()) return;
			if (!"if".equals((String) input.readReserved().getValue())) return;

			if (!input.isOperator()) return;
			if (!"(".equals((String) input.readOperator().getValue())) return;
			
			AST condition = new Condition(input);
			if (!condition.ok) return;
			
			if (!input.isOperator()) return;
			if (!")".equals((String) input.readOperator().getValue())) return;
			
			if (!input.isOperator()) return;
			if (!"{".equals((String) input.readOperator().getValue())) return;
			
			AST program = new Program(input);
			if (!program.ok) return;
			
			if (!input.isOperator()) return;
			if (!"}".equals((String) input.readOperator().getValue())) return;
			
			children.add(condition);
			children.add(program);

			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			System.out.println(Extension.getSpace(k) + "If");
			
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

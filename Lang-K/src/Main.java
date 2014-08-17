import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import lang.Extension;


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
		Token[] ls = tokenizer(s);		// 字句解析
		for (Token t: ls) System.out.println(" [" + t + ']');	// 字句解析の結果を出力
		System.out.println();	//改行
		
		AST ast = new Program(new TokenInput(ls));	// 構文解析
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

	// 字句解析器
	public static Token[] tokenizer(String s) {
		String[][] m = Extension.matchAll(s,
				"\\s*((==|[-+*/=;<>\\{\\}\\(\\)])|([0-9]+)|([a-zA-z]+))\\s*");
		
		Token[] ret = new Token[m.length];
		for (int i=0; i<m.length; i++) {
			if		(m[i][2] != null) ret[i] = new Token.Operator(m[i][2]);
			else if	(m[i][3] != null) ret[i] = new Token.Num(Integer.parseInt(m[i][3]));
			else if (m[i][4] != null) ret[i] = new Token.Name(m[i][4]);
		}
		return ret;
	}

	
	static class TokenInput implements Cloneable {
		static Token[] tokens;
		static int length;
		int offset = 0;
		
		TokenInput(Token[] tokens) {
			this.tokens = tokens;
			length = tokens.length;
		}
		Token getNext() {
			return tokens[offset+1];
		}
		Token get() {
			return tokens[offset];
		}
		Token get(int skip) {
			return tokens[offset + skip];
		}
		
		
		@Override
		public TokenInput clone() {
			TokenInput r;
			try {
				r = (TokenInput) super.clone();
			}
			catch (CloneNotSupportedException e) {
				throw new RuntimeException();
			}
			return r;
		}
		
		TokenInput clone(int skip) {
			TokenInput r;
			try {
				r = (TokenInput) super.clone();
				r.offset += skip;
			}
			catch (CloneNotSupportedException e) {
				throw new RuntimeException();
			}
			return r;
		}
	}
	
	

	static class Environment { 
		HashMap<String, Integer> hashMap;
		Environment() {
			hashMap = new HashMap<String, Integer>();
		}
	}
	
	

	static class AST {
		// その要素に含まれる合計のトークン数
		int num_token = 0;
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
		ASTLeaf() {
		}
		ASTLeaf(Token l) {
			child = l;
		}
	}

	
	/*	
	 *	Variable ::= NameToken
	 *	
	 *	Number	::= '(' Sum ')' | NumberToken | Variable
	 *
	 *		//	*Mod	::= Number ('%' Number)?
	 *	// Prod		::= Number { ('*' | '/') Number }
	 *	// Sum		::= Prod { ('+' | '-') Prod }
	 *	new [
	 *		Number	::= '(' Sum ')' | '-'?NumberToken | Variable
	 *		Prod 	::= Number ( ('*' | '/') (Prod | Number) )?
	 *		Sum		::= Prod ( ('+' | '-') (Sum | Prod) )?
	 *	]
	 *	
	 *	Assign	 ::= Variable '=' Statement
	 *	
	 *	Condition ::= Sum ('>' | '==' | '<') Sum
	 *	While	  ::= 'while' '(' Condition ')' '{' Program '}'
	 *	If		  ::= 'if' '(' Condition ')' '{' Program '}'
	 *
	 *
	 *	Statement ::= Assign | Sum | While | If
	 *	Program   ::= Statement {';' Statement? }
	 *
	 */
	
	static class Variable extends ASTLeaf {
		String name;
		Variable(String name) {
			this.name = name;
			num_token = 1;
		}
		
		@Override
		int eval(int k, Environment e) {
			for (int i=0; i<k; i++) System.out.print(' ');
			Integer v = e.hashMap.get(name);
			int value = v == null? 0: v;
			System.out.println("Variable : " + name + '(' + value + ')');
			return value;
		}
	}
	
	
	static class Num extends ASTList {
		Num(TokenInput input) {
			Token nextToken = input.get();
			if (nextToken instanceof Token.Operator) {
				String op = ((Token.Operator)nextToken).getValue();
				if(!op.equals("(")) return;
				AST s = new Sum(input.clone(1));
				if(!s.ok) return;
				if(input.offset + s.num_token + 1 >= input.length) return;
				if(!(input.get(s.num_token + 1) instanceof Token.Operator)) return;
				if(!((Token.Operator)input.get(s.num_token + 1)).getValue().equals(")")) return;
				num_token = 1 + s.num_token + 1;
				children.add(s);
			}

			else if (nextToken instanceof Token.Num) {
				children.add(new ASTLeaf(nextToken));
				num_token = 1;
			}
			
			else if (nextToken instanceof Token.Name) {
				String ident = (String)input.get().getValue();
				Variable v = new Variable(ident);
				children.add(v);
				num_token = 1;
			}
			
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			for (int i=0; i<k; i++) System.out.print(' ');
			System.out.println("Num");
			AST child = children.get(0);
			if		(child instanceof Sum)		return child.eval(k + 1, e);
			else if (child instanceof Variable) return child.eval(k + 1, e);
			else {
				for (int i=0; i<k; i++) System.out.print(' ');
				int value = ((Token.Num)((ASTLeaf)child).child).getValue();
				System.out.println(" : " + value);
				return value;
			}
		}
	}
	
	
	static class Sum extends ASTList {
		Sum(TokenInput input) {
			AST left = new Prod(input.clone());
			if (!left.ok) return;
			children.add(left);
			num_token = left.num_token;
			ok = true;
			// 左辺成立
			
			if (input.offset + num_token + 1 >= input.length) return;
			Token nextToken = input.get(num_token);
			
			if (!(nextToken instanceof Token.Operator)) return;
				
			if (!(((Token.Operator) nextToken).getValue().equals("+")) &&
				!(((Token.Operator) nextToken).getValue().equals("-"))) return;
			
			AST right = new Sum(input.clone(num_token + 1));
			if (!right.ok) {
				right = new Prod(input.clone(num_token + 1));
				if (!right.ok) return;
			}
			
			children.add(new ASTLeaf(nextToken));
			children.add(right);
			
			num_token += right.num_token + 1;
		}
		
		@Override
		int eval(int k, Environment e) {
			System.out.println(Extension.getSpace(k) + "Sum");
			int ret = children.get(0).eval(k + 1, e);

			// 右辺含む
			if (children.size() == 3) {
				ASTLeaf operator = (ASTLeaf)children.get(1);
				String op = ((Token.Operator) operator.child).getValue();
				
				System.out.println(Extension.getSpace(k) + op);
			
				int right = children.get(2).eval(k + 1, e);
				if		(op.equals("+")) ret += right;
				else if (op.equals("-")) ret = ret - right;
			}
			return ret;
		}
	}
	
	
	static class Prod extends ASTList {
		Prod(TokenInput input) {
			AST left = new Num(input.clone());
			if (!left.ok) return;
			children.add(left);
			num_token = left.num_token;
			
			ok = true;
			// 左辺成立
			
			if (input.offset + num_token + 1 >= input.length) return;

			Token nextToken = input.get(num_token);
			if (!(nextToken instanceof Token.Operator)) return;

			if (!(((Token.Operator)(nextToken)).getValue().equals("*")) &&
				!(((Token.Operator)(nextToken)).getValue().equals("/"))) return;
				
			AST right = new Num(input.clone(num_token + 1));
			if (!right.ok) return;
				
			children.add(new ASTLeaf(nextToken));
			children.add(right);

			num_token += right.num_token + 1;
		}
		
		@Override
		int eval(int k, Environment e) {
			for (int i=0; i<k; i++) System.out.print(' ');
			System.out.println("Prod");
			int ret = children.get(0).eval(k + 1, e);
			
			// 右辺含む
			if (children.size() == 3) {
				ASTLeaf operator = (ASTLeaf)children.get(1);
				String op = ((Token.Operator) operator.child).getValue();
				
				System.out.println(Extension.getSpace(k) + op);

				int right = children.get(2).eval(k + 1, e);

				if		(op.equals("*")) ret *= right;
				else if (op.equals("/")) ret /= right;
			}
			return ret;
		}
	}

	
	static class Statement extends ASTList {
		Statement(TokenInput input) {
			AST child = new While(input.clone());
			if (!child.ok) {
				child = new If(input.clone());
				if (!child.ok) {
					child = new Assign(input.clone());
					if (!child.ok) {
						child = new Sum(input.clone());
						if (!child.ok) return;
					}
				}
			}

			children.add(child);
			num_token = child.num_token;
			ok = true;
		}

		@Override
		int eval(int k, Environment e) {
			for (int i=0; i<k; i++) System.out.print(' ');
			System.out.println("Statement");

			AST child = children.get(0);
			return child.eval(k+1, e);
		}
	}
	
	
	static class Program extends ASTList {
		Program(TokenInput input) {
			AST s = new Statement(input.clone());
			if (!s.ok) return;
			children.add(s);
			num_token = s.num_token;

			while (input.offset + num_token + 1 < input.length) {
				Token nextToken = input.get(num_token);
				
				if (!(nextToken instanceof Token.Operator)) break;
				String op = ((Token.Operator)nextToken).getValue();
				if (!op.equals(";")) break;
				num_token++;
				
				AST right = new Statement(input.clone(num_token));
				if (!right.ok) continue;

				children.add(new ASTLeaf(nextToken));
				children.add(right);
				num_token += right.num_token;
			}
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			for (int i=0; i<k; i++) System.out.print(' ');
			System.out.println("Program");
			int ret = children.get(0).eval(k + 1, e);
			
			for (int i=1; i<children.size(); i++) {
				if (children.get(i) instanceof Statement) ret = children.get(i).eval(k+1, e);
				else {
					for (int j=0; j<k; j++) System.out.print(' ');
					System.out.println(((ASTLeaf)(children.get(i))).child.getValue().toString());
				}
			}
			return ret;
		}
	}

	
	static class Assign extends ASTList {
		Assign(TokenInput input) {
			if (input.offset + 2 >= input.length) return;
			
			Token nextToken = input.get();
			if (!(nextToken instanceof Token.Name)) return;
			AST left = new Variable((String)nextToken.getValue());
			
			if (!(input.getNext() instanceof Token.Operator)) return;
			if (!(input.getNext().getValue().equals("="))) return;
			
			AST right = new Statement(input.clone(2));
			if (!right.ok) return;
			
			children.add(left);
			children.add(new ASTLeaf(input.getNext()));
			children.add(right);

			num_token += 2 + right.num_token;
			
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			for(int i=0; i<k; i++) System.out.print(' ');
			System.out.println("Assign");
			int ret = ((Statement)children.get(2)).eval(k+1, e);
			e.hashMap.put(((Variable)children.get(0)).name, ret);
			return ret;
		}
	}
	
	
	static class Condition extends ASTList {
		Condition(TokenInput input) {
			if (input.offset + 2 >= input.length) return;
			AST left = new Sum(input.clone());
			if (!left.ok) return;
			children.add(left);
			num_token += left.num_token;
			
			if (input.offset + num_token + 1 >= input.length) return;
			Token operator = input.get(num_token);
			if (!(operator instanceof Token.Operator)) return;
			String op = (String)(((Token.Operator)operator).getValue());
			if (!( op.equals("==") || op.equals(">") || op.equals("<")) ) return;
			children.add(new ASTLeaf(operator));
			num_token++;
			
			AST right = new Sum(input.clone(num_token));
			if (!right.ok) return;
			children.add(right);
			num_token += right.num_token;
			
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			for (int i=0; i<k; i++) System.out.print(' ');
			System.out.print("Condition");
			String op = (String) (((ASTLeaf) children.get(1)).child.getValue());
			System.out.println(" : " + op);
			
			int left = children.get(0).eval(k+1, e);
			int right = children.get(2).eval(k+1, e);
			
			int ret = 0;
			if		(op.equals(">"))  ret = left > right?  1:0;
			else if (op.equals("==")) ret = left == right? 1:0;
			else if (op.equals("<"))  ret = left < right?  1:0;
			
			return ret;
		}
	}


	static class While extends ASTList {
		While(TokenInput input) {
			if (input.offset + 7 >= input.length) return;
			
			Token nextToken = input.get();
			if (!(input.get() instanceof Token.Name)) return;
			if (!((String) (((Token.Name) input.get()).getValue())).equals("while")) return;
			num_token++;

			nextToken = input.get(num_token);
			if (!(nextToken instanceof Token.Operator)) return;
			if (!((String) (((Token.Operator) nextToken)).getValue()).equals("(")) return;
			num_token++;
			
			AST condition = new Condition(input.clone(num_token));
			if (!condition.ok) return;
			num_token += condition.num_token;
			
			nextToken = input.get(num_token);
			if (!(nextToken instanceof Token.Operator)) return;
			if (!((String) (((Token.Operator) nextToken)).getValue()).equals(")")) return;
			num_token++;
			
			nextToken = input.get(num_token);
			if (!(nextToken instanceof Token.Operator)) return;
			if (!((String) (((Token.Operator) nextToken)).getValue()).equals("{")) return;
			num_token++;
			
			AST program = new Program(input.clone(num_token));
			if (!program.ok) return;
			num_token += program.num_token;
			
			nextToken = input.get(num_token);
			if (!(nextToken instanceof Token.Operator)) return;
			if (!((String) (((Token.Operator) nextToken)).getValue()).equals("}")) return;
			num_token++;
			
			children.add(condition);
			children.add(program);

			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			for (int i=0; i<k; i++) System.out.print(' ');
			System.out.println("While");
			
			int ret = 0;
			AST condition = children.get(0);
			AST program = children.get(1);
			
			while (condition.eval(k+1, e) == 1) {
				ret = program.eval(k+1, e);
			}
			return ret;
		}
	}
	
	
	static class If extends ASTList {
		If(TokenInput input) {
			if (input.offset + 7 >= input.length) return;
			
			Token nextToken = input.get();
			if (!(input.get() instanceof Token.Name)) return;
			if (!((String) (((Token.Name) input.get()).getValue())).equals("if")) return;
			num_token++;

			nextToken = input.get(num_token);
			if (!(nextToken instanceof Token.Operator)) return;
			if (!((String) (((Token.Operator) nextToken)).getValue()).equals("(")) return;
			num_token++;
			
			AST condition = new Condition(input.clone(num_token));
			if (!condition.ok) return;
			num_token += condition.num_token;
			
			nextToken = input.get(num_token);
			if (!(nextToken instanceof Token.Operator)) return;
			if (!((String) (((Token.Operator) nextToken)).getValue()).equals(")")) return;
			num_token++;
			
			nextToken = input.get(num_token);
			if (!(nextToken instanceof Token.Operator)) return;
			if (!((String) (((Token.Operator) nextToken)).getValue()).equals("{")) return;
			num_token++;
			
			AST program = new Program(input.clone(num_token));
			if (!program.ok) return;
			num_token += program.num_token;
			
			nextToken = input.get(num_token);
			if (!(nextToken instanceof Token.Operator)) return;
			if (!((String) (((Token.Operator) nextToken)).getValue()).equals("}")) return;
			num_token++;
			
			children.add(condition);
			children.add(program);

			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			for (int i=0; i<k; i++) System.out.print(' ');
			System.out.println("If");
			
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

import java.util.ArrayList;
import java.util.HashMap;

import lang.Extension;


public class Main {
	public static void main(String args[]) {
		String s = "a=(1+2)*3";
		Token[] ls = tokenizer(s);	// 字句解析
		TokenInput input = new TokenInput(ls);
		AST ast = new Statement(input);	// 構文解析
		System.out.println(s);
		System.out.println(ast.eval(0, new Environment()));
	}

	public static Token[] tokenizer(String s) {
		String[][] m = Extension.matchAll(s, "\\s*(([0-9]+)|([\\(\\)-+*/=;])|([a-zA-z]+))\\s*");
		Token[] ret = new Token[m.length];
		for (int i=0; i<m.length; i++) {
			if		(m[i][2] != null) ret[i] = new Token.Num(Integer.parseInt(m[i][2]));
			else if	(m[i][3] != null) ret[i] = new Token.Operator(m[i][3]);
			else if (m[i][4] != null) ret[i] = new Token.Name(m[i][4]);
		}
		return ret;
	}

	
	static class TokenInput implements Cloneable {
		Token[] tokens;
		int offset = 0;
		int length;
		
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
				r = (TokenInput)super.clone();
				r.offset = offset;
				r.tokens = tokens;
				r.length = length;
			}
			catch (CloneNotSupportedException e) {
				throw new RuntimeException();
			}
			return r;
		}
		
		TokenInput clone(int skip) {
			TokenInput r;
			try {
				r = (TokenInput)super.clone();
				r.offset = skip + offset;
				r.tokens = tokens;
				r.length = length;
			}
			catch (CloneNotSupportedException e) {
				throw new RuntimeException();
			}
			return r;
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

	
	
	// Num ::= '(' Sum ')' | NumberToken | Variable
	static class Num extends ASTList {
		Num(TokenInput input) {
			if (input.get() instanceof Token.Operator) {
//				if(!((Token.Operator)ls[offset]).getValue().equals("(")) return;
				if(!((Token.Operator)input.get()).getValue().equals("(")) return;
//				AST s = new Sum(ls, offset + 1);
				AST s = new Sum(input.clone(1));
				if(!s.ok) return;
				if(input.offset + s.num_token + 1 >= input.length) return;
				if(!(input.get(s.num_token + 1) instanceof Token.Operator)) return;
				if(!((Token.Operator)input.get(s.num_token + 1)).getValue().equals(")")) return;
				num_token = 1 + s.num_token + 1;
				children.add(s);
			}

			else if (input.get() instanceof Token.Num) {
				children.add(new ASTLeaf(input.get()));
				num_token = 1;
			}
			
			else if (input.get() instanceof Token.Name) {
				String ident = (String)input.get().getValue();
				children.add(new Variable(ident));
				num_token = ident.length();
			}
			
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			for(int i=0; i<k; i++) System.out.print(" ");
			System.out.println("Num");
			AST firstChild = children.get(0);
			return firstChild instanceof Sum ?
					firstChild.eval(k + 1, e):
					((Token.Num)((ASTLeaf)firstChild).child).getValue();
		}
	}
	
	
	
	// Sum ::= Prod { [+-] Prod }
	static class Sum extends ASTList {
		Sum(TokenInput input) {
			AST left = new Prod(input.clone());
			if (!left.ok) return;
			children.add(left);
			num_token = left.num_token;
			
			while (input.offset + num_token + 1 < input.length) {
				Token nextToken = input.get(num_token);
				
				if (!(nextToken instanceof Token.Operator)) break;
				
				if (!(((Token.Operator) nextToken).getValue().equals("+")) &&
					!(((Token.Operator) nextToken).getValue().equals("-"))) break;
				
				AST right = new Prod(input.clone(num_token + 1));
				if(!right.ok) break;
				children.add(new ASTLeaf(nextToken));
				children.add(right);
				
				num_token += right.num_token + 1;
			}
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			for(int i=0; i<k; i++) System.out.print(' ');
			System.out.println("sum");
			int ret = children.get(0).eval(k + 1, e);
			for(int i=1; i<children.size(); i+=2) {
				for(int j=0; j<k; j++) System.out.print(' ');
				System.out.println( ((ASTLeaf)children.get(i)).child.toString() );
				
				int right = children.get(i + 1).eval(k + 1, e);
				Token.Operator operator = (Token.Operator) ((ASTLeaf)children.get(i)).child;
				if		(operator.getValue().equals("+")) ret += right;
				else if (operator.getValue().equals("-")) ret -= right;
			}
			return ret;
		}
	}
	
	
	// Prod ::= Num { [*/] Num }
	static class Prod extends ASTList {
		Prod(TokenInput input) {
			AST left = new Num(input.clone());
			if (!left.ok) return;
			children.add(left);
			num_token = left.num_token;
			
			while (input.offset + num_token + 1 < input.length) {

				Token nextToken = input.get(num_token);
				if (!(nextToken instanceof Token.Operator)) break;

				if (!(((Token.Operator)(nextToken)).getValue().equals("*")) &&
					!(((Token.Operator)(nextToken)).getValue().equals("/"))) break;
				
				AST right = new Num(input.clone(num_token + 1));
				if (!right.ok) break;
				
				children.add(new ASTLeaf(nextToken));
				children.add(right);
				
				num_token += right.num_token + 1;
			}
			ok = true;
		}
		
		@Override
		int eval(int k, Environment e) {
			for (int i=0; i<k; i++) System.out.print(' ');
			System.out.println("prod");
			int ret = children.get(0).eval(k + 1, e);
			
			for (int i=1; i<children.size(); i+=2) {
				for (int j=0; j<k; j++) System.out.print(' ');

				System.out.println( ((ASTLeaf)children.get(i)).child.toString() );

				int right = children.get(i + 1).eval(k + 1, e);

				Token.Operator operator = (Token.Operator) (((ASTLeaf)children.get(i)).child);
				if		(operator.getValue().equals("*")) ret *= right;
				else if (operator.getValue().equals("/")) ret /= right;
			}
			return ret;
		}
	}
	
	// Statement ::= Assign | Sum
	static class Statement extends ASTList {
		Statement(TokenInput input) {
			AST child = new Assign(input.clone());
			if (!child.ok) {
				child = new Sum(input.clone());
				if (!child.ok) return;
			}
			children.add(child);
			num_token = child.num_token;
			ok = true;
		}
	}
	
	// Assign ::= Variable '=' Num
	static class Assign extends ASTList {
		Assign(TokenInput input) {
			if (!(input.get() instanceof Token.Name)) return;
			AST left = new Variable((String)input.get().getValue());
			if (!left.ok) return;

			if (!(input.getNext() instanceof Token.Operator)) return;
			if (!(input.getNext().getValue().equals("="))) return;
			
			AST right = new Statement(input.clone(2));
			if (!right.ok) return;
			
			children.add(left);
			children.add(new ASTLeaf(input.getNext()));
			children.add(right);
			num_token += 3;
			
			ok = true;
		}
	}
	
	
	static class Variable extends ASTLeaf {
		String name;
		Variable(String name) {
			this.name = name;
			num_token = 1;
		}
		@Override
		int eval(int k, Environment e) {
			return e.hashMap.get(name);
		}
	}
	
	
	static class Environment { 
		HashMap<String, Integer> hashMap;
		Environment() {
			hashMap = new HashMap<String, Integer>();
		}
	}
}

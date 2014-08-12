import java.util.ArrayList;
import java.util.HashMap;

import lang.Extension;


public class Main {
	public static void main(String args[]) {
		String s = "(1+2)*2";
		Token[] ls = tokenizer(s);	// 字句解析
		AST ast = new Sum(ls, 0);	// 構文解析
		System.out.println(s);
		System.out.println(ast.eval(0));
	}

	public static Token[] tokenizer(String s) {
		String[][] m = Extension.matchAll(s, "\\s*(([0-9]+)|([\\(\\)-+*/]))\\s*");
		Token[] ret = new Token[m.length];
		for (int i=0; i<m.length; i++) {
			if		(m[i][2] != null) ret[i] = new Token.Num(Integer.parseInt(m[i][2]));
			else if	(m[i][3] != null) ret[i] = new Token.Operator(m[i][3]);
		}
		return ret;
	}

	
	
	
	// 抽象構文木
	static class AST {
		// その要素に含まれる合計のトークン数
		int num_token = 0;
		// 構文木の構築に成功したかどうか
		boolean ok = false;
		int eval(int k) {
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

	
	
	// Num ::= '('Sum')' | NumberToken
	static class Num extends ASTList {
		Num(Token[] ls, int offset) {
			if (ls[offset] instanceof Token.Operator) {
				if(!((Token.Operator)ls[offset]).getValue().equals("(")) return;
				AST s = new Sum(ls, offset + 1);
				if(!s.ok) return;
				if(offset + s.num_token + 1 >= ls.length) return;
				if(!(ls[offset + s.num_token + 1] instanceof Token.Operator)) return;
				if(!((Token.Operator)ls[offset + s.num_token + 1]).getValue().equals(")")) return;
				num_token = 1 + s.num_token + 1;
				children.add(s);
				ok = true;
			}

			else if(ls[offset] instanceof Token.Num) {
				children.add(new ASTLeaf(ls[offset]));
				num_token = 1;
				ok = true;
			}
		}
		@Override
		int eval(int k) {
			for(int i=0; i<k; i++) System.out.print(" ");
			System.out.println("Num");
			AST firstChild = children.get(0);
			return firstChild instanceof Sum ?
					firstChild.eval(k + 1):
					((Token.Num)((ASTLeaf)firstChild).child).getValue();
		}
	}
	
	
	
	// Sum ::= Prod { [+-] Prod }
	static class Sum extends ASTList {
		Sum(Token[] ls, int offset) {
			AST left = new Prod(ls, offset);
			if (!left.ok) return;
			children.add(left);
			num_token = left.num_token;
			
			while (offset + num_token + 1 < ls.length) {
				Token nextToken = ls[offset + num_token];
				
				if (!(nextToken instanceof Token.Operator)) break;
				
				if (!(((Token.Operator) nextToken).getValue().equals("+")) &&
					!(((Token.Operator) nextToken).getValue().equals("-"))) break;
				
				AST right = new Prod(ls, offset + num_token + 1);
				if(!right.ok) break;
				children.add(new ASTLeaf(nextToken));
				children.add(right);
				
				num_token += right.num_token + 1;
			}
			ok = true;
		}
		
		@Override
		int eval(int k) {
			for(int i=0; i<k; i++) System.out.print(' ');
			System.out.println("sum");
			int ret = children.get(0).eval(k + 1);
			for(int i=1; i<children.size(); i+=2) {
				for(int j=0; j<k; j++) System.out.print(' ');
				System.out.println( ((ASTLeaf)children.get(i)).child.toString() );
				
				int right = children.get(i + 1).eval(k + 1);
				Token.Operator operator = (Token.Operator) ((ASTLeaf)children.get(i)).child;
				if		(operator.getValue().equals("+")) ret += right;
				else if (operator.getValue().equals("-")) ret -= right;
			}
			return ret;
		}
	}
	
	
	// Prod ::= Num { [*/] Num }
	static class Prod extends ASTList {
		Prod(Token[] ls, int offset) {
			AST left = new Num(ls, offset);
			if (!left.ok) return;
			children.add(left);
			num_token = left.num_token;
			
			while (offset + num_token + 1 < ls.length) {

				Token nextToken = ls[offset + num_token];
				if (!(nextToken instanceof Token.Operator)) break;

				if (!(((Token.Operator)(nextToken)).getValue().equals("*")) &&
					!(((Token.Operator)(nextToken)).getValue().equals("/"))) break;
				
				AST right = new Num(ls, offset + num_token + 1);
				
				if (!right.ok) break;
				children.add(new ASTLeaf(nextToken));
				children.add(right);
				
				num_token += right.num_token + 1;
			}
			ok = true;
		}
		
		@Override
		int eval(int k) {
			for(int i=0; i<k; i++) System.out.print(' ');
			System.out.println("prod");
			int ret = children.get(0).eval(k + 1);
			
			for(int i=1; i<children.size(); i+=2) {
				for(int j=0; j<k; j++) System.out.print(' ');

				System.out.println( ((ASTLeaf)children.get(i)).child.toString() );

				int right = children.get(i + 1).eval(k + 1);

				Token.Operator operator = (Token.Operator) (((ASTLeaf)children.get(i)).child);
				if		(operator.getValue().equals("*")) ret *= right;
				else if (operator.getValue().equals("/")) ret /= right;
			}
			return ret;
		}
	}
	
	static class Environment { 
		HashMap<String, Integer> hashMap;
		Environment() {
			hashMap = new HashMap<String, Integer>();
		}
	}
	
	static class Variable extends ASTLeaf {
		String name;
		Variable(String name) {
			this.name = name;
			num_token = 1;
		}
		
		int eval(int k, Environment e) {
			return e.hashMap.get(name);
		}
	}
}

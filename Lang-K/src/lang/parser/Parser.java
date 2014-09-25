package lang.parser;

import lang.debug.Debug;
import lang.parser.operator.BinaryOperators;
import lang.parser.operator.IBinaryOperator;
import lang.token.Token;
import lang.token.TokenSet;

import java.util.ArrayList;

public class Parser implements IParser {
    // 構文解析
    public AST parse(TokenSet ls) {
        AST ast = new Program(ls);
        Debug.out.println( ast.succeed ?
                "--SUCCEED PARSING--":
                "ERROR: Parsing failed!" );
		return ast;
	}
}

abstract class ASTList extends AST {
	ArrayList<AST> children = new ArrayList<>();
}

abstract class ASTLeaf extends AST {
    @Override
    public int eval(int k, Environment e) {
        return 0;
    }
}

final class Operator extends ASTLeaf {
	String string;
	Operator(Token t) {
		string = t.string;
	}

    @Override
    public int eval(int k, Environment e) {
        return 0;
    }
}

class Variable extends ASTLeaf {
	String name;
	Variable(String name) {
		this.name = name;
	}

	@Override
	public int eval(int k, Environment e) {
		Integer v = e.hashMap.get(name);
		int value = (v == null)? 0: v;
		Debug.log(k, "Variable : " + name + '(' + value + ')');
		return value;
	}
}
	
class Literal extends ASTLeaf {
	int value;
	Literal(TokenSet ls) {
		Token t = ls.next();
		value = Integer.parseInt(t.string);
	}

	@Override
	public int eval(int k, Environment e) {
		Debug.log(k, "Literal: " + value);
		return value;
	}
}

class Expr extends ASTList {
	ArrayList<IBinaryOperator> operators = new ArrayList<>();
	
	Expr(TokenSet ls) {
		Value n = new Value(ls);
		if (!n.succeed) return;
		children.add(n);
		
		while (ls.isOperator()) {
			String opstr = ls.next().string;
			
            IBinaryOperator binaryOperator = null;
//            binaryOperator = IntegerBinaryOperatorHashMap.map.get(opstr);
			for (IBinaryOperator b: BinaryOperators.values()) {
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
			
			Value n2 = new Value(ls);
			if (!n2.succeed) return;
			children.add(n2);
		}
		succeed = true;
	}
	
	@Override
	public int eval(int k, Environment e) {
		Debug.log(k, "Expr");
		ArrayList<Object> vals = new ArrayList<>();
		
		int count = 0;
		for (AST v: children) {
			vals.add(v.eval(k + 1, e));
			if (count >= operators.size()) break;
			Debug.out.println(operators.get(count).getSign());
			count++;
		}
		
		ArrayList<IBinaryOperator> ops_cpy = new ArrayList<>(operators);
		ArrayList<IBinaryOperator> ops_cpy2 = new ArrayList<>();
		ArrayList<Object> vals_ = new ArrayList<>();
		
		for (int il=0; il<BinaryOperators.maxLevel; il++) {
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
			ops_cpy = new ArrayList<>(ops_cpy2);
			vals = new ArrayList<>(vals_);
		}
		return (Integer) vals.get(0);
	}
}

class Value extends ASTList {
	Value(TokenSet ls) {
		Debug.out.println("num");
		// ( Expression )
		if (ls.isSymbol()) {
			Debug.out.println("( expression )");
			if (!ls.read("(")) return;
			AST s = new Expr(ls);
			if (!s.succeed) return;
			if (!ls.read(")")) return;
			children.add(s);
		}

		// Literal
		else if (ls.isNumber()) {
			Debug.out.println("literal");
			children.add(new Literal(ls));
		}
		
		// Variable
		else if (ls.isName()) {
			Debug.out.println("variable");
			String ident = ls.next().string;
			Variable v = new Variable(ident);
			children.add(v);
		}
		else return;
		
		succeed = true;
	}
	
	@Override
	public int eval(int k, Environment e) {
		Debug.log(k, "Num");
		AST child = children.get(0);
		return child.eval(k+1, e);
	}
}


class Statement extends ASTList {
	Statement(TokenSet ls) {
		AST child = 
		  ls.is("while")?	new While(ls)
		: ls.is("if")?		new If(ls)
		: ls.is("print")?	new Print(ls)
		: ls.isName()?		new Assign(ls)
		: 					new Expr(ls)
		;
		children.add(child);
		succeed = true;
	}

	@Override
	public int eval(int k, Environment e) {
		Debug.log(k, "Statement");
		AST child = children.get(0);
		return child.eval(k+1, e);
	}
}


class Program extends ASTList {
	Program(TokenSet ls) {
		Debug.out.println("program");
        AST s = new Statement(ls);
		if (!s.succeed) return;
		children.add(s);

		while (true) {
			if (ls.isEOF()) break;
			if (!ls.isMatch(",")) break;
			Token operator = ls.next();
			
			AST right = new Statement(ls);
			if (!right.succeed) continue;

			children.add(new Operator(operator));
			children.add(right);
		}
		succeed = true;
	}
	
	@Override
	public int eval(int k, Environment e) {
		Debug.log(k, "Program");
		int ret = children.get(0).eval(k+1, e);
		
		for (int i=1; i<children.size(); i++) {
			if (children.get(i) instanceof Statement)
                ret = children.get(i).eval(k+1, e);
			else {
				Debug.log(k, ((Operator)children.get(i)).string);
			}
		}
		return ret;
	}
}

class Print extends ASTList {
    Print(TokenSet ls) {
        Debug.out.println("print");
        ls.read("print");
        AST ast = new Value(ls);
        if (!ast.succeed) return;
        children.add(ast);
    }
    @Override
    public int eval(int k, Environment e) {
        Debug.out.println(children.get(0).eval(k+1, e));
        return 0;
    }
}

class Assign extends ASTList {
	Assign(TokenSet ls) {
		Debug.out.println("assign");
		if (ls.isEOF()) return;
		AST left = new Variable(ls.next().string);
		
		if (ls.isEOF()) return;
		Token operator = ls.next();
		if (!"=".equals(operator.string)) return;
		
		AST right = new Statement(ls);
		if (!right.succeed) return;
		
		children.add(left);
		children.add(new Operator(operator));
		children.add(right);

		succeed = true;
	}
	
	@Override
	public int eval(int k, Environment e) {
		Debug.log(k, "Assign");
		int ret = children.get(2).eval(k + 1, e);
		// 代入
		String identifier = ((Variable)children.get(0)).name;
		e.hashMap.put(identifier, ret);
		return ret;
	}
}


class Condition extends ASTList {
	Condition(TokenSet ls) {
		Debug.out.println("condition");
		AST expr = new Expr(ls);
		if (!expr.succeed) return;
		children.add(expr);
		succeed = true;
	}

	@Override
	public int eval(int k, Environment e) {
		Debug.log(k, "Condition");
		return children.get(0).eval(k + 1, e);
	}
}


class While extends ASTList {
	While(TokenSet ls) {
		Debug.out.println("while");
		if (!ls.read("while", "(")) return;
		AST condition = new Condition(ls);
		if (!condition.succeed) return;
		if (!ls.read(")")) return;
		AST block = new Block(ls);

		children.add(condition);
		children.add(block);
		succeed = true;
	}
	
	@Override
	public int eval(int k, Environment e) {
		Debug.log(k, "While");
		
		AST condition = children.get(0);
		AST program = children.get(1);
		
		int ret = 0;
		while (condition.eval(k+1, e) == 1) {
			ret = program.eval(k+1, e);
		}
		return ret;
	}
}
class Block extends ASTList {
	Block(TokenSet ls) {
	    Debug.out.println("block");
		if (!ls.read(":")) return;
		AST program = new Program(ls);
		if (!ls.read(";")) return;
		children.add(program);
		succeed = true;
	}
	
	@Override
	public int eval(int k, Environment e) {
		Debug.log(k, "block");
		return children.get(0).eval(k + 1, e);
	}
}

class If extends ASTList {
	If(TokenSet ls) {
		Debug.out.println("if");
		if (!ls.read("if", "(")) return;
		
		AST condition = new Condition(ls);
		if (!condition.succeed) return;
		if (!ls.read(")")) return;
		AST block = new Block(ls);
		
		children.add(condition);
		children.add(block);
		succeed = true;
		}
	
	@Override
	public int eval(int k, Environment e) {
		Debug.log(k, "If");
		
		int ret = 0;
		AST condition = children.get(0);
		AST program = children.get(1);
		
		if (condition.eval(k+1, e) == 1) {
			ret = program.eval(k+1, e);
		}
		return ret;
	}
}

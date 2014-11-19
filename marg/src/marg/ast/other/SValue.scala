package marg.ast.other

import marg.ast.base.{ASTree, ASTList}
import marg.ast.leaf.{SIntLiteral, SBoolLiteral, SVariable}
import marg.exception.ParseException
import marg.lang.data.SType
import marg.parser.Env
import marg.token.TokenSet


class SValue extends ASTList {
  private var child: ASTree = null

  def this(ls: TokenSet) {
    this()
    if (ls.is("(")) {
      ls.read("(")
      val s: ASTree = new Expr(ls)
      if (!ls.read(")")) throw new ParseException("Syntax Error: ')' not found", ls)
      child = s
    }
    else if (ls.isNumber) {
      child = new SIntLiteral(ls)
    }
    else if (ls.isBool) {
      child = new SBoolLiteral(ls)
    }
    else if (ls.isName) {
      child = new SVariable(ls)
    }
    else throw new ParseException("Internal Error: invalid <Value>", ls)
  }

  def eval(e: Env): SType = child.eval(e)
}


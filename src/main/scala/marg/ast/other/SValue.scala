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
    child =
    if (ls.is("(")) {
      ls.read("(")
      val s: ASTree = new Expr(ls)
      if (!ls.read(")")) throw new ParseException("Syntax Error: ')' not found", ls)
      s
    }
    else if (ls.isNumber)  new SIntLiteral(ls)
    else if (ls.isBool) new SBoolLiteral(ls)
    else if (ls.isName) new SVariable(ls)
    else throw new ParseException("Internal Error: invalid <Value>", ls)
  }

  def eval(e: Env): SType = child.eval(e)
}


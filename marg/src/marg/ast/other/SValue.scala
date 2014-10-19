package marg.ast.other

import marg.exception.ParseException
import marg.lang.data.SType
import marg.parser.{SEnvironment, Environment}
import marg.ast.{SASTList, SASTree}
import marg.token.TokenSet


class SValue extends SASTList {
  private var child: SASTree = null

  private[other] def this(ls: TokenSet) {
    this()
    if (ls.is("(")) {
      ls.read("(")
      val s: SASTree = new SExpr(ls)
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
      val id: String = ls.next.string
      child = new SVariable(id)
    }
    else throw new ParseException("Internal Error: invalid <Value>", ls)
  }

  def eval(e: SEnvironment): SType = {
    return child.eval(e)
  }
}


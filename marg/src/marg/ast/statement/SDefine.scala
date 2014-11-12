package marg.ast.statement

import marg.ast.ASTree
import marg.ast.other.SExpr
import marg.exception.ParseException
import marg.lang.data.SType
import marg.parser.Env
import marg.token.TokenSet


class SDefine extends ASTree {
  var string = ""
  var child: ASTree = null

  def this(ls: TokenSet) {
    this()
    ls.read("var")
    string = ls.next.String
    if (!ls.read("="))
      throw new ParseException("""Syntax Error: '=' not found.""", ls)
    child = new SExpr(ls)
  }

  def eval(e: Env): SType = {
    e.put(string, child.eval(e))
    null
  }
}

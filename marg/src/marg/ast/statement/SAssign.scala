package marg.ast.statement

import marg.ast.ASTree
import marg.exception.{RuntimeError, ParseException}
import marg.lang.data.SType
import marg.parser.SEnvironment
import marg.ast.other.SExpr
import marg.ast.leaf.SVariable
import marg.token.TokenSet

class SAssign extends ASTree {
  private var variable: SVariable = null
  private var expr: SExpr = null

  def this(ls: TokenSet) {
    this()
    variable = new SVariable(ls)
    if (!ls.read("="))
      throw new ParseException("Syntax Error: invalid syntax.", ls)
    expr = new SExpr(ls)
  }

  def eval(e: SEnvironment): SType = {
    val key = variable.string
    val ret = expr.eval(e)

    if (!e.find(key))
      throw new RuntimeError("Assignment to the undefined variable.")
    e.put(key, ret)

    ret
  }
}


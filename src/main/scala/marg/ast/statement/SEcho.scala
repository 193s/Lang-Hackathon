package marg.ast.statement

import marg.ast.base.ASTree
import marg.ast.other.Expr
import marg.lang.data.SType
import marg.parser.Env
import marg.token.TokenSet

class SEcho extends ASTree {
  private var child: ASTree = null

  def this(ls: TokenSet) {
    this()
    ls.read("echo")
    child = new Expr(ls)
  }

  def eval(e: Env): SType = {
    val v = child.eval(e)
    println(v.get)
    null
  }
}

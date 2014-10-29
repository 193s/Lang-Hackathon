package marg.ast.statement

import marg.ast.ASTree
import marg.ast.other.SExpr
import marg.lang.data.SType
import marg.parser.SEnvironment
import marg.token.TokenSet

class SEcho extends ASTree {
  private var child: ASTree = null

  def this(ls: TokenSet) {
    this()
    ls.read("echo")
    child = new SExpr(ls)
  }

  def eval(e: SEnvironment): SType = {
    val v = child.eval(e)
    println(v.get)
    v
  }
}

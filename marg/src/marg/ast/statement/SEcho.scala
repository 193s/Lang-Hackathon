package marg.ast.statement

import marg.ast.SASTree
import marg.ast.other.SExpr
import marg.lang.data.SType
import marg.parser.{SEnvironment, Environment}
import marg.token.TokenSet

class SEcho extends SASTree {
  private var child: SASTree = null

  def this(ls: TokenSet) {
    this()
    ls.read("echo")
    child = new SExpr(ls)
  }

  def eval(e: SEnvironment): SType = child.eval(e)
}

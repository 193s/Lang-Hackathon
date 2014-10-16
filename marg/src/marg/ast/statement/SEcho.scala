package marg.ast.statement

import marg.ast.SASTree
import marg.lang.data.SType
import marg.parser.Environment
import marg.token.TokenSet

class SEcho extends SASTree {
  var child: SASTree

  override def build(ls: TokenSet): Unit = {
    ls.read("echo")
    child = new SExpr()
    child.build(ls)
  }

  override def eval(k: Int, e: Environment): SType = return child.eval(k, e)
}

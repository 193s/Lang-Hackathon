package marg.ast.statement

import marg.ast.base.ASTree
import marg.ast.other.Expr
import marg.parser.Env
import marg.token.TokenSet

class SEcho(child: ASTree) extends ASTree {
  def this(ls: TokenSet) =
    this ({
      ls.read("echo")
      new Expr(ls)
    })

  override def eval(e: Env) = {
    val v = child.eval(e)
    println(v.get)
    null
  }
}

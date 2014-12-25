package marg.ast.statement

import marg.ast.base.ASTree
import marg.ast.other.Expr
import marg.lang.data.SBool
import marg.parser.Env
import marg.token.TokenSet


class BoolExpr private(cond: Expr) extends ASTree {
  def this(ls: TokenSet) = this(new Expr(ls))

  override def eval(e: Env): SBool =
    cond.eval(e).asInstanceOf[SBool]
}

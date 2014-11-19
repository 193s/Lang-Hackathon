package marg.ast.statement

import marg.ast.base.ASTree
import marg.ast.other.Expr
import marg.lang.data.SBool
import marg.parser.Env
import marg.token.TokenSet

class SCondition extends ASTree {
  private var condition: ASTree = null

  def this(ls: TokenSet) {
    this()
    condition = new Expr(ls)
  }

  def eval(e: Env): SBool = {
    condition.eval(e).asInstanceOf[SBool]
  }
}

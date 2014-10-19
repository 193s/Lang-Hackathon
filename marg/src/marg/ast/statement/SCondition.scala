package marg.ast.statement

import marg.ast.SASTree
import marg.ast.other.SExpr
import marg.lang.data.SBool
import marg.parser.{SEnvironment, Environment}
import marg.token.TokenSet

class SCondition extends SASTree {
  private var condition: SASTree = null

  def this(ls: TokenSet) {
    this()
    condition = new SExpr(ls)
  }

  def eval(e: SEnvironment): SBool = {
    condition.eval(e).get.asInstanceOf[SBool]
  }
}

package marg.ast.statement

import marg.ast.ASTree
import marg.ast.other.SExpr
import marg.lang.data.SBool
import marg.parser.SEnvironment
import marg.token.TokenSet

class SCondition extends ASTree {
  private var condition: ASTree = null

  def this(ls: TokenSet) {
    this()
    condition = new SExpr(ls)
  }

  def eval(e: SEnvironment): SBool = {
    condition.eval(e).asInstanceOf[SBool]
  }
}

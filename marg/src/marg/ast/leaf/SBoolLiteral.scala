package marg.ast.leaf

import marg.ast.base.ASTLeaf
import marg.lang.data.SBool
import marg.parser.Env
import marg.token.TokenSet


class SBoolLiteral private(str: String) extends ASTLeaf {
  private var value: SBool = null

  def this(ls: TokenSet) {
    this(ls.next.String)
    value = new SBool("o" == str)
  }

  override def eval(e: Env) = value

  override var string: String = str
}

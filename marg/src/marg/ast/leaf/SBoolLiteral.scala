package marg.ast.leaf

import marg.ast.ASTLeaf
import marg.lang.data.{SBool, SType}
import marg.parser.Env
import marg.token.TokenSet


class SBoolLiteral extends ASTLeaf {
  var string = ""
  private var value: SBool = null

  def this(ls: TokenSet) {
    this()
    string = ls.next.String
    value = new SBool("o" == string)
  }

  override def eval(e: Env) = value.asInstanceOf[SType]
}

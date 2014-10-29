package marg.ast.leaf

import marg.ast.ASTLeaf
import marg.lang.data.{SBool, SType}
import marg.parser.SEnvironment
import marg.token.TokenSet


class SBoolLiteral extends ASTLeaf {
  var string: String = ""
  private var value: SBool = null

  def this(ls: TokenSet) {
    this()
    val t = ls.next.getString
    string = t
    value = new SBool("o" == string)
  }

  def eval(e: SEnvironment): SType = value.asInstanceOf[SType]
}

package marg.ast.leaf

import marg.ast.ASTLeaf
import marg.lang.data.{SInt, SType}
import marg.parser.SEnvironment
import marg.token.TokenSet


class SIntLiteral(var string: String) extends ASTLeaf {
  var value: SInt = null

  def this(ls: TokenSet) {
    this(ls.next.string)
    value = new SInt(Integer.parseInt(string))
  }

  def eval(e: SEnvironment): SType = value

}

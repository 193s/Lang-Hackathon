package marg.ast.leaf

import marg.ast.SASTLeaf
import marg.lang.data.{SInt, SType, IType, MInt}
import marg.parser.SEnvironment
import marg.token.{TokenSet, Token}


class SIntLiteral(var string: String) extends SASTLeaf {
  var value: SInt = null

  def this(ls: TokenSet) {
    this(ls.next.string)
    value = new SInt(Integer.parseInt(string))
  }

  def eval(e: SEnvironment): SType = value

}

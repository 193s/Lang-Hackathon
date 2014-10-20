package marg.ast.leaf

import marg.ast.SASTLeaf
import marg.debug.Debug
import marg.lang.data.{SBool, SType, IType, MBool}
import marg.parser.{SEnvironment, Environment}
import marg.token.{TokenSet, Token}


class SBoolLiteral extends SASTLeaf {
  var string: String = ""
  private var value: SBool = null

  def this(ls: TokenSet) {
    this()
    val t = ls.next.string
    string = t
    value = new SBool("o" == string)
  }

  def eval(e: SEnvironment): SType = value.asInstanceOf[SType]
}

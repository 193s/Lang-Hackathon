package marg.ast.leaf

import marg.ast.SASTLeaf
import marg.lang.data.SType
import marg.parser.SEnvironment
import marg.token.TokenSet

class SVariable(var string: String) extends SASTLeaf {
  def this(ls: TokenSet) {
    this(ls.next.string)
    ls.checkEOF()
  }
  def eval(e: SEnvironment): SType = e.get(string)
}

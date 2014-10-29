package marg.ast.leaf

import marg.ast.ASTLeaf
import marg.lang.data.SType
import marg.parser.SEnvironment
import marg.token.TokenSet

class SVariable(var string: String) extends ASTLeaf {
  def this(ls: TokenSet) {
    this(ls.next.getString)
    ls.checkEOF()
  }
  def eval(e: SEnvironment): SType = e.get(string)
}

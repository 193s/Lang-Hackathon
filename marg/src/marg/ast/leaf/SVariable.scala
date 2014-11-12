package marg.ast.leaf

import marg.ast.ASTLeaf
import marg.parser.Env
import marg.token.TokenSet

class SVariable private(var string: String) extends ASTLeaf {
  def this(ls: TokenSet) = this(ls.next.String)
  override def eval(e: Env) = e.get(string)
}

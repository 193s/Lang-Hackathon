package marg.ast.leaf

import marg.ast.base.ASTLeaf
import marg.exception.ParseException
import marg.lang.data.SInt
import marg.parser.Env
import marg.token.TokenSet


class SIntLiteral private(var string: String) extends ASTLeaf {
  var value: SInt = null

  def this(ls: TokenSet) = {
    this(ls.next.String)
    value = new SInt(try string.toInt catch {
      case e: NumberFormatException =>
        throw new ParseException(s"$string is too big.", ls)
    })
  }

  override def eval(e: Env) = value

}

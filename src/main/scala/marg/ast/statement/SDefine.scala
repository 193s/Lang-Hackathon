package marg.ast.statement

import marg.ast.base.ASTree
import marg.ast.other.Expr
import marg.exception.ParseException
import marg.lang.data.SType
import marg.parser.Env
import marg.token.TokenSet


class SDefine(id: String, child: ASTree) extends ASTree {

  def this(ls: TokenSet) =
    this ({
      ls.read("let")
      ls.next().String
    },
    {
      if (!ls.read("="))
        throw new ParseException( """Syntax Error: '=' not found.""", ls)
      new Expr(ls)
    })

  override def eval(e: Env): SType = {
    println(s"debug: $id, $child")
    e += (id -> child.eval(e))
    println(s"debug: ${e.map.mkString(", ")}")
    null
  }
}

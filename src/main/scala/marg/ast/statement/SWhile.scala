package marg.ast.statement

import marg.ast.base.ASTree
import marg.ast.other.SBlock
import marg.exception.ParseException
import marg.lang.data.SType
import marg.parser.Env
import marg.token.TokenSet


class SWhile(cond: BoolExpr, prog: ASTree) extends ASTree {

  def this(ls: TokenSet) =
    this ({
      if (!ls.read("while", "("))
        throw new ParseException("Syntax Error: invalid syntax", ls)
      new BoolExpr(ls)
    }, {
      if (!ls.read(")"))
        throw new ParseException("Syntax Error: invalid syntax", ls)

      new SBlock(ls)
    })

  override def eval(e: Env) = {
    var ret: SType = null
    while (cond.eval(e).g) ret = prog.eval(e)
    ret
  }
}

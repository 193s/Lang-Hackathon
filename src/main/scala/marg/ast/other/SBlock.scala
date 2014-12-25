package marg.ast.other

import marg.ast.base.ASTree
import marg.exception.ParseException
import marg.lang.data.SType
import marg.parser.Env
import marg.token.TokenSet

class SBlock private(child: ASTree) extends ASTree {
  def this(ls: TokenSet) =
    this({
      if (!ls.read("{")) throw new ParseException("Syntax Error", ls)
      val program = new SProgram(ls)
      if (!ls.read("}")) throw new ParseException("Syntax Error", ls)
      program
    })

  override def eval(e: Env): SType = child.eval(e)
}

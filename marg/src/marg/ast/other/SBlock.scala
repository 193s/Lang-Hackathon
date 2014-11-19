package marg.ast.other

import marg.ast.base.ASTree
import marg.exception.ParseException
import marg.lang.data.SType
import marg.parser.Env
import marg.token.TokenSet

class SBlock extends ASTree {
  private var child: ASTree = null
  def this(ls: TokenSet) {
    this()
    // FIXME
    if (!ls.read(":")) throw new ParseException("Syntax Error", ls)
    val program: ASTree = new SProgram(ls)
    if (!ls.read(";")) throw new ParseException("Syntax Error", ls)
    child = program
  }

  def eval(e: Env): SType = child.eval(e)
}


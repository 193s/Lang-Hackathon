package marg.ast.other

import marg.debug.Debug
import marg.exception.ParseException
import marg.lang.data.SType
import marg.ast.{SASTree, SASTList}
import marg.parser.SEnvironment
import marg.token.TokenSet

class SBlock extends SASTList {
  private var child: SASTree = null
  def this(ls: TokenSet) {
    this()
    Debug.log("block")
    // FIXME
    if (!ls.read(":")) throw new ParseException("Syntax Error", ls)
    val program: SASTree = new SProgram(ls)
    if (!ls.read(";")) throw new ParseException("Syntax Error", ls)
    child = program
  }

  def eval(e: SEnvironment): SType = {
    return children.head.eval(e)
  }
}


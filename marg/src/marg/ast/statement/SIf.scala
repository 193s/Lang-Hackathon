package marg.ast.statement

import marg.exception.ParseException
import marg.ast.SASTree
import marg.lang.data.SType
import marg.parser.SEnvironment
import marg.ast.other.SBlock
import marg.token.TokenSet


class SIf extends SASTree {
  private var condition: SCondition = null
  private var program: SASTree = null

  def this(ls: TokenSet) {
    this()
    if (!ls.read("if", "(")) throw new ParseException("Syntax Error: invalid syntax", ls)
    condition = new SCondition(ls)
    if (!ls.read(")")) throw new ParseException("Syntax Error: invalid syntax", ls)
    program = new SBlock(ls)
  }

  def eval(e: SEnvironment): SType = {
    val cond: Boolean = condition.eval(e).get.asInstanceOf[Boolean]
    if (cond) program.eval(e)
    return null
  }
}


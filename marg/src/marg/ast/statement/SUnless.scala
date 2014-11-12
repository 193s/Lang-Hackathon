package marg.ast.statement

import marg.ast.ASTree
import marg.ast.other.SBlock
import marg.exception.ParseException
import marg.lang.data.SType
import marg.parser.Env
import marg.token.TokenSet


class SUnless extends ASTree {
  private var condition: SCondition = null
  private var program: ASTree = null

  def this(ls: TokenSet) {
    this()
    if (!ls.read("unless", "("))
      throw new ParseException("Syntax Error: invalid syntax", ls)
    condition = new SCondition(ls)
    if (!ls.read(")"))
      throw new ParseException("Syntax Error: invalid syntax", ls)
    program = new SBlock(ls)
  }

  def eval(e: Env): SType = {
    val cond = condition.eval(e).g()
    if (!cond) program.eval(e)
    else null
  }
}


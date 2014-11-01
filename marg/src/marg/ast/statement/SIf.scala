package marg.ast.statement

import marg.exception.ParseException
import marg.ast.ASTree
import marg.lang.data.SType
import marg.parser.SEnvironment
import marg.ast.other.SBlock
import marg.token.TokenSet


class SIf extends ASTree {
  private var condition: SCondition = null
  private var program: ASTree = null

  def this(ls: TokenSet) {
    this()
    if (!ls.read("if", "("))
      throw new ParseException("Syntax Error: invalid syntax", ls)
    condition = new SCondition(ls)
    if (!ls.read(")", ":"))
      throw new ParseException("Syntax Error: invalid syntax", ls)

    if (ls.isEOL) ls.next
    program = new SBlock(ls)

    if (ls.isEOL) ls.next
    if (!ls.read(";"))
      throw new ParseException("Syntax Error: invalid syntax", ls)
  }

  def eval(e: SEnvironment): SType = {
    val cond = condition.eval(e).g()
    if (cond) program.eval(e)
    else null
  }
}


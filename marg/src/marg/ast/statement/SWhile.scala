package marg.ast.statement

import marg.ast.ASTree
import marg.ast.other.{SStatement, SProgram}
import marg.exception.ParseException
import marg.lang.data.SType
import marg.parser.Env
import marg.token.TokenSet


class SWhile extends ASTree {
  private var condition: SCondition = null
  private var program: ASTree = null

  def this(ls: TokenSet) {
    this()
    if (!ls.read("while", "("))
      throw new ParseException("Syntax Error: invalid syntax", ls)

    condition = new SCondition(ls)

    if (!ls.read(")", ":"))
      throw new ParseException("Syntax Error: invalid syntax", ls)

    program = new SStatement(ls)

    if (!ls.read(";"))
      throw new ParseException("Syntax Error: invalid syntax", ls)
  }

  def eval(e: Env): SType = {
    var ret: SType = null
    while (condition.eval(e).g()) {
      ret = program.eval(e)
    }
    return ret
  }
}

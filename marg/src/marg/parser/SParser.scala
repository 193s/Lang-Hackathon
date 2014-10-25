package marg.parser

import marg.ast.ASTree
import marg.ast.other.SProgram
import marg.token.TokenSet


class SParser extends IParser {

  def parse(ls: TokenSet): ASTree = {
    val ast = new SProgram(ls)
    return ast
  }
}

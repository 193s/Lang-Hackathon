package marg.parser

import marg.ast.ASTree
import marg.ast.other.Program
import marg.token.TokenSet


class SParser {
  def parse(ls: TokenSet): ASTree = {
    val ast = new Program(ls)
    return ast
  }
}

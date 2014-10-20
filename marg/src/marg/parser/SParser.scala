package marg.parser

import marg.ast.{SASTree, ASTree}
import marg.ast.other.{SProgram, Program}
import marg.token.TokenSet


class SParser {
  def parse(ls: TokenSet): SASTree = {
    val ast = new SProgram(ls)
    return ast
  }
}

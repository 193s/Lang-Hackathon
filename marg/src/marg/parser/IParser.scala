package marg.parser

import marg.ast.ASTree
import marg.token.TokenSet

trait IParser {
  def parse(ls: TokenSet): ASTree
}

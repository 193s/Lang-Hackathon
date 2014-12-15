package marg.parser

import marg.ast.base.ASTree
import marg.token.{Token, TokenSet}

trait IParser {
  def parse(ls: List[Token]): ASTree
}

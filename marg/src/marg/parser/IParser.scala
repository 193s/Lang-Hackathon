package marg.parser

import marg.ast.ASTree
import marg.token.{Token, TokenSet}

trait IParser {
  def parse(ls: List[Token]): ASTree
}

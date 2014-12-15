package marg.parser

import marg.ast.base.ASTree
import marg.ast.other.SProgram
import marg.token.{Token, TokenSet}


class SParser extends IParser {

  def parse(ls: List[Token]): ASTree = new SProgram(new TokenSet(ls))
}

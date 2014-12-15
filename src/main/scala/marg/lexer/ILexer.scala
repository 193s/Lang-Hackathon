package marg.lexer

import marg.token.Token

trait ILexer {
  def tokenize(input: String): List[Token]
}

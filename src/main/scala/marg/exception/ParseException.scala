package marg.exception

import marg.token.TokenSet


class ParseException(message: String, ls: TokenSet) extends Exception {
  override def getMessage = message
  def getErrorOffset = ls.Offset
}

package marg.token

class Token private(string: String, kind: TokenKind) {
  require(string != null)

  override def toString = string + " : " + kind.toString
  def isEOF = kind eq TokenKind.EOF
  def String = string
  def Kind = kind
}

object Token {
  def apply(string: String, kind: TokenKind) = new Token(string, kind)
  def apply(char: Char, kind: TokenKind) = new Token(char.toString, kind)

  val EOF = Token("", TokenKind.EOF)
}


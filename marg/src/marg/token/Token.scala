package marg.token


class Token private(string: String, kind: TokenKind) {

  override def toString = string.reverse.padTo(10, ' ').reverse.mkString + " : " + kind.toString
  def isEOF = kind eq TokenKind.EOF
  def String = string
  def Kind = kind
}

object Token {
  def apply(string: String, kind: TokenKind) = {
    require(string != null)
    new Token(string, kind)
  }
  def apply(char: Char, kind: TokenKind): Token = this(char.toString, kind)

  val EOF = Token("", TokenKind.EOF)
}


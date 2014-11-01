package marg.token


class Token(string: String, kind: TokenKind) {
  def this(char: Char, kind: TokenKind) {
    this(char.toString, kind)
  }

  override def toString = string + "\t: " + kind
  def isEOF = kind eq TokenKind.EOF
  def String = string
  def Kind = kind
}

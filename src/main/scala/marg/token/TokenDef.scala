package marg.token


object TokenDef {
  val operators = Seq ('!', '?', '%', '&', '*', '+', '-', ',', '.', '/', ':', '<', '>', '=', '@', '^', '`', '|', '~')
  val reserved = Seq ("return", "if", "unless", "else", "while", "print")
  val symbols = Seq ('[', ']', '(', ')', '{', '}', '\'', '\"', ',', ':', ';')
}

package marg.token


object TokenDefinition {
  val operators = Seq('!', '?', '%', '&', '*', '+', '-', ',', '.', '/', ':', '<', '>', '=', '@', '^', '`', '|', '~')
  val reserved = Seq("return", "if", "unless", "else", "while", "print")
  val symbols = Seq('[', ']', '(', ')', '{', '}', '\'', '\"', ',', ':', ';')
}

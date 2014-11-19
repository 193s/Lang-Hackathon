package marg.lexer

import marg.token.TokenKind._
import marg.token._

class SLexer extends ILexer {

  override def tokenize(input: String) = {
    var ret = List[Token]()

    var offset = 0

    while (offset < input.length) {
      val t = getToken(input, offset)
      ret = ret :+ t
      offset += t.String.length
    }
    ret = ret.filter(_.Kind ne Space).filter(_.Kind ne Comment)

    ret match {
      case Nil => throw new NullPointerException
      case l => l :+ Token.EOF
    }
  }



  private def getToken(str: String, offset: Int): Token = str(offset) match {
    case '#' =>
      // Block Comment
      if (str.substring(offset + 1).startsWith("--")) {
        var comment = "#--"
        while (true) {
          val sr = take(str, offset + comment.length(), 3)
          comment += sr.head
          if (sr == "--#" || sr.isEmpty)
            return Token(comment + "--#", Comment)
        }
        sys error "error: Unclosed block comment."
      }
      // Inline Comment
      else Token('#' + str.substring(offset + 1).takeWhile(_ != '\n'), Comment)

    case '\n' => Token('\n', EOL)

    case c if c.isWhitespace => Token(c, Space)
    case c if isSymbol(c) => Token(c, Symbol)

    case c if isOperatorSign(c) =>
      val id = c + takeWhileOpSign(str, offset + 1)
      Token(id, Operator)

    case c if c.isDigit =>
      val id = c + takeWhileIdent(str, offset + 1)
      Token(id, IntLiteral)

    case c if c.isLetter =>
      val id = c + takeWhileIdent(str, offset + 1)
      if (id == "o" | id == "x") Token(id, BoolLiteral)
      else if (isReserved(id)) Token(id, Reserved)
      else Token(id, Identifier)

    case c => sys error s"Undefined token : $c"
  }

  private def takeWhileOpSign(str: String, offset: Int) =
    takeWhile(str, offset, TokenDef.operators)

  private def takeWhileIdent(str: String, offset: Int) =
    str.substring(offset) takeWhile(c => c.isLetterOrDigit)

  private def takeWhile(str: String, offset: Int, chars: Seq[Char]) =
    str.substring(offset) takeWhile(chars.contains(_))

  private def take(str: String, offset: Int, num: Int) = str.slice(offset, offset + num)

  private def isOperatorSign(c: Char) = TokenDef.operators.contains(c)

  private def isReserved(str: String) = TokenDef.reserved.contains(str)

  private def isSymbol(c: Char) = TokenDef.symbols.contains(c)
}


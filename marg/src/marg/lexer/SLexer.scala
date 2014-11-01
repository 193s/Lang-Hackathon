package marg.lexer

import java.lang.Character._

import marg.token.TokenKind._
import marg.token._

class SLexer extends ILexer {

  def tokenize(input: String): List[Token] = {
    println(input)//FIXME
    var ret = List[Token]()

    var offset = 0
    var flag = true

    while (offset < input.length && flag) {
      val t = getToken(input, offset)
      if (t == null) flag = false
      else {
        val kind: TokenKind = t.Kind

        if ((kind ne Space) && (kind ne Comment))
          ret = ret :+ t
        offset += t.String.length
      }
    }

    if (ret.isEmpty) throw new NullPointerException
    ret = ret :+ new Token(null, EOF)

    return ret
  }


  private def getToken(str: String, offset: Int): Token = {
    val s: String = take(str, offset, 1)
    if (s.isEmpty) return null

    val c = s.head

    if (c == '#') {
      var comment = "#"
      val string = take(str, offset + 1, 2)
      if ("--" == string) {
        comment += "--"
        var i = 3
        while (true) {
          val sr = take(str, offset + i, 3)
          comment += take(sr, 0, 1)
          if (("--#" == sr) || sr.isEmpty)
            return new Token(comment + "--#", Comment)
          i += 1
        }
      }
      else {
        var i = 1
        while (true) {
          val sr = take(str, offset + i, 1)
          comment += sr
          if (("\n" == sr) || sr.isEmpty)
            return new Token(comment, Comment)
          i += 1
        }
      }
    }

    if (c == '\n') return new Token(c, EOL)
    if (isWhitespace(c)) return new Token(c, Space)

    if (isSymbol(c)) return new Token(c, Symbol)

    if (isOperatorSign(c)) {
      val ident = c + takeWhileOperatorSign(str, offset + 1)
      return new Token(ident, Operator)
    }

    if (isDigit(c)) {
      val ident = c + takeWhileIdent(str, offset + 1)
      return new Token(ident, IntLiteral)
    }

    if (isLetter(c)) {
      val ident = c + takeWhileIdent(str, offset + 1)
      if (("o" == ident) || ("x" == ident)) return new Token(ident, BoolLiteral)
      if (isReserved(ident)) return new Token(ident, Reserved)
      return new Token(ident, Identifier)
    }

    println("Undefined token : " + c)
    null
  }

  private def takeWhileOperatorSign(str: String, offset: Int): String =
    takeWhile(str, offset, TokenDefinition.operators)

  private def takeWhileIdent(str: String, offset: Int): String = {
    var string = ""
    var i = 0
    while (true) {
      val s = take(str, offset + i, 1)
      if (s.isEmpty) return string
      val c = s.head
      if (!isLetterOrDigit(c)) return string
      string += c
      i += 1
    }
    return string
  }

  private def takeWhile(str: String, offset: Int, chars: Seq[Char]): String = {
    var string = ""

    var i = 0
    while (true) {
      val s = take(str, offset + i, 1)
      if (s.isEmpty) return string

      val c = s.head
      if (!chars.contains(c)) return string
      string += c
      i += 1
    }

    return string
  }

  private def take(str: String, offset: Int, num: Int): String = {
    var s = ""

    var i = 0
    val string = str.substring(offset)
    while ((i < num) && (string.length > i)) {
      s += string.charAt(i)
      i += 1
    }

    return s
  }

  private def isOperatorSign(c: Char) =
    TokenDefinition.operators.contains(c)

  private def isReserved(str: String) =
    TokenDefinition.reserved.contains(str)

  private def isSymbol(c: Char) =
    TokenDefinition.symbols.contains(c)
}

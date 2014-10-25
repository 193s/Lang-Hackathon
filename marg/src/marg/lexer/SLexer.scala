package marg.lexer

import java.lang.Character._

import marg.token.TokenKind._
import marg.token._

class SLexer extends ILexer {

  def tokenize(input: String): List[Token] = {
    var ls = List[Token]()

    var offset = 0
    var flag = true

    while (offset < input.length && flag) {
      val t = getToken(input, offset)
      if (t == null) flag = false
      else {
        val kind: TokenKind = t.kind

        if ((kind ne Space) && (kind ne OneLineComment) && (kind ne MultiLineComment))
          ls =  ls :+ t
        offset += t.string.length
      }
    }

    if (ls.isEmpty) throw new NullPointerException
    ls = ls :+ new Token(null, EOF)

    return ls.toList
  }


  private def getToken(str: String, offset: Int): Token = {
    val s: String = take(str, offset, 1)
    if (s.isEmpty) return null
    val c: Char = s.charAt(0)
    if (c == '#') {
      var comment: String = "#"
      val sr: String = take(str, offset + 1, 2)
      if ("--" == sr) {
        comment += "--"
        var i = 3
        while (true) {
          val string: String = take(str, offset + i, 3)
          comment += take(string, 0, 1)
          if (("--#" == string) || string.isEmpty) return new Token(comment + "--#", MultiLineComment)
          i += 1
        }
      }
      else {
        var i = 1
        while (true) {
          val string: String = take(str, offset + i, 1)
          comment += string
          if (("\n" == string) || string.isEmpty) return new Token(comment, OneLineComment)
          i += 1
        }
      }
    }
    if (isWhitespace(c)) return new Token(c, Space)

    if (isSymbol(c)) return new Token(c, Symbol)

    if (isOperatorSign(c)) {
      val ident: String = c + takeWhileOperatorSign(str, offset + 1)
      return new Token(ident, Operator)
    }

    if (isDigit(c)) {
      val ident: String = c + takeWhileIdent(str, offset + 1)
      return new Token(ident, IntLiteral)
    }

    if (isLetter(c)) {
      val ident: String = c + takeWhileIdent(str, offset + 1)
      if (("o" == ident) || ("x" == ident)) return new Token(ident, BoolLiteral)
      if (isReserved(ident)) return new Token(ident, Reserved)
      return new Token(ident, Identifier)
    }

    Console.out.println("Undefined token : " + c)
    return null
  }

  private def takeWhileOperatorSign(str: String, offset: Int): String = {
    var string: String = ""
    var i = 0
    var flag = true
    while (flag) {
      val s: String = take(str, offset + i, 1)
      if (s.isEmpty) {
        flag = false
      }
      else {
        val c: Char = s.charAt(0)
        if (isOperatorSign(c)) string += c
        else flag = false
        i += 1
      }
    }
    return string
  }

  private def takeWhileIdent(str: String, offset: Int): String = {
    var string: String = ""
    var i = 0
    var flag = true
    while (flag) {
      val s: String = take(str, offset + i, 1)
      if (s.isEmpty) {
        flag = false
      }
      else {
        val c: Char = s.charAt(0)
        if (isLetterOrDigit(c)) string += c
        else flag = false
        i += 1
      }
    }
    return string
  }


  private def take(str: String, offset: Int, num: Int): String = {
    var s: String = ""
    var i = 0
    while ((i < num) && (str.length > offset + i)) {
      s += str.charAt(offset + i)
      i += 1
    }
    return s
  }

  private def isOperatorSign(c: Char): Boolean = {
    for (t <- OperatorSign.values if c == t.character) return true
    return false
  }

  private def isReserved(str: String): Boolean = {
    for (t <- ReservedKind.values if str == t.toString) return true
    return false
  }

  private def isSymbol(c: Char): Boolean = {
    for (t <- SymbolKind.values if c == t.character) return true
    return false
  }
}

package marg.token

import java.io.EOFException

import marg.exception.ParseException
import marg.token.TokenKind._

class TokenSet(ls: List[Token]) {

  def getOffset = offset

  def unget(): Unit =
    if (offset > 0) offset -= 1

  def unget(k: Int): Unit =
    if (offset >= k) offset -= k

  def next: Token = {
    checkEOF()
    offset += 1
    ls(offset - 1)
  }

  def get: Token = ls(offset)

  def is(s: String): Boolean = { s == get.String }

  def isEOF: Boolean = get.isEOF

  def checkEOF() = if (isEOF) throw new EOFException

  def read(args: String*): Boolean = {
    for (t <- args) {
      val n: Token = next
      if (t != n.String) return false
    }
    true
  }

  def readP(message: String, args: String*) {
    args.foreach(t => {
      checkEOF()
      if (next.String != t)
        throw new ParseException(s"Syntax Error: $message", this)
    })
  }


  def isEOL = !isEOF && (get.Kind eq EOL)

  def isName = !isEOF && (get.Kind eq Identifier)

  def isNumber = !isEOF && (get.Kind eq IntLiteral)

  def isBool = !isEOF && (get.Kind eq BoolLiteral)

  def isOperator = !isEOF && (get.Kind eq Operator)

  def isReserved = !isEOF && (get.Kind eq Reserved)

  private var offset = 0
}

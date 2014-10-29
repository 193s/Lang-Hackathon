package marg.token

import java.io.EOFException

import marg.exception.ParseException
import marg.token.TokenKind._

class TokenSet(ls: List[Token]) {

  def getOffset: Int = offset

  def unget(): Unit = {
    if (offset > 0) offset -= 1
  }

  def unget(k: Int): Unit = {
    if (offset >= k) offset -= k
  }

  def next: Token = {
    checkEOF()
    offset += 1
    ls(offset - 1)
  }

  def get: Token = ls(offset)

  def is(s: String): Boolean = { s == get.getString }

  def isEOF: Boolean = get.isEOF

  def checkEOF() = if (isEOF) throw new EOFException

  def read(args: String*): Boolean = {
    for (t <- args) {
      val n: Token = next
      if (t != n.getString) return false
    }
    true
  }

  def readP(args: String*) {
    for (t <- args) {
      if (isEOF) throw new EOFException
      val n: Token = next
      if (!(t == n.getString)) throw new ParseException("", this)
    }
  }

  def isName = !isEOF && (get.getKind eq Identifier)

  def isNumber = !isEOF && (get.getKind eq IntLiteral)

  def isBool = !isEOF && (get.getKind eq BoolLiteral)

  def isOperator = !isEOF && (get.getKind eq Operator)

  def isReserved = !isEOF && (get.getKind eq Reserved)

  private var offset = 0
}

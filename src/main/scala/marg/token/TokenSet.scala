package marg.token

import java.io.EOFException

import marg.exception.ParseException
import marg.token.TokenKind._

@Deprecated
class TokenSet(ls: List[Token]) extends Iterator[Token] {
  private var offset = 0

  override def next() = {
    checkEOF()
    offset += 1
    ls(offset - 1)
  }

  override def hasNext = !isEOF


  def Offset = offset

  def previous() = if (offset > 0) offset -= 1

  def previous(k: Int) = if (offset >= k) offset -= k


  def get = ls(offset)

  def is(s: String) = s == get.String

  def isEOF = get.isEOF

  def checkEOF() = if (isEOF) throw new EOFException

  def read(args: String*): Boolean = {
    for (t <- args) if (t != next().String) return false
    true
  }

  def readP(message: String, args: String*) =
    for (t <- args) {
      checkEOF()
      if (next().String != t)
        throw new ParseException("Syntax Error: " + message, this)
    }


  def isEOL = !isEOF && (get.Kind eq EOL)

  def isName = !isEOF && (get.Kind eq Identifier)

  def isNumber = !isEOF && (get.Kind eq IntLiteral)

  def isBool = !isEOF && (get.Kind eq BoolLiteral)

  def isOperator = !isEOF && (get.Kind eq Operator)

  def isReserved = !isEOF && (get.Kind eq Reserved)

}

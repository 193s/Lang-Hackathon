package marg.ast.other

import java.io.EOFException

import marg.ast.{ASTree, ASTList}
import marg.lang.data.SType
import marg.parser.SEnvironment
import marg.token.TokenSet


class SProgram extends ASTList {

  def this(ls: TokenSet) {
    this()

    children += new SStatement(ls)

    try {
      while (!ls.isEOF && ls.isEOL) {
        ls.next
        val right: ASTree = new SStatement(ls)
        children += right
      }
    }
    catch {
      case e: EOFException =>
    }
  }

  def eval(e: SEnvironment): SType = {
    var ret: SType = null
    children.foreach(ast => ret = ast.eval(e))
    ret
  }
}

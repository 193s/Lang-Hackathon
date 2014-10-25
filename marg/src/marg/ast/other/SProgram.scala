package marg.ast.other

import java.io.EOFException

import marg.ast.{ASTree, ASTList}
import marg.lang.data.SType
import marg.parser.SEnvironment
import marg.token.TokenSet


class SProgram extends ASTList {

  def this(ls: TokenSet) {
    this()

    val s = new SStatement(ls)

    children += s

    try {
      while (!ls.isEOF && ls.is(",")) {
        ls.read(",")
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
    return ret
  }
}

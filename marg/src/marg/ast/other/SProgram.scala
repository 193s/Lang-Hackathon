package marg.ast.other

import java.io.EOFException
import marg.ast.base.ASTList
import marg.parser.Env
import marg.token.TokenSet


class SProgram extends ASTList {

  def this(ls: TokenSet) {
    this()

    children = List(new Statement(ls))

    try {
      while (!ls.isEOF && (ls.isEOL || ls.is(";"))) {
        ls.next()
        children = children :+ new Statement(ls)
      }
    }
    catch {
      case e: EOFException =>
    }
  }

  override def eval(e: Env) = {
    for (ast <- children.init) ast.eval(e)
    children.last.eval(e)
  }
}

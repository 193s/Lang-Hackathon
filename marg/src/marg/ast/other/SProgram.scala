package marg.ast.other

import java.io.EOFException

import marg.ast.{ASTree, ASTList}
import marg.lang.data.SType
import marg.parser.SEnvironment
import marg.token.TokenSet


class SProgram extends ASTList {

  def this(ls: TokenSet) {
    this()

    children = List(new SStatement(ls))

    try {
      while (!ls.isEOF && (ls.isEOL || ls.is(";"))) {
        ls.next
        children = children :+ new SStatement(ls)
      }
    }
    catch {
      case e: EOFException =>
    }
  }

  override def eval(e: SEnvironment) = {
    children.init.foreach(ast => ast.eval(e))
    children.last.eval(e)
  }
}

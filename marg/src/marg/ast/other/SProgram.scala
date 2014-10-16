package marg.ast.other

import java.io.EOFException

import marg.ast.{SASTree, SASTList}
import marg.debug.Debug
import marg.lang.data.SType
import marg.parser.Environment
import marg.token.TokenSet

import scala.collection.mutable.ListBuffer


class SProgram extends SASTList {

  def build(ls: TokenSet): Unit = {
    Debug.log("program")


    val s = new SStatement
    s.build(ls)

    children += s

    try {
      while (!ls.isEOF && ls.is(",")) {
        ls.read(",")
        val right: SASTree = new SStatement
        right.build(ls)
        children += right
      }
    }
    catch {
      case e: EOFException => {}
    }
  }

  override var children: ListBuffer[SASTree] = _

  override def eval(k: Int, e: Environment): SType = ???
}

package marg.ast.other

import java.io.EOFException

import marg.ast.SASTree
import marg.ast.statement._
import marg.debug.Console
import marg.lang.data.SType
import marg.parser.Environment
import marg.token.TokenSet


class SStatement extends SASTree {
  val child: SASTree

  override def build(ls: TokenSet): Unit = {
    try {
      child = if (ls.is("while"))       new SWhile(ls)
              else if (ls.is("if"))     new SIf(ls)
              else if (ls.is("unless")) new SUnless(ls)
              else if (ls.is("echo"))   new SEcho(ls)
              else if (ls.isName)       new SAssign(ls)
              else new SExpr(ls)
      child.build(ls)
    }
    catch {
      case e: EOFException => {
        return
      }
      case e: Exception => {
        Console.out.println("Unexpected error.")
        e.printStackTrace(Console.out)
      }
    }
  }

  override def eval(k: Int, e: Environment): SType = child.eval(k, e)
}

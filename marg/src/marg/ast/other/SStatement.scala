package marg.ast.other

import java.io.EOFException

import marg.ast.SASTree
import marg.ast.statement._
import marg.debug.Console
import marg.lang.data.SType
import marg.parser.{SEnvironment, Environment}
import marg.token.TokenSet


class SStatement extends SASTree {
  private var child: SASTree = null

  def this(ls: TokenSet) {
    this()
    try {
      child = ls.get().string match {
//        case "while" => new SWhile(ls)
        case "if"    => new SIf(ls)
//        case "unless"=> new SUnless(ls)
        case "echo"  => new SEcho(ls)
        case _ => {
          if (ls.isName()) new SAssign(ls)
          else new SExpr(ls)
        }
      }
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

  def eval(e: SEnvironment): SType = child.eval(e)
}

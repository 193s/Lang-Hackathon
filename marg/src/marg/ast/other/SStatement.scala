package marg.ast.other

import java.io.EOFException

import marg.ast.ASTree
import marg.ast.statement._
import marg.lang.data.SType
import marg.parser.SEnvironment
import marg.token.TokenSet


class SStatement extends ASTree {
  private var child: ASTree = null

  def this(ls: TokenSet) {
    this()
    try {
      child = ls.get.string match {
        case "while"  => new SWhile(ls)
        case "if"     => new SIf(ls)
        case "unless" => new SUnless(ls)
        case "echo"   => new SEcho(ls)
        case _ =>
          if (ls.isName) new SAssign(ls)
          else new SExpr(ls)
      }
    }
    catch {
      case e: EOFException =>
    }
  }

  def eval(e: SEnvironment): SType = child.eval(e)
}

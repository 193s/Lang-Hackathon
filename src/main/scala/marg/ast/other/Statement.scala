package marg.ast.other

import java.io.EOFException
import marg.ast.base.ASTree
import marg.ast.statement._
import marg.parser.Env
import marg.token.TokenSet


class Statement extends ASTree {
  private var child: ASTree = null

  def this(ls: TokenSet) {
    this()
    try {
      child = ls.get.String match {
        case "var"    => new SDefine(ls)
        case "while"  => new SWhile(ls)
        case "if"     => new SIf(ls)
        case "unless" => new SUnless(ls)
        case "echo"   => new SEcho(ls)
        case _ =>
          if (ls.isName) new SAssign(ls)
          else new Expr(ls)
      }
    }
    catch {
      case e: EOFException =>
    }
  }

  override def eval(e: Env) = child.eval(e)
}

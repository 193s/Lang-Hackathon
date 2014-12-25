package marg.ast.other

import java.io.EOFException
import marg.ast.{SUnless, SIf}
import marg.ast.base.ASTree
import marg.ast.statement._
import marg.parser.Env
import marg.token.TokenSet


class Statement private(child: ASTree) extends ASTree {

  def this(ls: TokenSet) =
    this (
      try ls.get.String match {
        case "let"    => new SDefine(ls)
        case "while"  => new SWhile(ls)
        case "if"     => new SIf(ls)
        case "unless" => new SUnless(ls)
        case "echo"   => new SEcho(ls)
        case "{"      => new SBlock(ls)
        case _        => new Expr(ls)
      }
      catch {
        case e: EOFException => null
      }
    )

  override def eval(e: Env) = child.eval(e)
}

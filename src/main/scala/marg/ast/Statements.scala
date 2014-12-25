package marg.ast

import marg.ast.base.ASTree
import marg.ast.other.Statement
import marg.ast.statement.BoolExpr
import marg.exception.ParseException
import marg.parser.Env
import marg.token.TokenSet



class SIf private(cond: BoolExpr, then: ASTree) extends ASTree {

  def this(ls: TokenSet) =
    this(
    {
      if (!ls.read("if", "("))
        throw new ParseException("Syntax Error: invalid syntax", ls)
      new BoolExpr(ls)
    }, {
      if (!ls.read(")"))
        throw new ParseException("Syntax Error: invalid syntax", ls)

      if (ls.isEOL) ls.next()
      new Statement(ls)
    })

  override def eval(e: Env) =
    cond.eval(e).g match {
      case true => then.eval(e)
      case false => null
    }
}


class SUnless(cond: BoolExpr, prog: ASTree) extends ASTree {
  def this(ls: TokenSet) =
    this ({
      if (!ls.read("unless", "("))
        throw new ParseException("Syntax Error: invalid syntax", ls)
      new BoolExpr(ls)
    },
    {
      if (!ls.read(")"))
        throw new ParseException("Syntax Error: invalid syntax", ls)
      new Statement(ls)
    })

  override def eval(e: Env) =
    cond.eval(e).g match {
      case true => null
      case false => prog.eval(e)
    }
}


package marg.ast.other

import marg.ast.leaf.SOperator
import marg.ast.{ASTList, ASTree}
import marg.lang.data.{SInt, SType}
import marg.parser.Env
import marg.token.TokenSet

import scala.collection.mutable.ListBuffer


class SExpr extends ASTList {
  private val operators = ListBuffer[SOperator]()

  def this(ls: TokenSet) {
    this()
    val n: ASTree = new SValue(ls)
    children = n :: Nil
    while (ls.isOperator) {
      operators += new SOperator(ls)
      children = children :+ new SValue(ls)

    }
  }

  def eval(e: Env): SType = {
    if (children.length == 1) return children.head.eval(e)

    var vals = ListBuffer[SType]()
    children.foreach(o => vals += o.eval(e))

    var ops_cpy: ListBuffer[SOperator] = operators.clone()
    val ops_cpy2 = ListBuffer[SOperator]()
    val vals_ = ListBuffer[SType]()


    for (il <- 0 to 3) {
      ops_cpy2.clear()
      vals_.clear()
      vals_ += vals.head

      for (i <- 0 to ops_cpy.size - 1) {
        if (ops_cpy(i).level == il) {
          vals_ += ops_cpy(i)(
            vals_.remove(vals_.size - 1).asInstanceOf[SInt],
            vals(i + 1).asInstanceOf[SInt])
        }
        else {
          vals_ += vals(i + 1)
          ops_cpy2 += ops_cpy(i)
        }
      }
      ops_cpy = ops_cpy2.clone()
      vals = vals_.clone()
    }

    vals.head
  }
}

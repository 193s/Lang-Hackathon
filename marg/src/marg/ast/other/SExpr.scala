package marg.ast.other

import java.util

import marg.ast.leaf.SOperator
import marg.ast.{SASTList, SASTree}
import marg.lang.data.SType
import marg.lang.operator.BiOperator
import marg.parser.SEnvironment
import marg.token.TokenSet

import scala.collection.mutable.ListBuffer


class SExpr extends SASTList {
  private val operators = ListBuffer[SOperator]()

  def this(ls: TokenSet) {
    this()
    val n: SASTree = new SValue(ls)
    children += n
    while (ls.isOperator) {
      val op: SOperator = new SOperator(ls)
      val n2: SASTree = new SValue(ls)
      operators += op
      children += n2
    }
  }

  def eval(e: SEnvironment): SType = {
    if (children.length == 1) return children.head.eval(e)

    var vals = ListBuffer[SType]()

    children.foreach(o => vals += o.eval(e))

    var ops_cpy: ListBuffer[SOperator] = operators.clone
    val ops_cpy2 = ListBuffer[SOperator]()
    val vals_ = ListBuffer[SType]()

    for (il <- 0 to BiOperator.maxLevel) {
      ops_cpy2.clear()
      vals_.clear()
      vals_ += vals.head

      for (i <- 0 to ops_cpy.size) {
        if (ops_cpy(i).level == il) {
          vals_ += ops_cpy(i).eval(
            vals_.remove(vals_.size - 1), vals(i + 1))
        }
        else {
          vals_ += vals(i + 1)
          ops_cpy2 += ops_cpy(i)
        }
      }
      ops_cpy = ops_cpy2.clone
      vals = vals_.clone
    }

    return vals.head
  }
}

package marg.ast.other

import java.util

import marg.ast.leaf.Operator
import marg.ast.{SASTList, SASTree}
import marg.lang.data.SType
import marg.lang.operator.BinaryOperators
import marg.parser.SEnvironment
import marg.token.TokenSet

import scala.collection.mutable.ListBuffer


class SExpr extends SASTList {
  private val operators: ListBuffer[Operator] = ListBuffer[Operator]()

  def this(ls: TokenSet) {
    this()
    val n: SASTree = new SValue(ls)
    children += n
    while (ls.isOperator) {
      val op: Operator = new Operator(ls)
      val n2: SASTree = new SValue(ls)
      operators += op
      children += n2
    }
  }

  def eval(e: SEnvironment): SType = {
    var vals: ListBuffer[SType] = ListBuffer[SType]()

    children.foreach(o => vals += o.eval(e))
//    for (v <- children) {
//      vals add v.eval(k + 1, e)
//      if (count >= operators.size) break //todo: break is not supported
//      Debug.log(operators.get(count).string)
//      count += 1
//    }
    var ops_cpy: ListBuffer[Operator] = operators.clone()
    val ops_cpy2 = ListBuffer[Operator]()
    val vals_ : ListBuffer[SType] = ListBuffer[SType]()

    for (il <- 0 to BinaryOperators.maxLevel) {
      ops_cpy2.clear
      vals_.clear
      vals_ += vals.head

      for (i <- 0 to ops_cpy.size) {
        if (ops_cpy(i).level == il) {
          vals_ += ops_cpy(i).eval.apply(
            vals_.remove(vals_.size - 1), vals(i + 1)).asInstanceOf[SType]
        }
        else {
          vals_ += vals(i + 1)
          ops_cpy2 += ops_cpy(i)
        }
      }
      ops_cpy = ops_cpy2.clone()
      vals = vals_.clone()
    }

    return vals.head
  }
}

package marg.ast.statement

import marg.ast.{SASTree, SASTList}
import marg.ast.leaf.Operator
import marg.ast.other.Value
import marg.debug.Debug
import marg.lang.data.SType
import marg.lang.operator.BinaryOperators
import marg.parser.Environment
import marg.token.TokenSet

import scala.collection.mutable.ListBuffer


class SExpr extends SASTList {
  private var operators: ListBuffer[Operator] = List

  def this(ls: TokenSet) {
    this()
    val n: SASTree = new SValue(ls)
    children += n
    while (ls.isOperator) {
      val op: Operator = new Operator(ls)
      val n2: Value = new Value(ls)
      operators += op
      children += n2
    }
  }

  def eval(k: Int, e: Environment): SType = {
    Debug.log(k, "Expr")
    var vals = List[SType]()
    var count: Int = 0
    for (v <- children) {
      vals = vals :+ v.eval(k + 1, e)
      if (count >= operators.size) break //todo: break is not supported
      Debug.log(operators.get(count).string)
      count += 1
    }
    var ops_cpy: List[Operator] = List[Operator](operators)
    val ops_cpy2: List[Operator] = List[Operator]()
    val vals_ : List[_] = new List[_]
    {
      var il = 0
      while (il < BinaryOperators.maxLevel) {
        ops_cpy2.clear
        vals_.clear
        vals_.add(vals.get(0)) {
          var i: Int = 0
          while (i < ops_cpy.size) {
            if (ops_cpy.get(i).level == il) {
              vals_.add(ops_cpy.get(i).eval.apply(vals_.remove(vals_.size - 1), vals.get(i + 1)))
            }
            else {
              vals_.add(vals.get(i + 1))
              ops_cpy2.add(ops_cpy.get(i))
            }
          }
          i = i + 1
        }
        ops_cpy = new List[Operator](ops_cpy2)
        vals = new List[_](vals_)
        il = il + 1
      }
    }
    return vals(0)
  }
}

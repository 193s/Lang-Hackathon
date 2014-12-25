package marg.ast.other

import marg.ast.base.{ASTList, ASTree}
import marg.ast.leaf.SOperator
import marg.lang.data.{SInt, SType}
import marg.parser.Env
import marg.token.TokenSet

// <Expr> := <expr> <operator> <expr>
class Expr extends ASTList {
  private var operators = List[SOperator]()

  def this(ls: TokenSet) {
    this()
    val n: ASTree = new SValue(ls)
    children = n :: Nil
    while (ls.isOperator) {
      operators = operators :+ new SOperator(ls)
      children = children :+ new SValue(ls)
    }
  }

  // FIXME: so dirty...
  override def eval(e: Env): SType = {
    if (children.length == 1) return children.head.eval(e)

    var vals = for (o <- children) yield o.eval(e)
    // OR var vals = children.map(_.eval(e))

    var ops_cpy = operators

    for (il <- 0 to 3) {
      var ops_cpy2 = List[SOperator]()
      var v2 = List(vals.head)

      for (i <- 0 to ops_cpy.size - 1) {
        if (ops_cpy(i).level == il) {
          val last = v2.last
          v2 = v2.init :+ ops_cpy(i) (
            last.asInstanceOf[SInt],
            vals(i + 1).asInstanceOf[SInt]
          )
        }
        else {
          v2 = v2 :+ vals(i + 1)
          ops_cpy2 = ops_cpy2 :+ ops_cpy(i)
        }
      }
      ops_cpy = ops_cpy2
      vals = v2
    }

    vals.head
  }
}

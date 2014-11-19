package marg.ast.leaf

import marg.ast.base.ASTLeaf
import marg.lang.data.SInt
import marg.parser.Env
import marg.token.TokenSet

class SOperator private(var string: String) extends ASTLeaf {
  var level = 0
  var func: (SInt, SInt) => SInt = null

  def this(ls: TokenSet) = {
    this(ls.next().String)

    if (SOperator.map_op.contains(string)) {
      val s = SOperator.map_op(string)
      level = s._1
      func = s._2
    }
  }

  def apply(a: SInt, b: SInt): SInt = func(a, b)

  override def eval(e: Env) = null
}

object SOperator {
  private val map_op = Map[String, (Int, (SInt, SInt) => SInt)] (
    "*" -> (0, (a: SInt, b: SInt) => new SInt(a.g * b.g)),
    "/" -> (0, (a: SInt, b: SInt) => new SInt(a.g / b.g)),
    "+" -> (1, (a: SInt, b: SInt) => new SInt(a.g + b.g)),
    "-" -> (1, (a: SInt, b: SInt) => new SInt(a.g - b.g)),
    "%" -> (2, (a: SInt, b: SInt) => new SInt(a.g % b.g))

//    "==" -> (3, (a: SInt, b: SInt) => new SBool(a.g == b.g)),
//    "!=" -> (3, (a: SInt, b: SInt) => new SBool(a.g != b.g)),
//    ">"  -> (3, (a: SInt, b: SInt) => new SBool(a.g > b.g)),
//    "<"  -> (3, (a: SInt, b: SInt) => new SBool(a.g < b.g))
  )
}

package marg.ast.leaf

import marg.ast.ASTLeaf
import marg.lang.data.{SBool, SInt, SType}
import marg.parser.SEnvironment
import marg.token.TokenSet

class SOperator(var string: String) extends ASTLeaf {
  var level = 0
  var func: (SInt, SInt) => SInt = null
  var assignment: Boolean = false

  def this(ls: TokenSet) {
    this(ls.next.getString)

    if (string == "=")
      assignment = true
    else if (map_op.contains(string)) {
      val s = map_op(string)
      level = s._1
      func = s._2
    }
  }

  def apply(a: SInt, b: SInt): SInt = func(a, b)

  def eval(e: SEnvironment): SType = null

  val map_op = Map[String, (Int, (SInt, SInt) => SInt)] (
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

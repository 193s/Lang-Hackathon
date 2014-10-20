package marg.ast.leaf

import marg.ast.SASTLeaf
import marg.lang.data.{SBool, SInt, SType}
import marg.parser.SEnvironment
import marg.token.TokenSet

class SOperator(var string: String) extends SASTLeaf {
  var level = 0
  var func: (_ <: SType, _ <: SType) => _ <: SType = null
  var assignment: Boolean = false

  def this(ls: TokenSet) {
    this(ls.next().string)

    if (string == "=")
      assignment = true
    else if (map_op.contains(string)) {
      val a = map_op(string)
      level = a._1
      func = a._2
    }
  }

  def eval(a: SType, b: SType): SType = {
    func.apply(null, null)
  }

  def eval(e: SEnvironment): SType = null

  val map_op = Map[String, (Int, (_ <: SType, _ <: SType) => _ <: SType)] (
    "*" -> (0, (a: SInt, b: SInt) => new SInt(a.g * b.g)),
    "/" -> (0, (a: SInt, b: SInt) => new SInt(a.g / b.g)),
    "+" -> (1, (a: SInt, b: SInt) => new SInt(a.g + b.g)),
    "-" -> (1, (a: SInt, b: SInt) => new SInt(a.g - b.g)),
    "%" -> (2, (a: SInt, b: SInt) => new SInt(a.g % b.g)),

    "==" -> (3, (a: SInt, b: SInt) => new SBool(a.g == b.g)),
    "!=" -> (3, (a: SInt, b: SInt) => new SBool(a.g != b.g)),
    ">"  -> (3, (a: SInt, b: SInt) => new SBool(a.g > b.g)),
    "<"  -> (3, (a: SInt, b: SInt) => new SBool(a.g < b.g))
  )
}

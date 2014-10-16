package marg.ast

import marg.lang.data.SType
import marg.parser.Environment

abstract class SASTRLeaf extends SASTree {
  var string: String

  def eval(k: Int, e: Environment): SType = null
}

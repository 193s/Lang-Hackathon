package marg.ast.leaf

import marg.ast.SASTLeaf
import marg.lang.data.SType
import marg.parser.SEnvironment

class SVariable(val string: String) extends SASTLeaf {
  def eval(e: SEnvironment): SType = e.get(string)
}

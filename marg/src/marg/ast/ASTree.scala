package marg.ast

import marg.lang.data.SType
import marg.parser.SEnvironment


abstract class ASTree {
  def eval(e: SEnvironment): SType
}

package marg.ast

import marg.lang.data.SType
import marg.parser.Env


abstract class ASTree {
  def eval(e: Env): SType
}

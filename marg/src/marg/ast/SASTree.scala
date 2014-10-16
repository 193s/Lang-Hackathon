package marg.ast

import marg.lang.data.SType
import marg.parser.Environment
import marg.token.TokenSet


abstract class SASTree {
  def build(ls: TokenSet): Unit
  def eval(k: Int, e: Environment): SType
}

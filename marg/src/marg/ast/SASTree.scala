package marg.ast

import marg.lang.data.SType
import marg.parser.{SEnvironment, Environment}
import marg.token.TokenSet


abstract class SASTree {
  def eval(e: SEnvironment): SType
}

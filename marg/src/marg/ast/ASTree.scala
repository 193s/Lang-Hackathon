package marg.ast

import marg.lang.data.SType
import marg.parser.{SEnvironment, Environment}
import marg.token.TokenSet


abstract class ASTree {
  def eval(e: SEnvironment): SType
}

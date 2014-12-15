package marg.util

import scala.io.AnsiColor

object AnsiExtension extends AnsiColor {
  val CLEAR = "\033[2J\033[0;0H"
}

package marg.main


class RuntimeOption private(source: String, var debug: Boolean) {
  def isDebug = debug
  def src = source

  def toggleDebug() { debug = !debug }
}

object RuntimeOption {
  def apply(source: String, debug: Boolean = false) = new RuntimeOption(source, debug)
}

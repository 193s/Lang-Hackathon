package marg.exception


class RuntimeError(message: String) extends Exception {
  override def getMessage = message
}

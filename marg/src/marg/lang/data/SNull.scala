package marg.lang.data

@Deprecated
class SNull extends SType {
  def name: String = "null"
  def set(o: AnyRef) = throw new NullPointerException()
  def get: AnyRef = null
}

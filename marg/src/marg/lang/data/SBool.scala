package marg.lang.data

class SBool(var value: Boolean) extends SType {
  def name: String = "bool"
  def set(o: Any) = { value = o.asInstanceOf[Boolean] }
  def get: Any = value

  def s(o: Boolean) = { value = o }
  def g(): Boolean = value
}

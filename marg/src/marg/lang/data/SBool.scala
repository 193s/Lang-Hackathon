package marg.lang.data

class SBool(var value: Boolean) extends SType {
  def name: String = "bool"
  def set(o: AnyRef) = { value = o.asInstanceOf[Boolean] }
  def get: AnyRef = value
}

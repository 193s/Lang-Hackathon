package marg.lang.data


class SBool(var value: Boolean) extends SType {
  override def name = "bool"
  override def set(o: Any) = {
    require(o.isInstanceOf[Boolean])
    value = o.asInstanceOf[Boolean]
  }
  override def get: Any = value

  def s(o: Boolean) = { value = o }
  def g: Boolean = value
}

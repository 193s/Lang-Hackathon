package marg.lang.data


class SInt(var value: Int) extends SType {
  def name: String = "int"
  def get: AnyRef = value.asInstanceOf[AnyRef]
  def set(o: AnyRef) = { value = o.asInstanceOf[Int] }
}

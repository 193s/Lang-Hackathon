package marg.lang.data


class SInt(var value: Int) extends SType {
  def name: String = "int"
  def get: Any = value
  def set(o: Any) = { value = o.asInstanceOf[Int] }

  def s(o: Int) = { value = o }
  def g(): Int = value
}

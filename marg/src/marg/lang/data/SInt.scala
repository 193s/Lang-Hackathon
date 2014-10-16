package marg.lang.data


class SInt(var value: Int) extends SType {


  def name: String = "int"

  def get: Int = value

  def set(o: AnyRef): Unit = {
    value = o.toString().toInt
  }
}

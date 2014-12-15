package marg.lang.data


trait SType {
  def name: String
  def get: Any
  def set(o: Any): Unit
}

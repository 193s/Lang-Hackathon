package marg.lang.data


abstract class SType {
  def name: String
  def get: Any
  def set(o: Any): Unit
}

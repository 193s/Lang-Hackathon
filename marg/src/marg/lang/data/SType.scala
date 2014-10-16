package marg.lang.data


abstract class SType {
  def name: String
//  def get: AnyRef
  def set(o: AnyRef): Unit
}

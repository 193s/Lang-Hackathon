package marg.util


object Extension {
  def convertList[T](list: List[T]): java.util.List[T] = {
    val ret = new java.util.ArrayList[T]()
    list.foreach(ret.add)
    ret
  }
}

package marg.util


object Extension {
  def getChar(c: Char, times: Int): String = {
    val builder = new StringBuilder()
    for (_ <- 0 to times) builder.append(c)
    builder.toString()
  }

  def convertList[T](list: List[T]): java.util.List[T] = {
    val ret = new java.util.ArrayList[T]()
    list.foreach(ret.add)
    ret
  }
}

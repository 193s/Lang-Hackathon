package marg.util


object MyExtension {
  def convertList[T](list: List[T]) = {
    val ret = new java.util.ArrayList[T]()
    list.foreach(ret.add)
    ret
  }

  def removeIndex[T](list: List[T], index: Int) =
    if (list.size < index) list
    else list.take(index) ++ list.drop(index+1)
}

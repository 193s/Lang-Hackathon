package marg.ast

import scala.collection.mutable.ListBuffer

abstract class SASTList extends SASTree {
  var children: ListBuffer[SASTree]

  def get(i: Int): SASTree = children(i)
}

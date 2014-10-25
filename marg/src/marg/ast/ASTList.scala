package marg.ast

import scala.collection.mutable.ListBuffer

abstract class ASTList extends ASTree {
  var children: ListBuffer[ASTree] = ListBuffer()

  def get(i: Int): ASTree = children(i)
}

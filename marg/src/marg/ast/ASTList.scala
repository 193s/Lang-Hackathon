package marg.ast


abstract class ASTList extends ASTree {
  var children = List[ASTree]()

  def get(i: Int) = children(i)
}

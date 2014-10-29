package marg.ast.statement

import marg.ast.{ASTLeaf, ASTree, ASTList}
import marg.lang.data.SType
import marg.parser.SEnvironment
import marg.ast.other.SStatement
import marg.ast.leaf.{SOperator, SVariable}
import marg.token.TokenSet

class SAssign extends ASTList {
  def this(ls: TokenSet) {
    this()
    val left = new SVariable(ls.next.getString)
    val operator = new SOperator(ls.next.getString)
    val right = new SStatement(ls)
    children ++= List(left, operator, right)
  }

  def eval(e: SEnvironment): SType = {
    val identifier = children.head.asInstanceOf[ASTLeaf].string
    val ret = children.last.eval(e)
    e.put(identifier, ret)

    ret
  }
}


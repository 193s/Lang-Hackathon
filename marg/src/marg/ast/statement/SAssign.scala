package marg.ast.statement

import marg.ast.{SASTLeaf, SASTree, SASTList}
import marg.lang.data.SType
import marg.parser.SEnvironment
import marg.ast.other.SStatement
import marg.ast.leaf.{SOperator, SVariable, Variable}
import marg.token.TokenSet

class SAssign extends SASTList {
  def this(ls: TokenSet) {
    this()
    val left = new SVariable(ls.next().string)
    val operator = new SOperator(ls.next().string)
    val right = new SStatement(ls)
    children ++= List(left, operator, right)
  }

  def eval(e: SEnvironment): SType = {
    val identifier = children.head.asInstanceOf[SASTLeaf].string
    val ret: SType = children.last.eval(e)
    e.put(identifier, ret)
    return ret
  }
}


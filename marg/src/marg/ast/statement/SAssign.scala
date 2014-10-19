package marg.ast.statement

import marg.ast.{SASTree, SASTList}
import marg.lang.data.SType
import marg.parser.SEnvironment
import marg.ast.other.SStatement
import marg.ast.leaf.Variable
import marg.token.TokenSet

class SAssign extends SASTList {
  def this(ls: TokenSet) {
    this()
    ls.checkEOF()
    val left: SASTree = new SVariable(ls)
    ls.checkEOF()
    val operator: SASTree = new SOperator(ls)
    val right: SASTree = new SStatement(ls)
    children += left
    children += operator
    children += right
  }

  def eval(e: SEnvironment): SType = {
    val ret: SType = children.last.eval(e)
    val identifier: String = children.head.asInstanceOf[Variable].string
    e.put(identifier, ret)
    return ret
  }
}


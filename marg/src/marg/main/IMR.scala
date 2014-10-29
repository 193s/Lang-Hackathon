package marg.main

import marg.ast.ASTree
import marg.lexer.{ILexer, SLexer}
import marg.exception.ParseException
import marg.parser._
import marg.token.{TokenSet, Token}
import marg.util.AnsiExtension

import scala.io.StdIn


object IMR {
  def main(args: Array[String]) {
    // Shutdown Hook
    sys addShutdownHook {
      println(Console.RESET)
      println("Program will exit...")
    }

    val e: SEnvironment = new SEnvironment(null)
    val lexer: ILexer = new SLexer
    val parser: IParser = new SParser

    while (true) exec(e, lexer, parser)
  }

  def exec(e: SEnvironment, lexer: ILexer, parser: IParser): Unit = {
    print("Marg> ")
    print(Console.GREEN)
    var input = StdIn.readLine()

    print(Console.RESET)

    if (input.last == '\\') {
      input = input.init
      print("\\\t")
      print(Console.GREEN)
      val input_ = StdIn.readLine()
      print(Console.RESET)
      input += input_
    }

    if (input.head == ':')
      input.tail match {
        case "?" | "h" | "help" => println(":?")
        case "q" | "quit" => sys.exit()
        case "c" | "clear" =>
          print(AnsiExtension.CLEAR)
          return
        case "v" | "values" =>
          e.map.foreach(p => println(p._1 + " := " + p._2.get))
        case s =>
          print(Console.RED)
          println("Command not found: " + s)
          print(Console.RESET)
      }

    else {
      execLine(input, e, lexer, parser)
    }

    println()
  }


  def execLine(input: String, e: SEnvironment, lexer: ILexer, parser: IParser): Unit = {
    var ls: List[Token] = Nil
    try {
      ls = lexer.tokenize(input)
    }
    catch {
      // No input
      case ex: NullPointerException => return
    }

    var ast: ASTree = null
    try {
      ast = parser.parse(new TokenSet(ls))
    }
    catch {
      case ex: ParseException =>
        print(Console.RED)
        println(ex.getMessage)
        print(Console.RESET)

        val offset = ex.getErrorOffset
        val list = ls.slice(offset - 3, offset + 3).init
        list.foreach(t => print(t.getString))
        return
      case ex: Exception =>
        println("Unexpected error")
        print(Console.RED)
        ex.printStackTrace()
        print(Console.RESET)
        return
    }

    try {
      ast.eval(e)
    }
    catch {
      case ex: Exception =>
        println("Runtime error")
        print(Console.RED)
        ex.printStackTrace()
        print(Console.RESET)
        return
    }
  }
}

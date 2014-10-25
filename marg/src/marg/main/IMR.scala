package marg.main

import java.io.{BufferedReader, InputStreamReader}

import marg.ast.ASTree
import marg.debug.Debug
import marg.lexer.{ILexer, SLexer}
import marg.exception.ParseException
import marg.parser._
import marg.token.{TokenSet, Token}
import marg.util.AnsiExtension

import scala.collection.JavaConverters._


object IMR {
  def main(args: Array[String]): Unit = {
    Debug.setEnabled(false)

    // Shutdown Hook
    sys addShutdownHook {
      println(Console.RESET)
      println("Program will exit...")
    }

    val e: SEnvironment = new SEnvironment(null)
    val lexer: ILexer = new SLexer
    val parser: IParser = new SParser
    val isr: InputStreamReader = new InputStreamReader(System.in)
    val reader: BufferedReader = new BufferedReader(isr)

    while (true) exec(reader, e, lexer, parser)
  }

  def exec(reader: BufferedReader, e: SEnvironment, lexer: ILexer, parser: IParser): Unit = {
    print("Marg> ")
    print(Console.GREEN)
    val input = reader.readLine()

    print(Console.RESET)

//    if (input.last == '\\') {
//      val f = () => {
//        print("\\\t")
//        print(Console.GREEN)
//        val input_ = reader.readLine()
//        print(ansi.reset())
//
//        if (input_.last == '\\') {
//          input += input_
//          f()
//        }
//        return
//      }
//      f()
//    }
    input match {
      case "exit" => sys.exit()
      case "clear" =>
        print(AnsiExtension.CLEAR)
        return
      case "values" =>
        e.map.foreach(p => println(p._1 + " := " + p._2.get))
      case _ =>
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
      ast = parser.parse(new TokenSet(ls.asJava))
    }
    catch {
      case ex: ParseException =>
        println("Parse error")
        print(Console.RED)
        println(ex.getMessage)
        ex.printStackTrace()
        print(Console.RESET)
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

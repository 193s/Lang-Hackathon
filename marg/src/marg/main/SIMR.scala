package marg.main

import java.io.{BufferedReader, InputStreamReader}

import marg.ast.SASTree
import marg.debug.Debug
import marg.lexer.SLexer
import marg.exception.ParseException
import marg.parser._
import marg.token.{TokenSet, Token}
import marg.util.AnsiExtension

import scala.collection.JavaConverters._


object SIMR {
  def main(args: Array[String]): Unit = {
    Debug.setEnabled(false)

    // Shutdown Hook
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      override def run(): Unit = {
        println(Console.RESET)
        println("Program will exit...");
      }
    }))

    val e: SEnvironment = new SEnvironment(null)
    val lexer: SLexer = new SLexer
    val parser: SParser = new SParser
    val isr: InputStreamReader = new InputStreamReader(System.in)
    val reader: BufferedReader = new BufferedReader(isr)

    while (true) exec(reader, e, lexer, parser)
  }

  def exec(reader: BufferedReader, e: SEnvironment, lexer: SLexer, parser: SParser): Unit = {
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
      case "exit" => System.exit(0)
      case "clear" => {
        print(AnsiExtension.CLEAR)
        return
      }
      case "values" => {
        e.map.foreach(p => println(p._1 + " := " + p._2.get))
      }
      case _ => {
        execLine(input, e, lexer, parser)
      }
    }
    println()
  }
  def execLine(input: String, e: SEnvironment, lexer: SLexer, parser: SParser): Unit = {
    var ls: List[Token] = Nil
    try {
      ls = lexer.tokenize(input)
    }
    catch {
      // No input
      case ex: NullPointerException => return
    }

    var ast: SASTree = null
    try {
      ast = parser.parse(new TokenSet(ls.asJava))
    }
    catch {
      case ex: ParseException => {
        println("Parse error")
        println(ex.getMessage())
        ex.printStackTrace()
        return
      }
      case ex: Exception => {
        println("Unexpected error")
        print(Console.RED)
        ex.printStackTrace()
        print(Console.RESET)
        return
      }
    }

    try {
      ast.eval(e)
    }
    catch {
      case ex: Exception => {
        println("Runtime error")
        ex.printStackTrace()
        return
      }
    }
  }
}

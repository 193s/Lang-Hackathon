package marg.main

import marg.lexer.{ILexer, SLexer}
import marg.exception.ParseException
import marg.parser._
import marg.token.TokenSet

import scala.io.StdIn

import scala.Console._
import marg.util.AnsiExtension._

object IMR {
  def main(args: Array[String]) {
    // Shutdown Hook
    sys addShutdownHook {
      println(RESET + "Program will exit...")
    }

    val e: SEnvironment = new SEnvironment(null)
    val lexer: ILexer = new SLexer
    val parser: IParser = new SParser

    while (true) exec(e, lexer, parser)
  }


  def readLine(first: String, str: String): String = {
    print(if (first == null) str else first)
    print(GREEN)
    val input = StdIn.readLine()
    print(RESET)

    if (!input.isEmpty && input.last == '\\')
      input.init + '\n' + readLine(null, str)
    else input
  }


  def exec(e: SEnvironment, lexer: ILexer, parser: IParser): Unit = {
    val input = readLine("Marg> ", "    > ")

    if (input.isEmpty) return
    else if (input.head != ':') execLine(input, e, lexer, parser)
    else input.tail match {
      case "?" | "h" | "help" => println(" <statement> : run <statement>")
      case "v" | "version" => println("Interactive Marg v0.1")
      case "q" | "quit" | "exit" => sys.exit()
      case "c" | "cls" | "clear" => print(CLEAR)
      case "e" | "env" | "environment" =>
        e.map.foreach(p => println(s"${p._1} := ${p._2.get}"))

      case str => println(RED + s"${RED}Unknown command \'$str\'" + RESET)
    }

    println()
  }


  def execLine(input: String, e: SEnvironment, lexer: ILexer, parser: IParser) {
    try {
      val ls = lexer.tokenize(input)

      try {
        val ast = parser.parse(new TokenSet(ls))

        try ast.eval(e)
        catch {
          case ex: Exception =>
            println("Runtime error")
            print(RED + ex.getStackTrace.mkString("\n ") + RESET)
            return
        }
      }
      catch {
        case ex: ParseException =>
          println(RED + ex.getMessage + RESET)

          val offset = ex.getErrorOffset
          val list = ls.slice(offset - 3, offset + 3).init
          println(list.mkString("\n"))
          return
        case ex: Exception =>
          println("Unexpected error")
          print(RED + ex.getStackTrace.mkString("\n ") + RESET)
          return
      }
    }
    catch {
      case ex: Exception => println("An unexpected error has occurred.")
    }
  }
}

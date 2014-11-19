package marg.main

import marg.exception.{ParseException, RuntimeError}
import marg.lexer.{ILexer, SLexer}
import marg.parser._
import marg.token.Token
import marg.util.AnsiExtension._

import scala.io.StdIn


object IMR {
  private var debug = false

  def main(args: Array[String]) {
    // Shutdown Hook
    sys addShutdownHook
      println("\n\n" + RESET + "Program will exit...")

    while (true) exec(new Env(null), new SLexer, new SParser)
  }


  def readLine(first: String, second: String): String = {
    print(if (first == null) second else first)
    print(GREEN)
    // FIXME
//    new jline.console.ConsoleReader().paste()
    val input = StdIn.readLine()
    print(RESET)

    if (!input.isEmpty && input.last == '\\')
      input.init + '\n' + readLine(null, second)
    else input
  }


  def exec(e: Env, lexer: ILexer, parser: IParser): Unit = {
    val input = readLine("Marg> ",
                         "    > ")

    if (input.isEmpty) return
    else if (input.head != ':') execLine(input, e, lexer, parser)
    else input.tail match {
      case "" =>
      case "?" | "h" | "help" => println(" <statement> : run <statement>")
      case "v" | "version" => println("Interactive Marg v0.1")
      case "q" | "quit" | "exit" => sys.exit()
      case "c" | "cls" | "clear" => print(CLEAR)
      case "e" | "env" | "environment" =>
        for (p <- e.map) println(s"${p._1} := ${p._2.get}")
      case "r" | "reset" => e.map.clear()
      case "d" | "debug" =>
        debug = !debug
        println(s"debug ${if (debug) "on" else "off"}.")

      case str => println(RED + s"${RED}Unknown command \'$str\'" + RESET)
    }

    println()
  }


  def execLine(input: String, e: Env, lexer: ILexer, parser: IParser) {
    val ls: List[Token] = try lexer.tokenize(input)
    catch {
      case ex: NullPointerException =>
        return

      case ex: Exception =>
        println("Failed to tokenize.")
        return
    }

    if (debug) println(YELLOW + ls.mkString("\n") + RESET)

    val ast = try parser.parse(ls)
    catch {
      case ex: ParseException =>
        println(RED + ex.getMessage + RESET)

        val offset = ex.getErrorOffset
        val list = ls.slice(offset - 3, offset + 3).init
        println(list.mkString("\n"))
        return

      case ex: Exception =>
        println("An unexpected error has occurred.")
        println(RED + ex.getStackTrace.slice(0, 10).mkString("\n ") + RESET)
        return
    }

    try {
      val eval = ast.eval(e)
      if (eval != null) println(eval.get)
    }
    catch {
      case ex: RuntimeError =>
        println("Runtime error!")
        println(BLUE + ex.getMessage + RESET)

      case ex: Exception =>
        println("An unexpected error has occurred while running.")
        println(YELLOW + ex.getClass + RESET)
        println(RED + ex.getStackTrace.slice(0, 10).mkString("\n ") + RESET)
        return
    }
  }
}

package marg.main

import marg.exception.{ParseException, RuntimeError}
import marg.lexer.{ILexer, SLexer}
import marg.parser._
import marg.util.AnsiExtension._

import scala.tools.jline.console.ConsoleReader


object IMR {
  private val status = RuntimeOption("", debug = false)
  private val reader = new ConsoleReader()

  def main(args: Array[String]) {
    // Shutdown Hook
    sys addShutdownHook
      println("\n\n" + RESET + "Program will exit...")

    while (true) exec()
  }


  def readLine(first: String, second: String, isFirstLine: Boolean = false): String = {
    val prompt = if (isFirstLine) second
                 else first

    val input = reader.readLine(prompt + GREEN)
    print(RESET)

    if (input.isEmpty || input.last != '\\') input
    else input.init + '\n' + readLine(first, second, isFirstLine = true)
  }


  private def exec(e: Env = new Env(null), lexer: ILexer = new SLexer(), parser: IParser = new SParser()) {
    val input = readLine("Marg> ",
                         "    | ")

    if (input.isEmpty) return
    else if (input.head != ':') execLine(input, e, lexer, parser)
    else input.tail match {
      case "" =>
      case "?" | "h" | "help" => println(" <statement> : run <statement>")
      case "v" | "version" => println("Interactive Marg v0.1")
      case "q" | "quit" | "exit" => sys.exit()
      case "c" | "cls" | "clear" => print(CLEAR)
      case "s" | "status" =>
        for (p <- e.map) println(s"${p._1} := ${p._2.get}")
      case "reset" => e.map.clear()
      case "d" | "debug" =>
        status.toggleDebug()
        println(s"debug ${if (status.isDebug) "on" else "off"}.")

      case str => println(RED + s"${RED}Unknown command '$str'" + RESET)
    }

    println()
  }


  private def execLine(input: String, e: Env, lexer: ILexer, parser: IParser) {
    val ls = try lexer.tokenize(input)
    catch {
      case ex: NullPointerException =>
        return

      case ex: Exception =>
        println("Failed to tokenize.")
        return
    }

    if (status.isDebug) println(YELLOW + ls.mkString("\n") + RESET)

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

    val eval = try ast.eval(e)
    catch {
      case ex: RuntimeError =>
        println("Runtime error!")
        println(BLUE + ex.getMessage + RESET)
        return

      case ex: Exception =>
        println("An unexpected error has occurred while running.")
        println(YELLOW + ex.getClass + RESET)
        println(RED + ex.getStackTrace.slice(0, 10).mkString("\n ") + RESET)
        return
    }
    if (eval != null) println(eval.get)
  }
}

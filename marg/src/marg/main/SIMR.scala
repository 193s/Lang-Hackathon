package marg.main

import java.io.{BufferedReader, InputStreamReader, IOException}

import marg.ast.ASTree
import marg.debug.Debug
import marg.lexer.SLexer
import marg.exception.ParseException
import marg.parser.{Parser, IParser, Environment}
import marg.token.{TokenSet, Token}
import org.fusesource.jansi.Ansi.Color._
import org.fusesource.jansi.Ansi._

import scala.collection.JavaConverters._


object SIMR {
  def main(args: Array[String]): Unit = {
    Debug.setEnabled(false)

    // Shutdown Hook
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      override def run(): Unit = {
        print(ansi().reset().a('\n'))
        println("Program will exit...");
      }
    }))

    val e: Environment = new Environment(null)
    val lexer: SLexer = new SLexer
    val parser: IParser = new Parser
    val isr: InputStreamReader = new InputStreamReader(System.in)
    val reader: BufferedReader = new BufferedReader(isr)

    while (true) exec(reader, e, lexer, parser)
  }

  def exec(reader: BufferedReader, e: Environment, lexer: SLexer, parser: IParser): Unit = {
    print("Marg> ")
    print(ansi.fg(GREEN))
    var input = reader.readLine()
    print(ansi.reset())

    if (input.last == '\\') {
      val f = () => {
        print("\\\t")
        print(ansi.fg(GREEN))
        val input_ = reader.readLine()
        print(ansi.reset())

        if (input_.last == '\\') {
          input += input_
          f()
        }
        return
      }
      f()
    }
    input match {
      case "exit" => System.exit(0)
      case "clear" => println(ansi().eraseScreen())
      case "values" => {
        println("values:") //FIXME

//        for (entry: java.util.Set <- e.map.entrySet()) {
//          println(entry)
//        }
      }
      case _ => {
        execLine(input, e, lexer, parser)
      }
    }
  }
  def execLine(input: String, e: Environment, lexer: SLexer, parser: IParser): Unit = {
    var ls: List[Token] = Nil
    try {
      ls = lexer.tokenize(input)
    }
    catch {
      // No input
      case ex: NullPointerException => {
        return
      }
    }
    var ast: ASTree = null
    try {
      ast = parser.parse(new TokenSet(ls.asJava))
    }
    catch {
      case ex: ParseException => {
        println("Parse error.")
        println(ex.getMessage())
        ex.printStackTrace()
        return
      }
      case ex: Exception => {
        println("Unexpected error.")
        println(ansi.fg(RED).a(ex.getStackTrace).reset())
        return
      }
    }
    try {
      ast.eval(0, e)
    }
    catch {
      case ex: Exception => {
        println("Runtime error.")
        ex.printStackTrace()
        return
      }
    }
  }
}

package marg.main

import java.io.{BufferedReader, InputStreamReader, IOException}

import marg.ast.ASTree
import marg.debug.Console._
import marg.debug.Debug
import marg.lexer.SLexer
import marg.exception.ParseException
import marg.parser.{Parser, IParser, Environment}
import marg.token.{TokenSet, Token}
import org.fusesource.jansi.Ansi.Color._
import org.fusesource.jansi.Ansi._

import scala.collection.JavaConverters._


class SIMR {
  def main(args: Array[String]): Unit = {
    Debug.setEnabled(false)

    // Shutdown Hook
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      override def run(): Unit = {
        out.print(ansi().reset().a('\n'))
        out.println("Program will exit...");
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
    try {
      out.print("Marg> ")
      out.print(ansi.fg(GREEN))
      val s: String = reader.readLine()
      out.print(ansi.reset())
      if ("exit" == s) {
        System.exit(0)
      }
      if ("values" == s) {
        out.println("Values:")

//        for (entry: java.util.Set <- e.map.entrySet()) {
//          out.println(entry)
//        }//FIXME
      }
      else if ("clear" == s) {
        out.println(ansi().eraseScreen())
//      continue //todo: continue is not supported
      }
      var ls: List[Token] = Nil
      try {
        ls = lexer.tokenize(s)
      }
      catch {
        // No input
        case ex: NullPointerException => {
//        continue //todo: continue is not supported
        }
      }
      var ast: ASTree = null
      try {
        ast = parser.parse(new TokenSet(ls.asJava))
      }
      catch {
        case ex: ParseException => {
          out.println("Parse error.")
          out.println(ex.getMessage())
          ex.printStackTrace(out)
//          continue //todo: continue is not supported
        }
        case ex: Exception => {
          out.println("Unexpected error.")
          out.println(ansi.fg(RED).a(ex.getStackTrace).reset())
          //            continue //todo: continue is not supported
        }
      }
      try {
        ast.eval(0, e)
      }
      catch {
        case ex: Exception => {
          out.println("Runtime error.")
          ex.printStackTrace(out)
          //            continue //todo: continue is not supported
        }
      }
    }
    catch {
      case ex: IOException => {
       ex.printStackTrace
     }
    }
  }
}

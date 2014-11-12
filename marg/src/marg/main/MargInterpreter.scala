package marg.main

import java.io.{File, IOException, FileNotFoundException}
import java.util.InputMismatchException

import marg.util.Options
import Options._
import marg.exception.ParseException
import marg.lexer.{SLexer, ILexer}
import marg.parser.{Env, SParser, IParser}
import marg.token.Token
import marg.util.CommandLineOption

import scala.io.Source


object MargInterpreter {

  def main(args: Array[String]) {
    var option: CommandLineOption = null
    try {
      option = new CommandLineOption(args)
    }
    catch {
      case e: FileNotFoundException =>
        println("File not found.")
        sys.exit(-1)
      case e: InputMismatchException =>
        println("No input files.")
        sys.exit(-1)
    }

    option.`type` match {
      case Run => run(option, debug = true)
      case Version => println("Version: β")
      case _ => println("Undefined option.")
    }
  }

  def run(option: CommandLineOption, debug: Boolean) {
    val s = try Source.fromFile(new File("example.mg")).getLines().reduceLeft(_+_)
    catch {
      case e: IOException =>
        println("Failed to read Input File.")
        sys.exit(-1)
    }


    val lexer: ILexer = new SLexer()
    val ls: List[Token] = try lexer.tokenize(s)
    catch {
      case e: NullPointerException =>
        println("No input files.")
        sys.exit(-1)
    }

    if (debug) println(ls.mkString("\n "))
    val parser: IParser = new SParser

    val ast = try parser.parse(ls)
    catch {
      case e: ParseException =>
        println("Failed to parse. Program will exit.")
        println(e.getStackTrace.slice(0, 10).mkString("\n "))
        sys.exit(-1)
    }
    val e: Env = new Env(null)

    try ast.eval(e)
    catch {
      case ex: Exception =>
        println("Runtime error")
        println(ex.getStackTrace.slice(0, 10).mkString("\n "))
    }
    finally {
      println("Process finished.")
    }
  }
}

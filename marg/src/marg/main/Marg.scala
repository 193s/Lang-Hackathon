package marg.main

import java.io.{IOException, FileNotFoundException}
import java.util.InputMismatchException

import marg.ast.ASTree
import marg.command.CommandLineOption
import marg.command.Options._
import marg.debug.Debug
import marg.exception.ParseException
import marg.lexer.{SLexer, ILexer}
import marg.parser.{SEnvironment, SParser, IParser}
import marg.token.{Token, TokenSet}

import scala.collection.JavaConverters._


object Marg {
  def main(args: Array[String]) {
    Debug.setEnabled(false)
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
      case Run => run(option)
      case Version => println("Version: β")
      case _ => println("Undefined option.")
    }
  }

  def run(option: CommandLineOption) {
    var s: String = null
    try {
      s = option.read
    }
    catch {
      case e: IOException =>
        println("Failed to read Input File.")
        sys.exit(-1)
    }

    val lexer: ILexer = new SLexer()
    var ls: List[Token] = null
    try {
      ls = lexer.tokenize(s)
    }
    catch {
      case e: NullPointerException =>
        println("No input files.")
        sys.exit(-1)
    }
    val parser: IParser = new SParser
    var ast: ASTree = null
    try {
      ast = parser.parse(new TokenSet(ls.asJava))
    }
    catch {
      case e: ParseException =>
        println("Failed to parse. Program will exit.")
        e.printStackTrace()
        sys.exit(-1)
    }
    Debug.log("--Parse finished--")
    val e: SEnvironment = new SEnvironment(null)
    Debug.log("--- RUNNING ---")

    try {
      ast.eval(e)

      Debug.log("Environment:")
      e.map.foreach(entry => Debug.log(entry._1 + " : " + entry._2))
    }
    catch {
      case ex: Exception =>
        println("RUNTIME ERROR:")
        ex.printStackTrace()
    }
    finally {
      println("Process finished.")
    }
  }
}

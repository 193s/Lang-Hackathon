package marg.main

import java.io.{IOException, FileNotFoundException}
import java.util.InputMismatchException

import marg.ast.ASTree
import marg.util.Options
import Options._
import marg.exception.ParseException
import marg.lexer.{SLexer, ILexer}
import marg.parser.{SEnvironment, SParser, IParser}
import marg.token.{Token, TokenSet}
import marg.util.CommandLineOption


object Marg {

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
      case Run => run(option)
      case Version => println("Version: β")
      case _ => println("Undefined option.")
    }
  }

  def run(option: CommandLineOption) {
    val s = try option.read
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
    val parser: IParser = new SParser

    val ast = try parser.parse(new TokenSet(ls))
    catch {
      case e: ParseException =>
        println("Failed to parse. Program will exit.")
        println(e.getStackTrace.slice(0, 10).mkString("\n "))
        sys.exit(-1)
    }
    val e: SEnvironment = new SEnvironment(null)

    try ast.eval(e)
    catch {
      case ex: Exception =>
        println("Runtime error")
        ex.printStackTrace()
    }
    finally {
      println("Process finished.")
    }
  }
}

package marg.main

import java.io.{File, FileNotFoundException, IOException}
import java.util.InputMismatchException

import marg.exception.ParseException
import marg.lexer.{ILexer, SLexer}
import marg.parser.{Env, IParser, SParser}
import marg.util.CommandLineOption
import marg.util.Options._

import scala.io.Source


object Marg {

  def main(args: Array[String]) {
    val option: CommandLineOption = try new CommandLineOption(args)
    catch {
      case e: FileNotFoundException =>
        println("File not found.")
        sys.exit(-1)
      case e: InputMismatchException =>
        println("No input files.")
        sys.exit(-1)
    }

    option.kind match {
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
    val ls = try lexer.tokenize(s)
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
    val e = new Env(null)

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

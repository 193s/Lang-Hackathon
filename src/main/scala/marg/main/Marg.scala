package marg.main

import java.io.{FileNotFoundException, IOException}
import java.lang.System.err
import java.util.InputMismatchException

import marg.exception.ParseException
import marg.lexer.SLexer
import marg.parser.{Env, SParser}
import marg.util.CommandLineOption
import marg.util.Options._
import marg.util.AnsiExtension._

import scala.io.Source


object Marg {

  def main(args: Array[String]) {
    val option = try new CommandLineOption(args)
    catch {
      case e: FileNotFoundException =>
        println("File not found.")
        sys.exit(-1)
      case e: InputMismatchException =>
        println("No input files.")
        sys.exit(-1)
    }

    val s = try Source.fromFile("example.mg").getLines().mkString("\n")
    catch {
      case e: IOException => errExit("Failed to read Input File.")
    }

    val status = RuntimeOption(s, debug = true)

    option.kind match {
      case Run => run(status)
      case Version => println("Version: β")
      case _ => errExit("Undefined option.")
    }
  }

  def run(status: RuntimeOption) {

    val lexer = new SLexer()
    val ls = try lexer.tokenize(status.src)
    catch {
      case e: NullPointerException => errExit("No input files.")
    }

    // debug mode
    if (status.isDebug) println(ls.mkString("\n "))

    val parser = new SParser()

    val ast = try parser.parse(ls)
    catch {
      case e: ParseException =>
        err println "Failed to parse. Program will exit."
        println(e.getStackTrace.slice(0, 10).mkString("\n "))
        sys.exit(-1)
    }

    val e = new Env(null)

    try ast.eval(e)
    catch {
      case ex: Exception =>
        println("Runtime error" + RED)
        println(ex.getStackTrace.slice(0, 10).mkString("\n "))
        println(RESET)
    }
    finally println("Process finished.")
  }

  private def errExit(message: String) = {
    err.println(RED + message + RESET)
    sys.exit(-1)
  }
}

package marg.util

import java.io.{File, FileNotFoundException, FileReader, IOException}
import java.util.InputMismatchException


class CommandLineOption {
  private var file: File = null
  var kind: Options = null

  def this(args: Array[String]) {
    this()
    for (s <- args) {
      if (s.head == '-') {
        val str: String = s.init
        str match {
          case "v" | "V" =>
            kind = Options.Version
          case _ =>
            println("Undefined option")
        }
      }
      else {
        kind = Options.Run
        file = readFile(s)
      }
    }
    if (kind == null) throw new InputMismatchException
  }

  def read: String = {
    val reader = new FileReader(file)
    var s = ""
    var c = 0
    try {
      reader.read()
      while(c != -1) {
        s += c.asInstanceOf[Char]
        c = reader.read()
      }
    }
    catch {
      case e: IOException =>
        throw e
    }
    finally {
      reader.close()
    }
    return s
  }

  private def readFile(s: String): File =
    new File(s) match {
      case f =>
        if (f.exists()) f
        else throw new FileNotFoundException()
    }
}

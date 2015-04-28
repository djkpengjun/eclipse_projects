package basic

import scala.util.matching.Regex

object Regular {
  def main(args : Array[String]) = {
    val pattern = new Regex("(S|s)cala");
    val str = "this is a scala test Scala"
    println( (pattern findAllIn str ).mkString(","))
  }
}
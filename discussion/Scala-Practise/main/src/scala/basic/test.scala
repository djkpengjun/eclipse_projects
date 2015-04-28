
package basic

object test {
  
  private[this] val x1 = List(1, 2, 3)
  
  protected[basic] val x2 = Set(1, 2, 3, 4, 3)
  
  val x3 = Map("one" -> 1, "two" -> 2, 23 -> 4)
  val x4 = (1, 2, "xy")
  val x5: Option[Int] = Some(3)

  // Nested function
  def factorial(i : Int): Int = {
    def fact(i: Int, accumulator : Int) : Int = {
      if (i<= 1)
        accumulator
      else
        fact(i-1, i*accumulator)
    }
    fact(i, 1)
  }
  
  def main(args: Array[String]) = {

    println(x1)

    println(x2)
    println(x3)
    println(x4)
    println(x5)

    val it = Iterator(3, 4, 5, 7, 0)
    while (it.hasNext) {
      println(it.next())
    }

    val it1 = x1.iterator
    while (it1.hasNext) {
      println(it1.next())
    }

    println(factorial(9))
  }

}
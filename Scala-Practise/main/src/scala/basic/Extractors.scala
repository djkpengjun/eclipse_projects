package basic

object Extractors {
  
  def apply(x: Int) = x*2
  
  def unapply(z: Int) : Option[Int] = if (z%2 == 0) Some(z/2) else None
  
  def main(args : Array[String]) = {
    val x = Extractors(5)
    println(x)
    
    x match {
      case Extractors(num) => print(x + " is bigger two times than " + num)
      case _ => println("i cannot calculate")
    }
    
  }
  
}
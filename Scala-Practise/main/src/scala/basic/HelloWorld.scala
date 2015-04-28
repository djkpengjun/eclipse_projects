package scala.basic

trait equal {
    def isEqual(x:Any):Boolean
    def isNotEqual(x:Any):Boolean = !isEqual(x)
}

case class Person(name:String, age:Int)

object HelloWorld {

    def matchTest(x:Int) : String = x match {
        case 1=> "one"
        case 2=> "two"
        case _=> "many"
    }

    def main(args:Array[String])={
        println("hello world")
        println(matchTest(1))
        println(matchTest(2))
        println(matchTest(4))
        
        val peter = new Person("Peter", 30)
        val bob = new Person("Bob", 100)
        val nobody = new Person("nobody", 200)
        for (person <- List(peter, bob, nobody))
        {
            person match {
                case Person("Peter", 30) => println("Hi Peter!")
                case Person("Bob", 100) => println("Hi Bob!")
                case _ => println("Hi nobody!")
                
            }
        }
    }

}
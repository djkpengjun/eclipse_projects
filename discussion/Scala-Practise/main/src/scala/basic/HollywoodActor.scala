package scala.basic

import akka.actor.Actor
import akka.actor.ActorSystem
import scala.util.Properties
import akka.actor.Props

class HollywoodActor extends Actor {

  def receive = {
    case role => println("Playing " + role + "from thread" + Thread.currentThread().getName)
  }
  
  def main(atgs : Array[String]) : Unit = 
  {
    val system = ActorSystem.create()
  }
}
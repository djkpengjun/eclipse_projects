package HollywoodActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class HollywoodActor extends UntypedActor
{

  public static void main( String[] args ) throws InterruptedException
  {
    final ActorSystem system = ActorSystem.create( "MySystem" );
    final ActorRef peter = system.actorOf( Props.create( HollywoodActor.class ) );
    peter.tell( "Jack Chow", ActorRef.noSender() );
    Thread.sleep( 100 );
    peter.tell( "Edward", ActorRef.noSender() );
    Thread.sleep( 100 );
    peter.tell( "Jack Chow", ActorRef.noSender() );
    system.shutdown();
  }

  @Override
  public void onReceive( Object role ) throws Exception
  {
    System.out.println( "Playing " + role + " from thread " + Thread.currentThread().getName() );
  }

}

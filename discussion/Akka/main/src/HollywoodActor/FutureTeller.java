package HollywoodActor;

import static akka.pattern.Patterns.ask;

import java.util.concurrent.TimeUnit;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class FutureTeller extends UntypedActor
{

  public FutureTeller()
  {
    getContext().setReceiveTimeout( Duration.create( "30 seconds" ) );
  }

  @Override
  public void onReceive( Object name ) throws Exception
  {
    if ( name instanceof String )
      getSender().tell( String.format( "%s you'll rock", name ), getSelf() );
    else
    {
      unhandled( name );
      System.out.println( "Not string" );
    }
  }

  @Override
  public void postStop()
  {
    System.out.println( "Dying..!" );
  }

  LoggingAdapter log = Logging.getLogger( getContext().system(), this );

  public static void main( final String[] args ) throws Exception
  {
    final ActorSystem system = ActorSystem.create( "MySystem" );
    final ActorRef fortuneTeller = system.actorOf( Props.create( FutureTeller.class ) );

    fortuneTeller.tell( new Integer( 2 ), ActorRef.noSender() );

    /**
     * Messages are sent to an Actor through one of the following methods. tell means “fire-and-forget”, e.g. send a
     * message asynchronously and return immediately. ask sends a message asynchronously and returns a Future
     * representing a possible reply.
     */
    final Future<Object> future = ask( fortuneTeller, "Emma", 1000 );
    String result = (String) Await.result( future, Duration.create( 1, TimeUnit.SECONDS ) );
    System.out.println( result );

    /**
     * When writing code outside of actors which shall communicate with actors, the ask pattern can be a solution (see
     * below), but there are two thing it cannot do: receiving multiple replies (e.g. by subscribing an ActorRef to a
     * notification service) and watching other actors’ lifecycle. For these purposes there is the Inb
     */
    final Inbox inbox = Inbox.create( system );
    inbox.send( fortuneTeller, "Becky" );
    final Object response = inbox.receive( Duration.create( 1, TimeUnit.SECONDS ) );
    System.out.println( response );

    system.shutdown();
  }
}

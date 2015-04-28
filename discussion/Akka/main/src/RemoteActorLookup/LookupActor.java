package RemoteActorLookup;

import java.util.concurrent.CountDownLatch;

import akka.actor.ActorIdentity;
import akka.actor.ActorRef;
import akka.actor.Identify;
import akka.actor.ReceiveTimeout;
import akka.actor.UntypedActor;

public class LookupActor extends UntypedActor
{

  private String path = null;

  private ActorRef remoteActor = null;

  private CountDownLatch deployComplete = null;

  public LookupActor( String path, CountDownLatch deployComplete )
  {
    this.path = path;
    this.deployComplete = deployComplete;
    sendIdentifyRequest();
  }

  public LookupActor( ActorRef actor )
  {
    remoteActor = actor;
    remoteActor.tell( new Identify( path ), getSelf() );
  }

  private void sendIdentifyRequest()
  {
    getContext().actorSelection( path ).tell( new Identify( path ), getSelf() );
  }

  @Override
  public void onReceive( Object message ) throws Exception
  {

    if ( message instanceof ActorIdentity )
    {
      remoteActor = ( (ActorIdentity) message ).getRef();
      System.out.println( "Get remote actor identity: " + remoteActor.path().address() );
      deployComplete.countDown();
    }
    else if ( message.equals( ReceiveTimeout.getInstance() ) )
    {
      sendIdentifyRequest();

    }
    else if ( remoteActor == null )
    {
      System.out.println( "Not ready yet" );

    }
    else if ( message instanceof Ops.Add )
    {
      System.out.printf( "Get Add result\n" );

    }
    else if ( message instanceof Ops.Substract )
    {
      System.out.printf( "Get Sub result\n" );

    }
    else
    {
      unhandled( message );
    }
  }
}

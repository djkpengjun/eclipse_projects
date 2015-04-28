package RemoteActorLookup;

import java.util.concurrent.CountDownLatch;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;

import com.typesafe.config.ConfigFactory;

public class LookupApplication implements Bootable
{
  private ActorSystem system;
  private ActorRef actor;
  public static CountDownLatch deployComplete = new CountDownLatch( 1 );

  public LookupApplication() throws InterruptedException
  {
    // Create remote
    system = ActorSystem.create( "CalculatorApplication", ConfigFactory.load().getConfig( "remotecreation" ) );

    // Create server
    system.actorOf( Props.create( CalculatorActor.class ), "simpleCalculator" );

    // Look up server

    final String path = "akka.tcp://CalculatorApplication@127.0.0.1:2552/user/simpleCalculator";

    actor = system.actorOf( Props.create( LookupActor.class, path, deployComplete ) );
  }

  public void doSomething( Ops.Add add )
  {
    actor.tell( add, null );
  }

  @Override
  public void startup()
  {
  }

  @Override
  public void shutdown()
  {
    system.shutdown();
  }

  public static void main( String[] args ) throws InterruptedException
  {
    LookupApplication lookup = new LookupApplication();
    deployComplete.await();
    lookup.doSomething( new Ops.Add() );
    lookup.shutdown();
  }
}

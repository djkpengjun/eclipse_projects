package PrimeFinder_Actor;

import static akka.pattern.Patterns.ask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import PrimeFinder_Actor.PrimeFinder;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

@SuppressWarnings( "unchecked" )
public class Primes extends UntypedActor
{

  static final ActorSystem system = ActorSystem.create( "MySystem" );

  @Override
  public void onReceive( final Object boundsList ) throws Exception
  {
    final List<Integer> bounds = (List<Integer>) boundsList;
    final int count = PrimeFinder.countPrimesInRange( bounds.get( 0 ), bounds.get( 1 ) );
    getSender().tell( count, getSelf() );
  }

  public static int countPrimes( final int number, final int numberOfParts ) throws Exception
  {
    final int chunksPerPartition = number / numberOfParts;

    final List<Future<Object>> results = new ArrayList<>();

    for ( int index = 0; index < numberOfParts; index++ )
    {
      final int lower = index * chunksPerPartition + 1;
      final int upper = ( index == numberOfParts - 1 ) ? number : lower + chunksPerPartition - 1;
      final List<Integer> bounds = Collections.unmodifiableList( Arrays.asList( lower, upper ) );
      final ActorRef prime = system.actorOf( Props.create( Primes.class ) );
      results.add( ask( prime, bounds, 1000 ) );
    }

    int count = 0;

    for ( Future<Object> result : results )
    {
      count += (Integer) ( result.result( Duration.create( 5, TimeUnit.SECONDS ), null ) );
    }

    system.shutdown();

    return count;
  }

  public static void main( final String[] args ) throws NumberFormatException, Exception
  {
    if ( args.length < 2 )
      System.out.println( "Usage: number numberOfParts" );
    else
    {
      final long start = System.nanoTime();
      final int count = countPrimes( Integer.parseInt( args[ 0 ] ), Integer.parseInt( args[ 1 ] ) );
      final long end = System.nanoTime();
      System.out.println( "Number of primes is " + count );
      System.out.println( "Time taken " + ( end - start ) / 1.0e9 );
    }
  }
}

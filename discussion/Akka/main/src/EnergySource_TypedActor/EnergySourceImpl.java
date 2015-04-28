package EnergySource_TypedActor;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.TypedActor;
import akka.actor.TypedProps;

public class EnergySourceImpl implements EnergySource, TypedActor.PreStart, TypedActor.PostStop
{
  static final ActorSystem system = ActorSystem.create( "MySystem" );

  private final long MAXLEVEL = 100L;
  private long level = MAXLEVEL;
  private long usageCount = 0L;

  private Cancellable replenisher;

  private Cancellable consumer;

  class Replenish
  {
  }

  private void replenish()
  {
    System.out.println( "Thread in replenish: " + Thread.currentThread().getName() );
    if ( level < MAXLEVEL )
      level += 10;
  }

  @Override
  public void preStart()
  {

    replenisher = system.scheduler().schedule( Duration.Zero(), Duration.create( 5, TimeUnit.SECONDS ), new Runnable()
      {

        @Override
        public void run()
        {
          replenish();
        }

      }, system.dispatcher() );

    consumer = system.scheduler().schedule( Duration.Zero(), Duration.create( 5, TimeUnit.SECONDS ), new Runnable()
      {
        @Override
        public void run()
        {
          useEnergy( 10 );
          System.out.println( "Energy units " + getUnitsAvailable() );
          System.out.println( "Usage " + getUsageCount() );
        }

      }, system.dispatcher() );
  }

  @Override
  public void postStop()
  {
    replenisher.cancel();
    consumer.cancel();
  }

  @Override
  public long getUnitsAvailable()
  {
    return level;
  }

  @Override
  public long getUsageCount()
  {
    return usageCount;
  }

  @Override
  public void useEnergy( long units )
  {
    if ( units > 0 && level - units >= 0 )
    {
      System.out.println( "Thread in useEnergy: " + Thread.currentThread().getName() );
      level -= units;
      usageCount++;
    }
  }

  public static void main( String[] args ) throws InterruptedException
  {

    System.out.println( "Thread in main: " + Thread.currentThread().getName() );

    TypedActor.get( system )
        .typedActorOf( new TypedProps<EnergySourceImpl>( EnergySource.class, EnergySourceImpl.class ) );
    Thread.sleep( 100000 );
    system.shutdown();
  }

}

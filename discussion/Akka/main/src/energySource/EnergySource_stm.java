package energySource;

import static scala.concurrent.stm.japi.STM.atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import scala.concurrent.stm.Ref;
import scala.concurrent.stm.japi.STM;

public class EnergySource_stm
{

  private final long MAXLEVEL = 100;

  final Ref.View<Long> level = STM.newRef( MAXLEVEL );
  final Ref.View<Long> usageCount = STM.newRef( 0L );
  final Ref.View<Boolean> keepRunning = STM.newRef( true );
  private static final ScheduledExecutorService replenishTimer = Executors.newScheduledThreadPool( 10 );

  private static final EnergySource_stm energySource = create();

  private EnergySource_stm()
  {
  }

  private void init()
  {
    replenishTimer.schedule( new Runnable()
      {

        @Override
        public void run()
        {
          replenish();
          if ( keepRunning.get() )
          {
            replenishTimer.schedule( this, 1, TimeUnit.SECONDS );
          }
        }
      }, 1, TimeUnit.SECONDS );
  }

  public static EnergySource_stm create()
  {
    final EnergySource_stm energySource = new EnergySource_stm();
    energySource.init();
    return energySource;
  }

  public void stopEnergySource()
  {
    keepRunning.swap( false );
    replenishTimer.shutdown();
  }

  public long getUnitsAvailable()
  {
    return level.get();
  }

  public long getUsageCount()
  {
    return usageCount.get();
  }

  public boolean useEnergy( final long units )
  {
    return STM.atomic( new Callable<Boolean>()
      {

        @Override
        public Boolean call()
        {
          long currentLevel = level.get();
          if ( units > 0 && currentLevel >= units )
          {
            level.swap( currentLevel - units );
            usageCount.swap( usageCount.get() + 1 );
            return true;
          }
          return false;
        }

      } );
  }

  protected void replenish()
  {
    atomic( new Callable<Void>()
      {

        @Override
        public Void call()
        {
          long currentLevel = level.get();
          if ( currentLevel < MAXLEVEL )
            level.swap( currentLevel + 1 );
          return null;
        }

      } );
  }

  public static void main( String[] args ) throws InterruptedException
  {
    System.out.println( "Energy level at start: " + energySource.getUnitsAvailable() );

    List<Callable<Void>> tasks = new ArrayList<>();

    IntStream.range( 0, 10 ).sequential().forEach( i -> tasks.add( new Callable<Void>()
      {

        @Override
        public Void call()
        {
          IntStream.range( 0, 7 ).parallel().forEach( j -> energySource.useEnergy( 1 ) );
          return null;
        }

      } ) );

    final ExecutorService service = Executors.newFixedThreadPool( 10 );

    service.invokeAll( tasks );

    System.out.println( "Energy level at end: " + energySource.getUnitsAvailable() );

    System.out.println( "Usage: " + energySource.getUsageCount() );

    energySource.stopEnergySource();

    service.shutdown();
  }
}

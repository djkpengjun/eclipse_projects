package fileSize;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentTotalFileSizeWQueue
{
  private ExecutorService service;

  final private BlockingQueue<Long> fileSizes = new ArrayBlockingQueue<Long>( 500 );

  final AtomicLong pendingFileVisits = new AtomicLong();

  private void startExplorDir( final File file )
  {
    pendingFileVisits.incrementAndGet();
    service.execute( new Runnable()
      {
        @Override
        public void run()
        {
          explorDir( file );
        }
      } );
  }

  private void explorDir( final File file )
  {
    long fileSize = 0;
    if ( file.isFile() )
      fileSize = file.length();
    else
    {
      final File[] children = file.listFiles();
      if ( children != null )
      {
        for ( final File child : children )
        {
          if ( child.isFile() )
            fileSize += child.length();
          else
            startExplorDir( child );
        }
      }
    }
    try
    {
      fileSizes.put( fileSize );
    }
    catch ( Exception ex )
    {
      throw new RuntimeException( ex );
    }
    pendingFileVisits.decrementAndGet();
  }

  private long getTotalSizeOfFile( final String fileName ) throws InterruptedException
  {
    service = Executors.newFixedThreadPool( 100 );
    try
    {
      startExplorDir( new File( fileName ) );
      long totalSize = 0;
      while ( pendingFileVisits.get() > 0 || fileSizes.size() > 0 )
      {
        totalSize += fileSizes.poll( 10, TimeUnit.SECONDS );
      }
      return totalSize;
    }
    finally
    {
      service.shutdown();
    }
  }

  public static void main( String[] args ) throws InterruptedException
  {
    final long start = System.nanoTime();
    final long total = new ConcurrentTotalFileSizeWQueue().getTotalSizeOfFile( args[ 0 ] );
    final long end = System.nanoTime();
    System.out.println( "Total size: " + total );
    System.out.println( "Time taken: " + ( end - start ) / 1.0e9 );
  }

}

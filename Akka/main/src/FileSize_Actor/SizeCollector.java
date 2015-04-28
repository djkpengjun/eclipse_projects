package FileSize_Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.IndirectActorProducer;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;

public class SizeCollector extends UntypedActor
{
  static final ActorSystem system = ActorSystem.create( "MySystem" );

  private final List<String> toProcessFileNames = new ArrayList<>();

  private final List<ActorRef> idleFileProcessors = new ArrayList<>();

  private long pendingNumberOfFilesToVisit = 0L;

  private long totalSize = 0L;

  private long start = System.nanoTime();

  public void sendAFileToProcess()
  {
    if ( !toProcessFileNames.isEmpty() && !idleFileProcessors.isEmpty() )
      idleFileProcessors.remove( 0 ).tell( new FileToProcess( toProcessFileNames.remove( 0 ) ), ActorRef.noSender() );
  }

  @Override
  public void onReceive( Object message ) throws Exception
  {
    if ( message instanceof RequestAFile )
    {
      idleFileProcessors.add( getSender() );
      sendAFileToProcess();
    }
    else if ( message instanceof FileToProcess )
    {
      toProcessFileNames.add( ( (FileToProcess) message ).fileName );
      sendAFileToProcess();
      pendingNumberOfFilesToVisit += 1;
    }
    else if ( message instanceof FileSize )
    {
      totalSize += ( (FileSize) message ).size;
      pendingNumberOfFilesToVisit -= 1;
      if ( pendingNumberOfFilesToVisit == 0 )
      {
        long end = System.nanoTime();
        System.out.println( "Total size is " + totalSize );
        System.out.println( "Time taken is " + ( end - start ) / 1.0e9 );
        system.shutdown();
      }
    }
  }

  static class MyActorCreator implements Creator<FileProcessor>
  {
    private static final long serialVersionUID = 1L;
    final ActorRef sizeCollector;

    public MyActorCreator( final ActorRef sizeCollector )
    {
      this.sizeCollector = sizeCollector;
    }

    @Override
    public FileProcessor create() throws Exception
    {
      return new FileProcessor( sizeCollector );
    }
  }

  public static void main( String[] args )
  {
    final ActorRef sizeCollector = system.actorOf( Props.create( SizeCollector.class ) );
    sizeCollector.tell( new FileToProcess( args[ 0 ] ), ActorRef.noSender() );
    //    IntStream.range( 0, 100 ).forEach( i -> system.actorOf( FileProcessor.getFileProcessorProp( sizeCollector ) ) );
    IntStream.range( 0, 100 ).parallel()
        .forEach( i -> system.actorOf( Props.create( new MyActorCreator( sizeCollector ) ) ) );
  }

  // Not used
  class DependencyInjector implements IndirectActorProducer
  {
    @Override
    public Class<? extends Actor> actorClass()
    {
      return FileProcessor.class;
    }

    @Override
    public Actor produce()
    {
      // obtain fresh Actor instance from DI framework ...
      return null;
    }
  }
}

package FileSize_Actor;

import java.io.File;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;

class RequestAFile
{
}

class FileToProcess
{
  public final String fileName;

  public FileToProcess( final String name )
  {
    fileName = name;
  }
}

class FileSize
{
  public final long size;

  public FileSize( final long size )
  {
    this.size = size;
  }
}

public class FileProcessor extends UntypedActor
{
  private ActorRef sizeCollector;

  public FileProcessor( ActorRef szieCollector )
  {
    this.sizeCollector = szieCollector;
  }

  public static Props getFileProcessorProp( final ActorRef sizeCollector )
  {
    return Props.create( new Creator<FileProcessor>()
      {
        private static final long serialVersionUID = 1L;

        @Override
        public FileProcessor create() throws Exception
        {
          return new FileProcessor( sizeCollector );
        }

      } );
  }

  @Override
  public void preStart()
  {
    registerToGetFile();
  }

  public void registerToGetFile()
  {
    sizeCollector.tell( new RequestAFile(), getSelf() );
  }

  @Override
  public void onReceive( final Object message ) throws Exception
  {
    FileToProcess fileToProcess = (FileToProcess) message;
    final File file = new File( fileToProcess.fileName );
    Long size = 0L;
    if ( file.isFile() )
    {
      size = file.length();
    }
    else
    {
      File[] children = file.listFiles();
      if ( children != null )
      {
        for ( File child : children )
        {
          if ( child.isFile() )
            size += child.length();
          else
            sizeCollector.tell( new FileToProcess( child.getPath() ), ActorRef.noSender() );
        }
      }
    }
    // Return the size of files under current directory
    sizeCollector.tell( new FileSize( size ), ActorRef.noSender() );

    // Run next calculation task
    registerToGetFile();

  }

}

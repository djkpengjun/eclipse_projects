package hdfs;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

public class FileCopyWithProgress
{
  public static void main( String[] args ) throws Exception
  {
    String localStr = args[ 0 ];
    String dst = args[ 1 ];
    InputStream in = new BufferedInputStream( new FileInputStream( localStr ) );

    Configuration conf = new Configuration();

    FileSystem fs = FileSystem.get( URI.create( dst ), conf );

    OutputStream out = fs.create( new Path( dst ), new Progressable()
      {
        @Override
        public void progress()
        {
          System.out.println( "." );
        }

      } );

    IOUtils.copyBytes( in, out, 4096, true );

    // Reload 
    InputStream outputFile = null;
    try
    {
      outputFile = fs.open( new Path( dst ) );
      System.out.println( dst );
      IOUtils.copyBytes( outputFile, System.out, 4096, false );
    }
    finally
    {
      IOUtils.closeStream( outputFile );
    }
  }
}

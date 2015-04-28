package hdfs;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

public class URLCat
{
  static
  {
    URL.setURLStreamHandlerFactory( new FsUrlStreamHandlerFactory() );
  }

  public static void main( String[] args ) throws IOException
  {
    InputStream in = null;
    try
    {
      in = new URL( args[ 0 ] ).openStream();
      /**
       * The last two arguments to the copyBytes method are the buffer size used for copying and whether to close the
       * streams when the copy is complete. We close the input stream ourselves, and System.out doesn’t need to be
       * closed.
       */
      IOUtils.copyBytes( in, System.out, 4096, false );
    }
    finally
    {
      IOUtils.closeStream( in );
    }
  }
}

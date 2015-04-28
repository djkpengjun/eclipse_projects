package hdfs;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ShowFileStatusTest
{
  // Use an in-process HDFS cluster for testing
  private MiniDFSCluster cluster;

  private FileSystem fs;

  @Before
  public void setup() throws IOException
  {
    Configuration conf = new Configuration();

    if ( System.getProperty( "test.build.data" ) == null )
    {
      System.setProperty( "test.build.data", "/tmp" );
    }
    cluster = new MiniDFSCluster( conf, 1, true, null );
    fs = cluster.getFileSystem();
    OutputStream out = fs.create( new Path( "dir/file" ) );
    out.write( "content".getBytes( "UTF-8" ) );
    out.close();
  }

  @After
  public void tearDown() throws IOException
  {
    if ( fs != null )
    {
      fs.close();
    }
    if ( cluster != null )
    {
      cluster.shutdown();
    }
  }

  @Test( expected = FileNotFoundException.class )
  public void throwsFileNotFoundForNonExistentFile() throws IOException
  {
    fs.getFileStatus( new Path( "no-such-file" ) );
  }

  @Test
  public void fileStatusForFile() throws IOException
  {
    Path file = new Path( "/dir/file" );
    FileStatus stat = fs.getFileStatus( file );
    assertEquals( stat.getPath().toUri().getPath(), "/dir/file" );
    assertEquals( stat.isDir(), false );
    assertEquals( stat.getLen(), 7L );
    assertEquals( stat.getModificationTime(), System.currentTimeMillis() );
    assertEquals( stat.getReplication(), (short) 1 );
    assertEquals( stat.getBlockSize(), 64 * 1024 * 1024L );
    assertEquals( stat.getOwner(), "tom" );
    assertEquals( stat.getGroup(), "supergroup" );
    assertEquals( stat.getPermission().toString(), "rw-r--r--" );
  }

  @Test
  public void fileStatusForDirectory() throws IOException
  {
    Path dir = new Path( "/dir" );
    FileStatus stat = fs.getFileStatus( dir );
    assertEquals( stat.getPath().toUri().getPath(), "/dir" );
    assertEquals( stat.isDir(), true );
    assertEquals( stat.getLen(), 0L );
    assertEquals( stat.getModificationTime(), System.currentTimeMillis() );
    assertEquals( stat.getReplication(), (short) 0 );
    assertEquals( stat.getBlockSize(), 0L );
    assertEquals( stat.getOwner(), "tom" );
    assertEquals( stat.getGroup(), "supergroup" );
    assertEquals( stat.getPermission().toString(), "rwxr-xr-x" );
  }
}

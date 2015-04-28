package redis.cluster;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

public class ClusterCommandsTest
{
  private static Jedis node1;
  private static Jedis node2;

  // Node 7000
  private HostAndPort nodeInfo1 = HostAndPortUtil.getClusterServers().get( 0 );
  // Node 7001
  private HostAndPort nodeInfo2 = HostAndPortUtil.getClusterServers().get( 1 );

  @Before
  public void setUp() throws Exception
  {

    node1 = new Jedis( nodeInfo1.getHost(), nodeInfo1.getPort() );
    node1.connect();
    node1.flushAll();

    node2 = new Jedis( nodeInfo2.getHost(), nodeInfo2.getPort() );
    node2.connect();
    node2.flushAll();
  }

  @After
  public void tearDown()
  {
    node1.disconnect();
    node2.disconnect();
  }

  @Test
  public void keepAddingValues()
  {
    node1.clusterAddSlots( 5000 );
    String[] nodes = node1.clusterNodes().split( "\n" );
    String nodeId = nodes[ 0 ].split( " " )[ 0 ];
    String status = node1.clusterSetSlotMigrating( 5000, nodeId );
    assertEquals( "OK", status );
  }

}

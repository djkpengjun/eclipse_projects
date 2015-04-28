package redis.cluster;

import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class ClusterTest
{

  private static Jedis jedis;
  private static ShardedJedis sharding;
  private static ShardedJedisPool pool;
  private static JedisCluster cluster;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    //    List<JedisShardInfo> shards = Arrays.asList( new JedisShardInfo( "localhost", 7000 ),
    //                                                 new JedisShardInfo( "localhost", 7001 ),
    //                                                 new JedisShardInfo( "localhost", 7002 ),
    //                                                 new JedisShardInfo( "localhost", 7003 ),
    //                                                 new JedisShardInfo( "localhost", 7004 ),
    //                                                 new JedisShardInfo( "localhost", 7005 ) );

    //    jedis = new Jedis( "localhost", 7001 );
    //
    //    sharding = new ShardedJedis( shards );
    //
    //    pool = new ShardedJedisPool( new JedisPoolConfig(), shards );

    Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
    jedisClusterNodes.add( new HostAndPort( "127.0.0.1", 7000 ) );
    cluster = new JedisCluster( jedisClusterNodes );
  }

  @Test
  public void test1Normal()
  {
    long start = System.currentTimeMillis();
    for ( int i = 0; i < 100000; i++ )
    {
      cluster.set( "sn" + i, "n" + i );
    }
    long end = System.currentTimeMillis();
    System.out.println( "Simple@Sharing SET: " + ( ( end - start ) / 1000.0 ) + " seconds" );
    cluster.getClusterNodes().forEach( ( x, y ) -> System.out.println( x ) );
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
  }

}

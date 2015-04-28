package redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.basic.RedisDataTypes;
import redis.basic.analytic.AnalyticService;
import redis.basic.key_value.CounterService;
import redis.basic.key_value.ObjectCircle;
import redis.basic.oauth.oAuthService;
import redis.basic.queue.QueueService;
import redis.basic.search.RankSearchService;
import redis.basic.search.SearchService;
import redis.clients.jedis.Jedis;

public class RedisTest
{
  Jedis jedis = null;

  @Before
  public void connect()
  {
    jedis = new Jedis( "localhost", 7000 );
  }

  @After
  public void disconnect()
  {
    jedis.disconnect();
  }

  @Test
  public void testAnalyticService()
  {
    new AnalyticService().main( jedis );
  }

  @Test
  public void testRedisDataTypes()
  {
    new RedisDataTypes().main( null );
  }

  @Test
  public void testCounterService()
  {
    new CounterService().main( null );
  }

  @Test
  public void testObjectCircle()
  {
    new ObjectCircle().main( null );
  }

  @Test
  public void testoAuthService() throws Exception
  {
    new oAuthService().main( null );
  }

  @Test
  public void testoSearch() throws Exception
  {
    new SearchService().main( jedis );
  }

  @Test
  public void testoRankSearch() throws Exception
  {
    new RankSearchService().main( jedis );
  }

  @Test
  public void testoQueueService() throws Exception
  {
    new QueueService().main( jedis );
  }

}

package redis.basic.queue;

import java.util.List;

import redis.clients.jedis.Jedis;

public class QueueService
{
  Jedis conn;

  private static final String QUEUE_PREFIX = "queue:";

  void enqueue_job( String queueName, String data )
  {
    conn.sadd( "queues", queueName );
    conn.rpush( QUEUE_PREFIX.concat( queueName ), data );
  }

  /**
   * if there are no jobs in the queue, nothing will be returned and we need to check again later. However, if we check
   * too often, we’re consuming both Redis and worker resources, and if we don’t check every few seconds we add latency
   * to our job processing. To resolve this problem, Redis offers a blocking pop operation.
   * 
   * @param queueName
   */
  void dequeue_job( String queueName )
  {
    conn.blpop( 2, QUEUE_PREFIX.concat( queueName ) );
  }

  List<String> dequeue_jobs( String[] queues )
  {
    return conn.blpop( 5, queues );
  }

  void remove_job( String queueName, String data )
  {
    conn.lrem( QUEUE_PREFIX.concat( queueName ), 0, data );
  }

  void clear( String queueName )
  {
    conn.del( QUEUE_PREFIX.concat( queueName ) );
  }

  void destroy( String queueName )
  {
    clear( queueName );
    conn.srem( "queue", queueName );
  }

  void length( String queueName )
  {
    clear( queueName );
    conn.llen( QUEUE_PREFIX.concat( queueName ) );
  }

  List<String> peek( String queueName )
  {
    return conn.lrange( QUEUE_PREFIX.concat( queueName ), 0, 0 );
  }

  void work( String[] queues )
  {
    while ( true )
    {
      // two element array including unlocking key and popped value
      List<String> jobInfo = dequeue_jobs( queues );
      if ( jobInfo == null || jobInfo.isEmpty() )
      {
        System.out.println( "Work Done for this round!\n\n" );
        break;
      }
      //      System.out.println( jobInfo.get( 0 ) );
      process_job( jobInfo.get( 1 ) );
    }
  }

  void process_job( String job )
  {
    System.out.println( "Finish job : ".concat( job ) );
  }

  public void main( Jedis redis )
  {
    conn = redis;

    conn.del( "queues" );

    enqueue_job( "high-priority", "high_job1" );
    enqueue_job( "high-priority", "high_job2" );
    enqueue_job( "high-priority", "high_job3" );

    enqueue_job( "low-priority", "low_job1" );
    enqueue_job( "low-priority", "low_job2" );
    enqueue_job( "low-priority", "low_job3" );

    work( new PrefixedCollection( new String[] { "high-priority", "low-priority" }, QUEUE_PREFIX ).prefix() );
    /**
     * Beware that this won’t respect the order of your queues because we’re simply grabbing the set from Redis (with no
     * order specified). To treat the queues in a priority order, you’d need to use a sorted set and assign different
     * scores to your queues.
     */

    enqueue_job( "high-priority", "high_job1" );
    enqueue_job( "high-priority", "high_job2" );
    enqueue_job( "high-priority", "high_job3" );

    enqueue_job( "low-priority", "low_job1" );
    enqueue_job( "low-priority", "low_job2" );
    enqueue_job( "low-priority", "low_job3" );
    work( new PrefixedCollection( conn.smembers( "queues" ), QUEUE_PREFIX ).prefix() );
  }
}

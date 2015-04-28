package redis.basic.key_value;

import redis.clients.jedis.Jedis;

public class ObjectCircle
{
  public void main( String[] args )
  {
    @SuppressWarnings( "resource" )
    Jedis conn = new Jedis( "localhost", 7000 );

    // Friends circle
    conn.sadd( "circle:peter:friends", "users:becky" );
    conn.sadd( "circle:peter:friends", "users:leaf" );
    conn.sadd( "circle:peter:friends", "users:bzhang" );
    conn.sadd( "circle:peter:friends", "users:rain" );

    // Badminton circle
    conn.sadd( "circle:peter:badminton", "users:leaf" );
    conn.sadd( "circle:peter:badminton", "users:becky" );

    // Ping pong circle
    conn.sadd( "circle:peter:pingpong", "users:bin" );
    conn.sadd( "circle:peter:pingpong", "users:becky" );

    // Print Peter's friends circle
    System.out.println( conn.smembers( "circle:peter:friends" ) );

    // Print Peter's badminton circle
    System.out.println( conn.smembers( "circle:peter:badminton" ) );

    // Find out full information of Peter's friends who play badminton and ping pong
    for ( String userId : conn.sinter( "circle:peter:friends", "circle:peter:badminton", "circle:peter:pingpong" ) )
    {
      System.out.println( conn.hgetAll( userId ) );
    }

    // Find out full information of Peter's friends who play badminton or ping pong
    for ( String userId : conn.sunion( "circle:peter:pingpong", "circle:peter:badminton" ) )
    {
      System.out.println( conn.hgetAll( userId ) );
    }
  }
}

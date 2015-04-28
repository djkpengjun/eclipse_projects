package redis.basic.key_value;

import redis.clients.jedis.Jedis;

public class ObjectService
{
  public void main( String[] args )
  {
    @SuppressWarnings( "resource" )
    Jedis conn = new Jedis( "localhost" );

    // object :  filed : value
    conn.hset( "users:peter", "name", "Peter Peng" );
    conn.hset( "users:peter", "email", "peter@test.com" );
    conn.hset( "users:peter", "phone", "+1555313940" );
    conn.hincrBy( "users:peter", "visits", 1 );

    // object :  filed : value
    conn.hset( "users:leaf", "name", "Leaf Li" );
    conn.hset( "users:leaf", "email", "leaf@test.com" );
    conn.hset( "users:leaf", "phone", "+1555313940" );
    conn.hincrBy( "users:leaf", "visits", 3 );

    // object :  filed : value
    conn.hset( "users:becky", "name", "Becky Hu" );
    conn.hset( "users:becky", "email", "becky@test.com" );
    conn.hset( "users:becky", "phone", "+1555313940" );
    conn.hincrBy( "users:becky", "visits", 5 );

    // object :  filed : value
    conn.hset( "users:bin", "name", "Bin Zhang" );
    conn.hset( "users:bin", "email", "bin@test.com" );
    conn.hset( "users:bin", "phone", "+1555313940" );
    conn.hincrBy( "users:bin", "visits", 9 );

    // Get single property for a user
    System.out.println( conn.hget( "users:peter", "name" ) );

    // Get all properties for a user
    System.out.println( conn.hgetAll( "users:peter" ) );
  }
}

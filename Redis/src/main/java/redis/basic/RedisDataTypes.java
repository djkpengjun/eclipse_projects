package redis.basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class RedisDataTypes
{
  public void main( String[] args )
  {
    @SuppressWarnings( "resource" )
    Jedis connection = new Jedis( "localhost" );

    //String
    System.out.println( "==========================String================================" );

    connection.set( "key", "Hello World!" );

    String value = connection.get( "key" );

    System.out.println( value );

    //List
    System.out.println( "==========================List================================" );

    connection.rpush( "messages", "List element 1" );

    connection.rpush( "messages", "List element 2" );

    connection.rpush( "messages", "List element 3" );

    List<String> values = connection.lrange( "messages", 0, -1 );

    System.out.println( values );

    //Set
    System.out.println( "==========================Set================================" );

    connection.sadd( "myset", "Set element 1" );

    connection.sadd( "myset", "Set element 2" );

    connection.sadd( "myset", "Set element 3" );

    connection.sadd( "myset", "Set element 4" );

    Set<String> setValues = connection.smembers( "myset" );

    System.out.println( setValues );

    //Sorted Set
    System.out.println( "==========================ZSet (Sorted Set)================================" );

    connection.zadd( "mySortedSet", 1940, "Sorted Set element 1" );

    connection.zadd( "mySortedSet", 1953, "Sorted Set element 2" );

    connection.zadd( "mySortedSet", 1965, "Sorted Set element 3" );

    connection.zadd( "mySortedSet", 1916, "Sorted Set element 4" );

    connection.zadd( "mySortedSet", 1969, "Sorted Set element 5" );

    connection.zadd( "mySortedSet", 1912, "Sorted Set element 6" );

    setValues = connection.zrange( "mySortedSet", 0, -1 );

    System.out.println( setValues );

    //Hash
    System.out.println( "====================================Hash================================" );

    Map<String, String> pairs = new HashMap<String, String>();

    pairs.put( "name", "Akshi" );

    pairs.put( "age", "2" );

    pairs.put( "sex", "Female" );

    connection.hmset( "kid", pairs );

    values = connection.hmget( "kid", new String[] { "name", "age", "sex" } );

    System.out.println( values );

    setValues = connection.hkeys( "kid" );

    System.out.println( setValues );

    values = connection.hvals( "kid" );

    System.out.println( values );

    pairs = connection.hgetAll( "kid" );

    System.out.println( pairs );

  }
}

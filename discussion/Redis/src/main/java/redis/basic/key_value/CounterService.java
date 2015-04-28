package redis.basic.key_value;

import redis.clients.jedis.Jedis;

public class CounterService
{
  public void main( String[] args )
  {
    @SuppressWarnings( "resource" )
    Jedis conn = new Jedis( "localhost" );

    //String
    System.out.println( "==========================String================================" );

    conn.set( "visits:1:totals", "21389" );
    conn.set( "visits:2:totals", "1367894" );

    System.out.println( conn.get( "visits:1:totals" ) );

    // Increase
    conn.incr( "visits:1:totals" );
    System.out.println( conn.get( "visits:1:totals" ) );
  }
}

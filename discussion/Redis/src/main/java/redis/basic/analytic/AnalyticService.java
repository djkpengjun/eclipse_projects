package redis.basic.analytic;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.LongStream;

import redis.clients.jedis.Jedis;

public class AnalyticService
{
  Jedis conn;

  void add_Hits( String client )
  {
    conn.sadd( "client", client );
    conn.hincrBy( "stats/client:" + client, "total", 1 );
    conn.hincrBy( "stats/client:" + client, String.valueOf( LocalDate.now().toEpochDay() ), 1 );
  }

  Integer hits( String client, LocalDate day )
  {
    return Integer.parseInt( conn.hget( "stats/client:" + client, String.valueOf( day.toEpochDay() ) ) );
  }

  /**
   * Bypass the limit that Java doesn't support default value for method parameters
   * 
   * @param client
   * @return
   */
  Integer hits( String client )
  {
    return Integer.parseInt( conn.hget( "stats/client:" + client, String.valueOf( LocalDate.now().toEpochDay() ) ) );
  }

  Boolean isOverLimit( String client, int limit )
  {
    return hits( client ) > limit;
  }

  List<String> statsForPeriod( String client, LocalDate start, LocalDate end )
  {
    String[] keys = new String[ (int) ( end.toEpochDay() - start.toEpochDay() + 1 ) ];
    LongStream.range( start.toEpochDay(), end.toEpochDay() ).parallel().forEach( i -> {
      keys[ (int) i ] = String.valueOf( i );
    } );
    //    String[] keyStrings = Stream.of( keys ).map( i -> String.valueOf( i ) ).toArray( String[]::new );
    return conn.hmget( "stats/client:" + client, keys );
  }

  //  List<String> topClients( String period, int limit )
  //  {
  //    SortingParams params = new SortingParams();
  //    conn.sort( "client", params );
  //  }

  public void main( Jedis conn )
  {
    this.conn = conn;
    System.out.println( statsForPeriod( "Peter", LocalDate.now().minusDays( 3 ), LocalDate.now() ) );
  }
}

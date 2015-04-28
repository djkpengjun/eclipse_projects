package redis.basic.queue;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class PrefixedCollection
{
  private String[] stringArray = null;
  private String prefix = null;

  public PrefixedCollection( Set<String> data, String prefix )
  {
    this.stringArray = data.toArray( new String[ 0 ] ).clone();
    this.prefix = prefix;
  }

  public PrefixedCollection( List<String> data, String prefix )
  {
    this.stringArray = data.toArray( new String[ 0 ] ).clone();
    this.prefix = prefix;
  }

  public PrefixedCollection( String[] data, String prefix )
  {
    this.stringArray = data.clone();
    this.prefix = prefix;
  }

  /**
   * Refer to the implementation: Arrays.parallelSetAll( queues, i -> "queue:".concat( queues[ i ] ) );
   * 
   * @return
   */
  public String[] prefix()
  {
    IntStream.range( 0, stringArray.length ).parallel().forEach( i -> {
      stringArray[ i ] = prefix.concat( stringArray[ i ] );
    } );
    return stringArray;
  }
}

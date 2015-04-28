package basic.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 * As already mentioned maps don't support streams. Instead maps now support various new and useful methods for doing
 * common tasks.
 * 
 * @author ppeng
 */
public class map
{
  public static void main( String[] args )
  {
    Map<Integer, String> xx = new HashMap<>();

    IntConsumer put = ( i ) -> xx.putIfAbsent( i, "val" + i );

    IntConsumer putAndPrint = put.andThen( i -> System.out.println( xx.get( i ) ) );

    IntStream.rangeClosed( 1, 101 ).parallel().forEach( put );

    IntStream.rangeClosed( 1, 101 ).parallel().forEach( putAndPrint );

    // Now Map accept new metods
    xx.forEach( ( i, val ) -> System.out.println( val ) );
    xx.computeIfPresent( 3, ( num, val ) -> num + val );
    xx.get( 3 );

    xx.remove( 3, "val3" );
    xx.get( 3 );

    //we learn how to remove entries for a a given key, only if it's currently mapped to a given value:
    xx.remove( 3, "val33" );
    xx.get( 3 );

    xx.getOrDefault( 3, "val33" );

    xx.computeIfPresent( 4, ( num, val ) -> null );
    xx.containsKey( 4 );

    //Merge either put the key/value into the map if no entry for the key exists, or the merging function will be called to change the existing value.
    xx.merge( 9, "val9", ( value, newValue ) -> value.concat( newValue ) );
    xx.get( 9 );

  }
}

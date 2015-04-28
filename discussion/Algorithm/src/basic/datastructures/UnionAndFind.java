package basic.datastructures;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class UnionAndFind
{

  class Node
  {
    Integer count;
    Integer root;

    Node( int count, int root )
    {
      this.count = count;
      this.root = root;
    }
  }

  Map<Integer, Node> data;

  /**
   * At the beginning , every element itself is a set and then we unify
   * 
   * @param n
   */
  void initialize( int n )
  {
    data.put( n, new Node( 1, n ) );
  }

  public UnionAndFind( final int x[] )
  {
    data = new HashMap<>();
  }

  int findRoot( int n )
  {
    if ( !data.containsKey( n ) )
    {
      return n;
    }
    if ( n == data.get( n ).root )
    {
      return n;
    }
    // Compress the path to improve performance
    return data.get( n ).root = findRoot( data.get( n ).root );
  }

  int unify( int n, int left, int right )
  {
    System.out.println( "Current=" + n + " , Letf=" + left + " , Right=" + right );

    if ( data.containsKey( left ) && data.containsKey( right )
         && !data.get( right ).root.equals( data.get( left ).root ) )
    {
      data.get( right ).root = left; // always choose the large tree as parent
      data.get( left ).count += data.get( right ).count + 1; // add the current element of index n
      data.get( n ).root = left;

      System.out.println( "Return count from merge " + data.get( left ).count );
      return data.get( left ).count;
    }

    if ( data.containsKey( left ) )
    {
      data.get( left ).count++;
      data.get( n ).root = left;

      System.out.println( "Return count from left " + data.get( left ).count );
      return data.get( left ).count;
    }

    if ( data.containsKey( right ) )
    {
      data.get( right ).count++;
      data.get( n ).root = right;
      System.out.println( "Return count from right " + data.get( right ).count );
      return data.get( right ).count;
    }
    System.out.println( "Return count 1" );
    return 1;
  }

  public static void main( String[] args )
  {
    final Map<Integer, Integer> res = new HashMap<>();
    res.put( 1, 1 );
    int[] data = { 100, 4, 200, 1, 3, 2 };
    UnionAndFind x = new UnionAndFind( data );
    IntStream.range( 0, data.length ).forEach( index -> {
                                                 x.initialize( data[ index ] );
                                                 res.put( 1,
                                                          Math.max( res.get( 1 ),
                                                                    x.unify( data[ index ],
                                                                             x.findRoot( data[ index ] - 1 ),
                                                                             x.findRoot( data[ index ] + 1 ) ) ) );
                                               } );
    System.out.println( res.get( 1 ) );
  }
}

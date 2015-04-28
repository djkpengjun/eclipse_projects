package basic.datastructures;

import java.util.stream.IntStream;

public class RotateMatriClockwiseBy90Degree
{
  public void rotate( int[][] matrix )
  {
    if ( matrix == null || matrix.length == 0 )
    {
      return;
    }
    int m = matrix.length;

    int[][] result = new int[ m ][ m ];
    IntStream
        .range( 0, m )
        .parallel()
        .forEach( row -> IntStream.range( 0, row ).parallel()
                      .forEach( column -> result[ column ][ m - 1 - row ] = matrix[ row ][ column ] ) );
    IntStream
        .range( 0, m )
        .parallel()
        .forEach( row -> IntStream.range( 0, row ).parallel()
                      .forEach( column -> result[ row ][ column ] = matrix[ row ][ column ] ) );

  }

  /**
   * By using the relation "matrix[i][j] = matrix[n-1-j][i]", we can loop through the matrix.
   * 
   * @param matrix
   */
  public void rotateInPlace( int[][] matrix )
  {
    if ( matrix == null || matrix.length == 0 )
    {
      return;
    }
    int n = matrix.length;
    for ( int i = 0; i < n / 2; i++ )
    {
      for ( int j = 0; j < Math.ceil( ( (double) n ) / 2. ); j++ )
      {
        int temp = matrix[ i ][ j ];
        matrix[ i ][ j ] = matrix[ n - 1 - j ][ i ];
        matrix[ n - 1 - j ][ i ] = matrix[ n - 1 - i ][ n - 1 - j ];
        matrix[ n - 1 - i ][ n - 1 - j ] = matrix[ j ][ n - 1 - i ];
        matrix[ j ][ n - 1 - i ] = temp;
      }
    }
  }

  public static void main( String[] args )
  {

  }
}

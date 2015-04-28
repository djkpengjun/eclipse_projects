package basic.lambda;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Lambda
{
  public static void main( String[] args )
  {
    List<String> names = Arrays.asList( "peter", "emma", "becky" );

    // Old ways
    Collections.sort( names, new Comparator<String>()
      {
        @Override
        public int compare( String a, String b )
        {
          return b.compareTo( a );
        }
      } );

    // Java 8 lambda way, much shorter and cleaner
    Collections.sort( names, ( a, b ) -> {
      return b.compareTo( a );
    } );

    // For one line function, we can be even much shorter
    Collections.sort( names, ( a, b ) -> a.compareTo( b ) );

    System.out.println( names );
  }
}

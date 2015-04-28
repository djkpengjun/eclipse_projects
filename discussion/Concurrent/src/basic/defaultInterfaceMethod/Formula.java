package basic.defaultInterfaceMethod;

public interface Formula
{
  double calculate( int a );

  default double sqrt( int a )
  {
    return Math.sqrt( a );
  }

  public static void main( String[] args )
  {
    Formula formula = new Formula()
      {
        @Override
        public double calculate( int a )
        {
          return sqrt( a * 100 );
        }
      };
    formula.calculate( 100 );
    formula.sqrt( 100 );
    System.out.println( "Done" );
  }
}

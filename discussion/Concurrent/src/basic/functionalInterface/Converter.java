package basic.functionalInterface;

@FunctionalInterface
public interface Converter<F, T>
{
  T convert( F from );

  public static void main( String[] args )
  {
    Converter<String, Integer> converter = ( from ) -> Integer.valueOf( from );
    Integer converted = converter.convert( "123" );
    System.out.println( converted );

    // We can be further improved by utilizing static method reference
    converter = Integer::valueOf;
    converted = converter.convert( "567" );
    System.out.println( converted );

    //But different to anonymous objects the variable num does not have to be declared final. This code is also valid:
    int num = 1;
    Converter<Integer, String> stringConverter = ( from ) -> String.valueOf( from + num );
    stringConverter.convert( 2 ); // 3
  }
}

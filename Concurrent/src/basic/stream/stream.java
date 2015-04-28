package basic.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Foo
{
  String name;
  List<Bar> bars = new ArrayList<>();

  Foo( String name )
  {
    this.name = name;
  }
}

class Bar
{
  String name;

  Bar( String name )
  {
    this.name = name;
  }
}

public class stream
{

  static class Person
  {
    String name;
    int age;

    Person( String name, int age )
    {
      this.name = name;
      this.age = age;
    }

    @Override
    public String toString()
    {
      return name;
    }
  }

  public static void main( String[] args )
  {

    /**
     * Optionals Optionals are not functional interfaces, instead it's a nifty utility to prevent NullPointerException.
     * Optional is a simple container for a value which may be null or non-null.
     */
    Optional<String> optional = Optional.of( "bam" );
    optional.isPresent();
    optional.get();
    optional.orElse( "fallback" );
    optional.ifPresent( s -> System.out.println( s ) );

    /**
     * Stream represents a sequence of elements on which one or more operations can be performed.
     */
    List<String> stringCollection = new ArrayList<>();
    stringCollection.add( "ddd2" );
    stringCollection.add( "aaa2" );
    stringCollection.add( "bbb1" );
    stringCollection.add( "aaa1" );
    stringCollection.add( "bbb3" );
    stringCollection.add( "ccc" );
    stringCollection.add( "bbb2" );
    stringCollection.add( "ddd1" );

    stringCollection.stream();
    stringCollection.parallelStream();

    // Filter -- Filter accepts a predicate to filter all elements of the stream. 
    stringCollection.stream().filter( s -> s.startsWith( "a" ) ).forEach( System.out::println );

    // Sorted -- Sorted is an intermediate operation which returns a sorted view of the stream.The ordering of stringCollection is untouched:
    stringCollection.stream().sorted().filter( s -> s.startsWith( "a" ) ).forEach( System.out::println );

    // Map -- The intermediate operation map converts each element into another object via the given function. 
    stringCollection.stream().map( String::toUpperCase ).sorted( ( a, b ) -> b.compareTo( a ) )
        .forEach( System.out::println );

    // Match -- Various matching operations can be used to check whether a certain predicate matches the stream.
    boolean anyStartsWithA = stringCollection.stream().anyMatch( s -> s.startsWith( "a" ) );
    System.out.println( anyStartsWithA );

    boolean allStartsWithA = stringCollection.stream().allMatch( s -> s.startsWith( "a" ) );
    System.out.println( allStartsWithA );

    boolean nonStartsWithA = stringCollection.stream().noneMatch( s -> s.startsWith( "a" ) );
    System.out.println( nonStartsWithA );

    // Count -- Count is a terminal operation returning the number of elements in the stream as a long.
    long startWithA = stringCollection.stream().filter( s -> s.startsWith( "a" ) ).count();
    System.out.println( startWithA );

    /**
     * Reduce -- This terminal operation performs a reduction on the elements of the stream with the given function. The
     * result is an Optional holding the reduced value.
     */
    Optional<String> reduced = stringCollection.stream().sorted().reduce( ( s1, s2 ) -> s1 + "#" + s2 );
    reduced.ifPresent( System.out::println );

    /**
     * Collect -- Collect is an extremely useful terminal operation to transform the elements of the stream into a
     * different kind of result, e.g. a List, Set or Map.
     */

    List<Person> persons = Arrays.asList( new Person( "Max", 18 ), new Person( "Peter", 23 ),
                                          new Person( "Pamela", 23 ), new Person( "David", 12 ) );
    List<Person> filtered = persons.stream().filter( p -> p.name.startsWith( "P" ) ).collect( Collectors.toList() );
    filtered.stream().forEach( System.out::println );

    // Group by
    Map<Integer, List<Person>> personByAge = persons.stream().collect( Collectors.groupingBy( p -> p.age ) );
    personByAge.forEach( ( age, p ) -> System.out.format( "age %s: %s\n", age, p ) );

    // AveragingInt
    Double averageAge = persons.stream().collect( Collectors.averagingInt( p -> p.age ) );

    System.out.println( averageAge );

    // IntSummaryStatistics 
    IntSummaryStatistics ageSummary = persons.stream().collect( Collectors.summarizingInt( p -> p.age ) );
    System.out.println( ageSummary );

    // Join
    String phrases = persons.stream().filter( p -> p.age > 18 ).map( p -> p.name )
        .collect( Collectors.joining( " and ", "In Germany", " are of legal age." ) );

    System.out.println( phrases );

    // FlatMap
    List<Foo> foos = new ArrayList<>();
    IntStream.range( 1, 4 ).forEach( i -> foos.add( new Foo( "Foo" + i ) ) );
    foos.forEach( f -> IntStream.range( 1, 4 ).forEach( i -> f.bars.add( new Bar( "Bar" + i + "<-" + f.name ) ) ) );

    foos.stream().flatMap( f -> f.bars.stream() ).forEach( b -> System.out.println( b.name ) );

    // Reduce -- The reduction operation combines all elements of the stream into a single result
    persons.stream().reduce( ( p1, p2 ) -> p1.age > p2.age ? p1 : p2 ).ifPresent( System.out::println );

  }
}

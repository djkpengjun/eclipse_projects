package basic.functionalInterface;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BuiltInInterfaces
{
  public static void main( String[] args )
  {
    // Predicates
    Predicate<String> predicate = ( s ) -> s.length() > 0;
    predicate.test( "test" );
    predicate.negate().test( "petr" );

    Predicate<Boolean> nonNull = Objects::nonNull;
    Predicate<Boolean> isNull = Objects::isNull;
    Predicate<String> isEmpty = String::isEmpty;
    Predicate<String> nonEmpty = isEmpty.negate();

    // Functions --Functions accept one argument and produce a result. 
    Function<String, Integer> toInteger = Integer::valueOf;
    Function<String, String> backToString = toInteger.andThen( String::valueOf );
    backToString.apply( "343534" );

    // Suppliers --Suppliers produce a result of a given generic type. Suppliers don't accept arguments.
    Supplier<String> stringSupplier = String::new;
    stringSupplier.get();

    // Consumers -- Consumers represents operations to be performed on a single input argument.
    Consumer<Person> greeter = ( p ) -> System.out.println( "Hello, " + p.firstName );

    // Comparators -- Comparators are well known from older versions of Java. Java 8 adds various default methods to the interface.
    Comparator<Person> comparator = ( p1, p2 ) -> p1.firstName.compareTo( p2.firstName );
    comparator.compare( new Person( "Peter", "Peng" ), new Person( "Becky", "Hu" ) );
    comparator.reversed().compare( new Person( "Peter", "Peng" ), new Person( "Becky", "Hu" ) );
  }
}

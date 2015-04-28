package basic.functionalInterface;

public interface PersonFactory<P extends Person>
{
  P create( String firstName, String lastName );

  /**
   * We create a reference to the Person constructor via Person::new. The Java compiler automatically chooses the right
   * constructor by matching the signature of PersonFactory.create.
   * 
   * @param args
   */
  public static void main( String[] args )
  {
    PersonFactory<Person> factory = Person::new;
    Person person = factory.create( "Peter", "Peng" );
    System.out.println( person );
  }
}

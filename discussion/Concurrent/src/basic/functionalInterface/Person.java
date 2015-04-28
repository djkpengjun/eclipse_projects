package basic.functionalInterface;

class Person
{
  String firstName;
  String lastName;

  Person()
  {
  }

  Person( String firstName, String lastName )
  {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String toString()
  {
    return firstName.concat( ":" ).concat( lastName );
  }
}

package basic.datastructures;

class Node<T>
{
  Node<T> next = null;
  T data;

  public Node( T d )
  {
    data = d;
  }

  void append( T d )
  {
    Node<T> end = new Node<>( d );
    Node<T> n = this;
    while ( n.next != null )
    {
      n = n.next;
    }
    n.next = end;
  }

  Node<T> deleteNode( Node<T> head, T d )
  {
    Node<T> n = head;
    if ( n.data.equals( d ) )
    {
      return head.next;
    }
    while ( n.next != null )
    {
      if ( n.next.data.equals( d ) )
      {
        n.next = n.next.next;
        return head;
      }
    }
    return head;
  }
}

public class ConcurrentLinkedList
{

}

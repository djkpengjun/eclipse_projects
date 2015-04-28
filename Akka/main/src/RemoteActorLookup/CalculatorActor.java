package RemoteActorLookup;

import RemoteActorLookup.Ops.Substract;
import akka.actor.UntypedActor;

public class CalculatorActor extends UntypedActor
{

  @Override
  public void onReceive( Object message )
  {
    if ( message instanceof Ops.Add )
    {
      System.out.println( "Calculating Adding " );
      getSender().tell( new Ops.Add(), getSelf() );

    }
    else if ( message instanceof Substract )
    {
      System.out.println( "Calculating Substracting" );
      getSender().tell( new Ops.Substract(), getSelf() );

    }
    else
    {
      unhandled( message );
    }
  }
}

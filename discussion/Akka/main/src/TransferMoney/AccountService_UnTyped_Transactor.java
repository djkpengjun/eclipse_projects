package TransferMoney;

import static akka.pattern.Patterns.ask;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.stm.Ref;
import scala.concurrent.stm.japi.STM;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.transactor.SendTo;
import akka.transactor.UntypedTransactor;

class Deposit
{
  public final int amount;

  public Deposit( final int theAmount )
  {
    amount = theAmount;
  }
}

class Withdraw
{
  public final int amount;

  public Withdraw( final int theAmount )
  {
    amount = theAmount;
  }
}

class FetchBalance
{
}

class Balance
{
  public final int amount;

  public Balance( final int theBalance )
  {
    amount = theBalance;
  }
}

class Transfer
{
  public final ActorRef from;
  public final ActorRef to;
  public final int amount;

  public Transfer( final ActorRef fromAccount, final ActorRef toAccount, final int theAmount )
  {
    from = fromAccount;
    to = toAccount;
    amount = theAmount;
  }
}

class Account_transactor extends UntypedTransactor
{
  private final Ref.View<Integer> balance = STM.newRef( 0 );

  @Override
  public void atomically( Object message ) throws Exception
  {
    if ( message instanceof Deposit )
    {
      int amount = ( (Deposit) message ).amount;
      if ( amount > 0 )
      {
        balance.swap( balance.get() + amount );
        System.out.println( "Received Deposit request " + amount );
      }
    }
    else if ( message instanceof Withdraw )
    {
      int amount = ( (Withdraw) message ).amount;
      if ( amount > 0 && balance.get() >= amount )
      {
        balance.swap( balance.get() - amount );
        System.out.println( "Received Withdraw request " + amount );
      }
      else
      {
        System.out.println( "..Insufficient fund.." );
        throw new RuntimeException( "Insufficient fund" );
      }
    }
    else if ( message instanceof FetchBalance )
    {
      getSender().tell( balance.get(), getSelf() );
    }
  }
}

public class AccountService_UnTyped_Transactor extends UntypedTransactor
{
  @SuppressWarnings( "deprecation" )
  @Override
  public Set<SendTo> coordinate( final Object message )
  {
    if ( message instanceof Transfer )
    {
      Set<SendTo> coordinations = new HashSet<>();
      Transfer transfer = (Transfer) message;
      coordinations.add( sendTo( transfer.to, new Deposit( transfer.amount ) ) );
      coordinations.add( sendTo( transfer.from, new Withdraw( transfer.amount ) ) );
      return Collections.unmodifiableSet( coordinations );
    }
    return nobody();
  }

  @Override
  public void atomically( Object arg0 ) throws Exception
  {

  }

  public static void printBalance( final String accountName, final ActorRef account ) throws Exception
  {
    Future<Object> future = ask( account, new FetchBalance(), 5000 );
    System.out.println( accountName + "" + future.result( Duration.create( 5, TimeUnit.SECONDS ), null ) );
  }

  public static void main( String[] args ) throws Exception
  {
    final ActorSystem system = ActorSystem.create( "MySystem" );

    final ActorRef account1 = system.actorOf( Props.create( Account_transactor.class ) );
    final ActorRef account2 = system.actorOf( Props.create( Account_transactor.class ) );
    final ActorRef accountService = system.actorOf( Props.create( AccountService_UnTyped_Transactor.class ) );

    account1.tell( new Deposit( 1000 ), ActorRef.noSender() );
    account2.tell( new Deposit( 1000 ), ActorRef.noSender() );

    Thread.sleep( 1000 );

    printBalance( "account1 ", account1 );
    printBalance( "account2 ", account2 );

    System.out.println( "Let's transfer $20... should succeed" );

    accountService.tell( new Transfer( account1, account2, 20 ), ActorRef.noSender() );

    Thread.sleep( 5000 );

    printBalance( "account1 ", account1 );
    printBalance( "account2 ", account2 );

    System.out.println( "Let's transfer $2000... should not succeed" );

    accountService.tell( new Transfer( account1, account2, 2000 ), ActorRef.noSender() );

    Thread.sleep( 5000 );

    printBalance( "account1 ", account1 );
    printBalance( "account2 ", account2 );

    system.shutdown();
  }
}

package TransferMoney;

import java.util.concurrent.Callable;

import scala.concurrent.stm.Ref;
import scala.concurrent.stm.japi.STM;

public class Account_stm
{
  final Ref.View<Integer> balance = STM.newRef( 0 );

  public Account_stm( int initialBalance )
  {
    balance.swap( initialBalance );
  }

  public int getBalance()
  {
    return balance.get();
  }

  public void deposit( final int amount )
  {
    STM.atomic( new Callable<Boolean>()
      {

        @Override
        public Boolean call() throws Exception
        {
          System.out.println( "Deposit " + amount );
          if ( amount > 0 )
          {
            balance.swap( balance.get() + amount );
            return true;
          }
          throw new Exception( "Account operation failed !" );
        }

      } );
  }

  public void withdraw( final int amount )
  {
    STM.atomic( new Callable<Boolean>()
      {

        @Override
        public Boolean call() throws Exception
        {
          System.out.println( "Withdraw " + amount );
          int currentBalance = balance.get();
          if ( amount > 0 && currentBalance >= amount )
          {
            balance.swap( currentBalance - amount );
            return true;
          }
          throw new Exception( "Account operation failed !" );
        }

      } );
  }

}

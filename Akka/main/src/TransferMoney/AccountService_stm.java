package TransferMoney;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import scala.concurrent.stm.japi.STM;

public class AccountService_stm
{
  public void transfer( Account_stm from, Account_stm to, final int amount )
  {
    STM.atomic( new Callable<Boolean>()
      {

        @Override
        public Boolean call() throws Exception
        {
          System.out.println( "Attempting transfer...." );
          to.deposit( amount );
          System.out.println( "Simulating a delay in transfer...." );
          try
          {
            Thread.sleep( 5000 );
          }
          catch ( Exception e )
          {
          }
          System.out.println( "Uncommitted balance after deposit $" + to.getBalance() );
          from.withdraw( amount );
          return true;
        }

      } );
  }

  public static void transferAndPrintBalance( Account_stm from, Account_stm to, final int amount )
  {
    boolean result = true;
    try
    {
      new AccountService_stm().transfer( from, to, amount );
    }
    catch ( Exception e )
    {
      result = false;
    }
    System.out.println( "Result of transfer is " + ( result ? "Pass" : "Fail" ) );

    System.out.println( "From account has $" + from.getBalance() );

    System.out.println( "To account has $" + to.getBalance() );
  }

  public static void main( String[] args ) throws Exception
  {
    final Account_stm account1 = new Account_stm( 2000 );
    final Account_stm account2 = new Account_stm( 100 );

    final ExecutorService service = Executors.newSingleThreadExecutor();

    service.submit( new Runnable()
      {
        public void run()
        {
          try
          {
            Thread.sleep( 1000 );
          }
          catch ( Exception e )
          {
          }
          account2.deposit( 20 );
        }
      } );

    service.shutdown();

    transferAndPrintBalance( account1, account2, 500 );

    System.out.println( "Making larger transfer.." );

    transferAndPrintBalance( account1, account2, 5000 );
  }
}

package TransferMoney;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import scala.concurrent.stm.Ref;
import scala.concurrent.stm.japi.STM;

public class Portfolio
{

  final private Ref.View<Integer> checkingBalance = STM.newRef( 500 );

  final private Ref.View<Integer> savingsBalance = STM.newRef( 600 );

  public Integer getCheckingBalance()
  {
    return checkingBalance.get();
  }

  public Integer getSavingBalance()
  {
    return savingsBalance.get();
  }

  public void withdraw( final boolean fromChecking, final int amount )
  {
    STM.atomic( new Runnable()
      {

        @Override
        public void run()
        {
          final int totalBalance = checkingBalance.get() + savingsBalance.get();

          try
          {
            Thread.sleep( 1000 );
          }
          catch ( Exception e )
          {
          }

          if ( totalBalance - amount >= 1000 )
          {
            if ( fromChecking )
              checkingBalance.swap( checkingBalance.get() - amount );
            else
              savingsBalance.swap( savingsBalance.get() - amount );
          }
          else
            System.out.println( "Sorry, can't withdraw due to constraint violation!" );
        }

      } );
  }

  public static void main( String[] args ) throws InterruptedException
  {
    final Portfolio portfolio = new Portfolio();
    int checkingBalance = portfolio.getCheckingBalance();
    int savingBalance = portfolio.getSavingBalance();

    System.out.println( "Checking balance is " + checkingBalance );
    System.out.println( "Savings balance is " + savingBalance );
    System.out.println( "Total balance is " + ( checkingBalance + savingBalance ) );

    final ExecutorService service = Executors.newFixedThreadPool( 10 );

    service.execute( new Runnable()
      {

        @Override
        public void run()
        {
          portfolio.withdraw( true, 100 );
        }
      } );

    service.execute( new Runnable()
      {

        @Override
        public void run()
        {
          portfolio.withdraw( false, 100 );
        }
      } );

    Thread.sleep( 10000 );

    checkingBalance = portfolio.getCheckingBalance();

    savingBalance = portfolio.getSavingBalance();

    System.out.println( "Checking balance is " + checkingBalance );
    System.out.println( "Savings balance is " + savingBalance );
    System.out.println( "Total balance is " + ( checkingBalance + savingBalance ) );

    if ( checkingBalance + savingBalance < 1000 )
      System.out.println( "Oops, broke the constraint!" );

    service.shutdown();
  }
}

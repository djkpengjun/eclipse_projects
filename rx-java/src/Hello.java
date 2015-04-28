import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import rx.Observable;
import rx.functions.Action1;

public class Hello
{

  public static void hello( String... names )
  {
    Observable.from( names ).subscribe( new Action1<String>()
      {
        @Override
        public void call( String s )
        {
          System.out.println( "Hello " + s + "!" );
        }
      }

    );

    // An Observable is created by passing a Func1 implementation
    Observable.create( observer1 -> {
      try
      {
        observer1.onCompleted();
      }
      catch ( Exception e )
      {
        observer1.onError( e );
      }
    } );

    Observable.just( "Peter" );

  }

  /**
   * This example shows a custom Observable that blocks when subscribed to (does not spawn an extra thread).
   */
  static Observable<String> customObservableBlocking()
  {

    return Observable.create( observer1 -> {

      IntStream.range( 1, 10 ).parallel().forEach( i -> {
        observer1.onNext( "value".concat( String.valueOf( i ) ) );
      } );

      observer1.onCompleted();
    }

    );

  }

  static Observable<String> customObservableNonBlocking()
  {

    return Observable.create( observer1 -> {
      ExecutorService serive = Executors.newCachedThreadPool();
      serive.submit( new Runnable()
        {
          @Override
          public void run()
          {

            IntStream.range( 1, 10 ).forEach( i -> {
              observer1.onNext( "value".concat( String.valueOf( i ) ) );
            } );

            observer1.onCompleted();
          }
        } );

      serive.shutdown();

      ScheduledExecutorService scheduleService = Executors.newScheduledThreadPool( 1 );
      scheduleService.schedule( new Runnable()
        {
          @Override
          public void run()
          {
            serive.shutdown();
          }
        }, 5, TimeUnit.SECONDS );
      scheduleService.shutdown();
    }

    );

  }

  public static void simpleComposition()
  {
    customObservableBlocking().skip( 2 ).take( 5 ).map( stringValue -> stringValue + "_xform" )
        .subscribe( System.out::println );
  }

  public static void main( String[] args )
  {
    hello( "Peter", "Becky", "Leaf" );
    customObservableBlocking().subscribe( System.out::println );
    customObservableNonBlocking().subscribe( System.out::println );
    simpleComposition();
  }

  public static <T> Observable<T> toObservable( CompletableFuture<T> future )
  {
    return Observable.create( subscriber -> future.whenComplete( ( result, error ) -> {
      if ( error != null )
      {
        subscriber.onError( error );
      }
      else
      {
        subscriber.onNext( result );
        subscriber.onCompleted();
      }
    } ) );
  }

  public static <T> CompletableFuture<List<T>> fromObservable( Observable<T> observable )
  {
    final CompletableFuture<List<T>> future = new CompletableFuture<>();
    observable.doOnError( future::completeExceptionally ).toList().forEach( future::complete );
    return future;
  }

  public static <T> CompletableFuture<T> fromSingleObservable( Observable<T> observable )
  {
    final CompletableFuture<T> future = new CompletableFuture<>();
    observable.doOnError( future::completeExceptionally ).single().forEach( future::complete );
    return future;
  }
}

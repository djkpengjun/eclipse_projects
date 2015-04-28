package CGlib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

class MyClass
{
  public void print()
  {
    System.out.println( "I'm in MyClass.print !" );
  }
}

public class CGlib
{

  public static void main( String[] args )
  {
    Enhancer enhaancer = new Enhancer();
    enhaancer.setSuperclass( MyClass.class );
    enhaancer.setCallback( new MethodInterceptor()
      {
        @Override
        public Object intercept( Object obj, Method method, Object[] args, MethodProxy proxy ) throws Throwable
        {
          System.out.println( method + " is intercepted !" );
          proxy.invokeSuper( obj, args );
          return null;
        }
      } );

    MyClass my = (MyClass) enhaancer.create();
    my.print();
  }
}

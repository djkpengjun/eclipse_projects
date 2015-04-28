package ASM;

import java.util.HashMap;

public class DynamicClassLoader extends ClassLoader
{
  private final HashMap<String, byte[]> bytecodes = new HashMap<String, byte[]>();

  public DynamicClassLoader()
  {
    super();
  }

  public DynamicClassLoader( ClassLoader parent )
  {
    super( parent );
  }

  public void putClass( String name, byte[] bytecode )
  {
    bytecodes.put( name, bytecode );
  }

  @Override
  protected Class<?> findClass( String name ) throws ClassNotFoundException
  {
    //remove here, because the class won't be loaded another time anyway
    byte[] bytecode = bytecodes.remove( name );
    if ( bytecode != null )
    {
      return defineClass( name, bytecode, 0, bytecode.length );
    }
    else
    {
      return super.findClass( name );
    }
  }

  public static void main( String[] args ) throws Exception
  {
    DynamicClassLoader l = new DynamicClassLoader();
    l.putClass( "net.slightlymagic.asm.test.HelloWorld", AsmBuilder.HelloWorld );

    Class<?> HelloWorld = l.loadClass( "net.slightlymagic.asm.test.HelloWorld" );
    HelloWorld.getMethod( "main", String[].class ).invoke( null, (Object) args );
  }
}

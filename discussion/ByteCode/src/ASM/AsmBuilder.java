package ASM;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AsmBuilder implements Opcodes
{
  public static final byte[] HelloWorld = HelloWorld();

  private static byte[] HelloWorld()
  {
    ClassWriter cw = new ClassWriter( 0 );

    cw.visit( V1_6, ACC_PUBLIC | ACC_SUPER, "net/slightlymagic/asm/test/HelloWorld", null, "java/lang/Object", null );
    cw.visitSource( "<generated>", null );

    HelloWorld_init( cw );
    HelloWorld_main( cw );
    HelloWorld_hello( cw );

    return cw.toByteArray();
  }

  private static void HelloWorld_init( ClassWriter cw )
  {
    MethodVisitor mv = cw.visitMethod( ACC_PUBLIC, "<init>", "()V", null, null );
    mv.visitCode();
    mv.visitVarInsn( ALOAD, 0 );
    mv.visitMethodInsn( INVOKESPECIAL, "java/lang/Object", "<init>", "()V" );
    mv.visitInsn( RETURN );
    mv.visitMaxs( 1, 1 );
    mv.visitEnd();
  }

  private static void HelloWorld_main( ClassWriter cw )
  {
    MethodVisitor mv = cw.visitMethod( ACC_PUBLIC | ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null );
    mv.visitCode();
    mv.visitTypeInsn( NEW, "net/slightlymagic/asm/test/HelloWorld" );
    mv.visitInsn( DUP );
    mv.visitMethodInsn( INVOKESPECIAL, "net/slightlymagic/asm/test/HelloWorld", "<init>", "()V" );
    mv.visitMethodInsn( INVOKEVIRTUAL, "net/slightlymagic/asm/test/HelloWorld", "hello", "()V" );
    mv.visitInsn( RETURN );
    mv.visitMaxs( 2, 1 );
    mv.visitEnd();
  }

  private static void HelloWorld_hello( ClassWriter cw )
  {
    MethodVisitor mv = cw.visitMethod( ACC_PUBLIC, "hello", "()V", null, null );
    mv.visitCode();
    mv.visitFieldInsn( GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;" );
    mv.visitLdcInsn( "Hello, World" );
    mv.visitMethodInsn( INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V" );
    mv.visitInsn( RETURN );
    mv.visitMaxs( 2, 1 );
    mv.visitEnd();
  }
}

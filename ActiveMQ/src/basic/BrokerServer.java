package basic;

import java.util.ArrayList;
import java.util.List;

import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.security.AuthenticationUser;
import org.apache.activemq.security.SimpleAuthenticationPlugin;

public class BrokerServer
{
  public static final String brokerUrl = "tcp://localhost:61616";

  //  public static final String brokerUrl = "vm://localhost:61616";

  public static void main( String[] args ) throws Exception
  {
    BrokerService broker = new BrokerService();
    broker.setBrokerName( "peter" );
    broker.setDataDirectory( "data/" );

    SimpleAuthenticationPlugin authentication = new SimpleAuthenticationPlugin();
    authentication.setAnonymousAccessAllowed( true );

    List<AuthenticationUser> users = new ArrayList<>();
    users.add( new AuthenticationUser( "admin", "admin", "admins,publishers,consumers" ) );
    users.add( new AuthenticationUser( "publisher", "password", "publishers,consumers" ) );
    users.add( new AuthenticationUser( "consumer", "password", "consumers" ) );
    users.add( new AuthenticationUser( "guest", "password", "guests" ) );
    authentication.setUsers( users );
    broker.setPlugins( new BrokerPlugin[] { authentication } );
    broker.addConnector( brokerUrl );
    broker.setPersistent( false );
    broker.setUseJmx( true );
    broker.start();

    System.out.println( "broker service start!" );
    System.in.read();

    broker.stop();
  }
}

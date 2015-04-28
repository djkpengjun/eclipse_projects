package basic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Receiver
{
  private ConnectionFactory factory = null;
  private Connection connection = null;
  private Session session = null;
  private Destination destination = null;
  private MessageConsumer consumer = null;

  public Receiver()
  {

  }

  public void receiveMessage()
  {
    try
    {
      //      factory = new ActiveMQConnectionFactory( ActiveMQConnection.DEFAULT_BROKER_URL );
      //      factory = new ActiveMQConnectionFactory( BrokerServer.brokerUrl );
      factory = new ActiveMQConnectionFactory( "admin", "admin", BrokerServer.brokerUrl );
      connection = factory.createConnection();
      connection.start();
      session = connection.createSession( false, Session.AUTO_ACKNOWLEDGE );
      destination = session.createQueue( "Peter's first message queue" );
      consumer = session.createConsumer( destination );
      Message message = consumer.receive();
      if ( message instanceof TextMessage )
      {
        TextMessage text = (TextMessage) message;
        System.out.println( text );
      }
    }
    catch ( JMSException e )
    {
      e.printStackTrace();
    }
  }

  public static void main( String[] args )
  {
    Receiver receiver = new Receiver();
    receiver.receiveMessage();
  }

}

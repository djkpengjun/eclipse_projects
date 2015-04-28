package basic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender
{

  private ConnectionFactory factory = null;
  private Connection connection = null;
  private Session session = null;
  private Destination destination = null;
  private MessageProducer producer = null;

  public Sender()
  {

  }

  public void sendMessage()
  {
    try
    {
      //      factory = new ActiveMQConnectionFactory( ActiveMQConnection.DEFAULT_BROKER_URL );
      factory = new ActiveMQConnectionFactory( BrokerServer.brokerUrl );
      connection = factory.createConnection();
      connection.start();
      session = connection.createSession( false, Session.AUTO_ACKNOWLEDGE );
      destination = session.createQueue( "Peter's first message queue" );
      producer = session.createProducer( destination );
      producer.setDeliveryMode( DeliveryMode.NON_PERSISTENT );

      TextMessage message = session.createTextMessage();
      message.setText( "Hello.... This is a first message to send out" );
      producer.send( message );

      System.out.println( "Send: " + message.getText() );
    }
    catch ( JMSException e )
    {
      e.printStackTrace();
    }
  }

  public static void main( String[] args ) throws Exception
  {
    Sender sender = new Sender();
    sender.sendMessage();
  }
}

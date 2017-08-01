// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package UI;
import java.io.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public abstract class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */

	protected String command;
	
  final public static int DEFAULT_PORT = 5000;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + client);
    command = msg.toString();
    
    receive_Command(command,client);
    /*
    System.out.println(commend);
    System.out.println(commend.charAt(0));
    System.out.println(commend.charAt(1));
    System.out.println(commend.charAt(2));
    if(commend.contains("fuc"))
    	System.out.println("success");
    	*/
    //this.sendToAllClients(msg);
  }
  protected abstract void receive_Command(String command,ConnectionToClient client);
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  @Override
  protected void clientConnected(ConnectionToClient client) 
  {
	  System.out.println("Client "+client.getInetAddress()+" is connected!!");
  }
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) 
  {
	  System.out.println("Client "+client.getInetAddress()+" is disconnected!!");
  }
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
}
//End of EchoServer class

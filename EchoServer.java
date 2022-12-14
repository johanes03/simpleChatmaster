
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;


import ocsf.server.*;



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
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
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
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  @Override
  protected void clientConnected(ConnectionToClient client) {

	  String msg =  client.getInfo("loginID") + " has logged on!";
	  this.sendToAllClients(msg);
	  System.out.println("A new client has connected to the server.");
	  System.out.println
      (msg);	  
  }
  

  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  String msg =  client.getInfo("loginID") + " has logged off!";;
	  this.sendToAllClients(msg);
	  System.out.println
      (msg);	
  }
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
      String msgstr = msg.toString();
      if (msgstr.startsWith("#login")) {
          String params = msgstr.substring(6);          
              if (client.getInfo("loginID") == null) {
                  client.setInfo("loginID", params);
              } else {
                  try {
                      client.sendToClient("A login ID has already been set!");
                  } catch (IOException e) {
                  }
              }

          
      } else {
          if (client.getInfo("loginID") == null) {
              try {
                  client.sendToClient("You should set a login ID!");
                  client.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          } else {
              System.out.println("Message received: " + msg + " from #loginID " + client.getInfo("loginID"));
              this.sendToAllClients(client.getInfo("loginID") + " > " + msgstr);
          }
      }
  }
  
  
  
  public void handleMessageFromServer
  (Object msg, ConnectionToClient client)
{
  System.out.println("Message received: " + msg + " from " + client);
  this.sendToAllClients(msg);
}
    
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
  

  public void handleMessageFromServerConsole(String message) {
      if (message.startsWith("#")) {
          String[] parameters = message.split(" ");
          String command = parameters[0];
          switch (command) {
              case "#quit":
                  //closes the server and then exits it
                  try {
                      this.close();
                      
                  } catch (IOException e) {
                      System.exit(1);
                  }
                  System.exit(0);
                  break;
              case "#stop":
                  this.stopListening();
                  
                  break;
              case "#close":
                  try {
                      this.close();
                  } catch (IOException e) {
                  }
                  break;
              case "#setport":
                  if (!this.isListening() && this.getNumberOfClients() < 1) {
                      super.setPort(Integer.parseInt(parameters[2]));
                      System.out.println("Port set to " + Integer.parseInt(parameters[2]));
                  } else {
                      System.out.println("Can't do that now. Server is connected.");
                  }
                  break;
              case "#start":
                  if (!this.isListening()) {
                      try {
                          this.listen();
                      } catch (IOException e) {
                          //error listening for clients
                      }
                  } else {
                      System.out.println("We are already started and listening for clients!.");
                  }
                  break;
              case "#getport":
                  System.out.println("Current port is " + this.getPort());
                  break;
              default:
                  System.out.println("Invalid command: '" + command+ "'");
                  break;
          }
      } else {
          this.sendToAllClients("SERVER MSG> " + message);
      }
  }
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
    
  }
 

  
}
//End of EchoServer class

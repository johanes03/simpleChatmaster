// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;




import java.util.Scanner;
import common.*;
import java.io.IOException;


/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ServerConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  EchoServer server;
  
  Scanner fromConsole;
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param port The port to connect on.
   */
  public ServerConsole(int port) {
	  server = new EchoServer(port);
    try 
    {
      server.listen();           
    } 
    catch(IOException e) 
    {

    }
    fromConsole = new Scanner(System.in); 
  }
    
  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {
     String message;

     while(true) 
     {
         message = fromConsole.nextLine(); 
         server.handleMessageFromServerConsole(message);
         this.display(message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) {
      if (message.startsWith("#")) {
          return;
      }
      System.out.println("SERVER MSG> " + message);
  }



  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) {
      int port = 0; //Port to listen on

      try {
          port = Integer.parseInt(args[0]); //Get port from command line
      } catch (Throwable t) {
          port = DEFAULT_PORT; //Set port to 5555
      }

      ServerConsole serv = new ServerConsole(port);
      serv.accept();
  }
}
//End of ConsoleChat class

import java.util.Scanner;

public class UserUI extends ClientConsole{

	
	public UserUI(String host, int port) {
		super(host, port);
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String args[]){
		String host = "";
	    int port = 1234;  //The port number
	    Scanner input = new Scanner(System.in);
	    
	    System.out.print("Enter IP: ");
	    host = input.next();
	    System.out.print("Enter the port number: ");
	    port = input.nextInt();
/*	    try
	    {
	      host = args[0];
	      port = Integer.parseInt(args[1]);
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	      host = "20.20.2.226";
	    }*/
	    ClientConsole chat= new ClientConsole(host, port);
	    chat.accept();  //Wait for console data
	}
}
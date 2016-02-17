import java.net.*;
import java.awt.AWTException;
import java.io.*;
import java.util.concurrent.*;

public class HttpSrv {
	
	public static void main( String argv[] ) throws IOException, AWTException {
		// argv[0] is mysql server ip address
		// argv[1] is mysql user name
		// argv[2] is mysql password
		
		System.out.println( "Travel Around Globe version 0.1 - application started" );
		if(argv.length<3 || argv.length>3 ) {
			System.out.println("mysql server IP not set");
			System.exit(1);
		}
		ConnectionInfo.sqlHostIP=argv[0];
		Db Db = new Db(argv[1], argv[2]);
		ConnectionInfo.thisServerHostName=InetAddress.getLocalHost().getHostName();
		System.out.println("this server hostname: "+ ConnectionInfo.thisServerHostName);
		ConnectionInfo.thisServerId=Db.getThisServerId();
		System.out.println("This server database Id:"+ConnectionInfo.thisServerId);
		//ConnectionInfo.thisServerIP=InetAddress.getLocalHost().toString();
		//System.out.println("this server IP: "+ConnectionInfo.thisServerIP);
		ConnectionInfo.serverPointedToIP=Db.getServerPointedToIP();
		System.out.println("Server pointed to IP: "+ConnectionInfo.serverPointedToIP);
		
		Executor executor = Executors.newCachedThreadPool();
		//Executor executor = Executors.newFixedThreadPool(3);
		ServerSocket ss = new ServerSocket( ConnectionInfo.appPort );
		while ( true ){
			executor.execute( new HttpdConnection( ss.accept(), new Receiver(), Db ) );
		}
	 }
}

class HttpdConnection implements Runnable {
  
  Socket client;
  Receiver rcv;
  Db Db;
  
  HttpdConnection ( Socket client, Receiver rcv, Db Db ) throws SocketException {
    this.client = client;
    this.rcv= rcv;
    this.Db=Db;
    
    // the program is sometimes stacked in this point !!!!!!!!!!!!!!!!!!!!!!!!!!!!
    System.out.println( "Incoming connection accepted" );  
  }
  
  public void run() {
    
	  try {
      
    	BufferedReader in = new BufferedReader(
    			new InputStreamReader(client.getInputStream(), "8859_1" ) );
    	String request;
    	Transmitter trans = null;
    	
    	while ((request = in.readLine()) != null) {
    		System.out.println( "The message received: "+request);
    		
    		if(request!=null){ // sometimes
    			//process the incoming message
    			rcv.processMsg( request, Db );
    			
    			//send the message to another server
    			//comment these rows if the code is used for a last server
    			trans= new Transmitter();
    			trans.sendString(request);
    			
    		}
    		else{
    			System.out.println( "http: null received???? " );
    		}
    	}
    	if(trans!=null)trans.close();
    	client.close();
    	System.out.println( "The session is closed");
	  } catch ( IOException e ) {
		  System.out.println( "I/O error " + e ); } catch (AWTException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
  }
   
}

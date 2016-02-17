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
		
		//Executor executor = Executors.newCachedThreadPool();
		Executor executor = Executors.newFixedThreadPool(3);
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
    System.out.println( "Incoming connection accepted" );
  }
  
  public void run() {
    
	  try {
      
    	BufferedReader in = new BufferedReader(
        new InputStreamReader(client.getInputStream(), "8859_1" ) );
    	OutputStream out = client.getOutputStream();
    	PrintWriter pout = new PrintWriter(new OutputStreamWriter(out, "8859_1"), true );
    	String request;
    	while ((request = in.readLine()) != null) {
    		System.out.println( "The message received: "+request);
    		pout.println( "I am server, I received: " +request);
      
    		if(request!=null){ // sometimes
    			rcv.processMsg( request, Db );
    		}
    		else{
    			System.out.println( "http: null received???? " );
    		}
    	}
    	client.close();
    	System.out.println( "The session is closed");
	  } catch ( IOException e ) {
		  System.out.println( "I/O error " + e ); } catch (AWTException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
  }
   
}

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class Transmitter {
	
	public static InetAddress serverIp;
	int port; 
	Socket server;
	PrintWriter pout;
	
	Transmitter(){
		
			
		try {
			serverIp = InetAddress.getByName(ConnectionInfo.serverPointedToIP);
			port = ConnectionInfo.appPort;
			server = new Socket( serverIp, ConnectionInfo.appPort );
			System.out.println( "Connected to: " + serverIp +":"+port); 
			pout = new PrintWriter( 
				new BufferedWriter( new OutputStreamWriter(
						server.getOutputStream())), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void sendString(String s){
		pout.println(s);
		System.out.println("message sent to: "+ ConnectionInfo.serverPointedToIP);
	}
	
	public void close(){
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

import java.awt.AWTException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class MsgSender {
	public static void main( String argv[] ) throws IOException, AWTException {
		String messageId="2272";
		new MsgSender(messageId);
	}
	
	MsgSender(String messageId){
		Socket server;
		try {
			//server = new Socket( "130.211.105.151", 81 );
			server = new Socket( "127.0.0.1", 81 );
		System.out.println( "Client is Connected to the server"); 
	
		PrintWriter pout;
		pout = new PrintWriter( 
				new BufferedWriter( new OutputStreamWriter(
						server.getOutputStream())), true);
		pout.println("messageId="+messageId+"&information=testiiiiing");
		
		for(int i=4011;i<4020;i++){
			pout.println("messageId="+i+"&information=testiiiiing");
			System.out.println(i);
			/*
			try {
			    Thread.sleep(1000);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			*/
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

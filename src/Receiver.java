import java.awt.AWTException;


public class Receiver {
	
	String [] msg_members;
	String [] msg_members_types;
	int thisServerId;
	
	public Receiver() throws AWTException{
			msg_members = new String[Message.member.values().length];
			msg_members_types = new String[Message.member.values().length];			
			for (int i=0; i < Message.member.values().length ; i++) {
				msg_members[i]=Message.member.values()[i].toString();
				msg_members_types[i]=Message.member.values()[i].type;
			}
			setThisServerId(ConnectionInfo.thisServerId);
	}
			
	public void processMsg(String str_received, Db Db) throws AWTException{
		
		if(check_roughly_data( str_received , msg_members) == true){
			Message msg= new Message( str_received );
			
			System.out.println("message format OK");
			//System.out.println( new java.util.Date().toString());
		    
			//safe time to sql
			Db.logMessageTimes(msg.getMessageId(), thisServerId,
					msg.getTimeReceived(), java.lang.System.currentTimeMillis());
			
			//send the message to another server
			//comment this if last server
			//Transmitter trans= new Transmitter();
			//trans.sendString(str_received);
			//trans.close();
		}
		
		else{
			System.out.println("Data not correct");
			//save to the table for rubbish
		}	
	}
	
	private boolean check_roughly_data( String str, String [] msg_members){
		int counter=0;
		
		for(String current_member : msg_members){
			if(str.contains(current_member))counter++;
		}
		if (counter<2) return false;  // the message must contain at least 3 key words
		else return true;
		//check if consist once each keyword
		//check if values are valid
		//check if format(contact id / SIA) is valid
	}
	
	void setThisServerId(int serverId){
		this.thisServerId=serverId;
	}
}

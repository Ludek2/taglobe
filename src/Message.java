

public class Message {
	
	public enum member{
		messageId("INT"), information("STRING");;
		
		public String type;
		
		private member(String type){
			this.type=type;
		}
	}
	
	private int messageId;
	private String information; 
	
	private long timeReceived;
	
	public Message(String received_string){
		
		timeReceived=java.lang.System.currentTimeMillis();
		String [] devided_str = received_string.split("\\s*&\\s*");
		
		for(int i=0; i<devided_str.length; i++){
			
			if(devided_str[i].contains("messageId")) 
				setMessageId( Integer.parseInt(devided_str[i].replaceAll("[messageId=]", "")));
			if(devided_str[i].contains("information")) 
				setInformation( devided_str[i].replaceAll("[information=]", ""));
		}
		System.out.println("messageId:"+ messageId);
		System.out.println("information:"+ information);
		
	}
	
	private void setMessageId(int messageId){
		this.messageId=messageId;
	}
	
	private void setInformation(String information){
		this.information=information;
	}

	public int getMessageId(){
		return messageId;
	}
	
	public long getTimeReceived(){
		return timeReceived;
	}
}

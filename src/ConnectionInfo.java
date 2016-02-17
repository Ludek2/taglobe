


public class ConnectionInfo {
	
	public static int thisServerId;
	public static String sqlHostIP;
	public static String thisServerHostName;
	public static String thisServerIP;
	public static String sqlDatabase = "TravelAroundGlobe"; 
	public static String sqlUser;
	public static String sqlPassword;	
	
	public static int serverPointedToId;
	public static String serverPointedToIP;
	public static int appPort = 81;
	//sendTo server
	
	ConnectionInfo(){	
		//setSendToServer();
	}
	
	void setSendToServer(){
		//elicit ip from database	
	}
}

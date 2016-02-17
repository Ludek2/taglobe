import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Db {
	
	private String hostIP = ConnectionInfo.sqlHostIP;
	private String dbName = ConnectionInfo.sqlDatabase;
	private String dbUser;
	private String dbPass;
	
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	Db(String userName, String pass){
		this.dbUser=userName;
		this.dbPass=pass;
		
		// This will load the MySQL driver, each DB has its own driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		// Setup the connection with the DB
	    try {
			connect = DriverManager
			   .getConnection("jdbc:mysql://"+ hostIP +"/"+dbName+"?"
			        + "user="+dbUser+"&password="+dbPass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/*
	The method saves times into the table:
	TimeLog: serverId INT, messageId INT, timeReceived BIGINT, timeSend BIGINT
	*/
	public void logMessageTimes(int messageId, int thisServerId, long timeRcv, long timeSent){
		String tableName = "TimeLog";
		try {
			preparedStatement = connect
			          .prepareStatement("insert into  "+dbName+"."+tableName+" values (?, ?, ?, ? )");
			preparedStatement.setInt(1, messageId);
		    preparedStatement.setInt(2, thisServerId);
		    //	setTime(int parameterIndex, Time x) 
		    preparedStatement.setLong(3, timeRcv); 
		    preparedStatement.setLong(4, timeSent);
		    preparedStatement.setQueryTimeout(10);
		    preparedStatement.executeUpdate();
		    
		    System.out.println("messageId:"+ messageId+" timeReceived:"+
		    		timeRcv + " timeSent:"+ timeSent);
		    System.out.println("times successfully saved to the database");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public int getThisServerId(){
		int id=-1; // if fails to read id, id will be -1
		try {
			statement = connect.createStatement();
			String query="SELECT serverId  FROM Servers " +
					"WHERE hostName='" + ConnectionInfo.thisServerHostName+"'";
			resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
                id=Integer.parseInt(resultSet.getString(1));
            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
	
	public int getServerPointedToId(){
		int id=-1;
		try {
			statement = connect.createStatement();
			String query="SELECT pointsToServerId  FROM Servers " +
					"WHERE serverId='" + ConnectionInfo.thisServerId+"'";
			resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
                id=Integer.parseInt(resultSet.getString(1));
            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
	
	public String getServerPointedToIP(){
		int serverPointedToId=getServerPointedToId();
		String ip="";
		try {
			statement = connect.createStatement();
			String query="SELECT ip  FROM Servers " +
					"WHERE serverId='" + serverPointedToId +"'";
			resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
                ip=resultSet.getString(1);
            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ip;
	}
	
	private void close() {
	    try {
	      if (resultSet != null) {
	        resultSet.close();
	      }
	      if (statement != null) {
	        statement.close();
	      }
	      if (connect != null) {
	        connect.close();
	      }
	    } catch (Exception e) {
	    }
	  }
}

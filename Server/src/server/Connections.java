package server;

import java.util.HashMap;
import java.util.Map;

import com.sun.media.jfxmedia.logging.Logger;

import server.CalendarServer.ClientConnection;


public class Connections {
	static Map<String, ClientConnection> clients;
	
	public Connections(){
		clients = new HashMap<>();
	}
	
	public static void addConnection(ClientConnection connection){
		String username = connection.username;
		
		if(clients.containsKey(username)){
			return;
		}
		
		clients.put(username, connection);
		Logger.logMsg(Logger.DEBUG, "Connection registerd: " + username);
	}
	
	public static ClientConnection getConnection(String username){
		if(clients.containsKey(username)){
			return clients.get(username);
		}
		
		return null;
	}
	
	public static void disconnect(String username){
		clients.remove(username);
	}

}

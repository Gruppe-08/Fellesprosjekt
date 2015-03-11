package server;

import java.util.HashMap;
import java.util.Map;

import server.CalendarServer.ClientConnection;


public class Connections {
	static Map<String, ClientConnection> clients = new HashMap<>();
	
	public static void addConnection(ClientConnection connection){
		String username = connection.username;
		
		if(clients.containsKey(username)){
			return;
		}
		
		clients.put(username, connection);
	}
	
	public static ClientConnection getConnection(String username){
		if(isConnected(username)){
			return clients.get(username);
		}
		
		return null;
	}
	
	public static void disconnect(String username){
		clients.remove(username);
	}
	
	public static boolean isConnected(String username){
		return clients.containsKey(username);
	}

}

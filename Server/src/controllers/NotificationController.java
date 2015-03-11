package controllers;

import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.esotericsoftware.kryonet.rmi.RemoteObject;

import rmi.NotificationCtrl;
import server.CalendarServer.ClientConnection;
import server.Connections;

public class NotificationController {
	
	public static void notify(String username, Object object){
		ClientConnection conn = Connections.getConnection(username);
		
		NotificationCtrl not = ObjectSpace.getRemoteObject(conn, 1, NotificationCtrl.class);
	    ((RemoteObject)not).setNonBlocking(true);
	    
	    System.out.println("Client should say hello");
	    not.sayHello();
	    System.out.println("Client said hello");
	}

}

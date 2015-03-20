package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;

import communication.responses.AuthenticationResponse;

public class ConnectionController extends Client {
	ArrayList<Object> messageStack = new ArrayList<Object>();
	
	public ConnectionController(String host, int port) throws IOException {
		super(8192, 8192);
		this.start();
		this.connect(5000, host, port);
		
		this.addListener(new ConnectionListener());
	}
	
	class ConnectionListener extends Listener {
		public void received (Connection connection, Object object) {
			if (object instanceof FrameworkMessage) {
				
			}
			else {
				addObject(object);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public Object sendRequest(Object request, Class responseClass) {
		sendTCP(request);
		return getObject(responseClass.getCanonicalName());
	}
	
	public synchronized void addObject(Object object) {
		messageStack.add(object);
		notify();
	}
	
	public synchronized Object getObject(String classname) {
		Boolean gotObject = false;
		Object objectToGet = null;
		while (!gotObject) {
			if (messageStack.isEmpty()) {
				try {
					wait();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			Iterator<Object> iter = messageStack.iterator();
			while (iter.hasNext()) {
				Object obj = iter.next();
				try {
					if (Class.forName(classname).isInstance(obj)) {
						objectToGet = obj;
					gotObject = true;
					iter.remove();
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return objectToGet;
	} 
}

package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import calendar.State;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import communication.responses.AuthenticationResponse;

public class ConnectionController extends Client {
	public final static short NOTIFICATION = 1;
	ArrayList<Object> messageStack = new ArrayList<Object>();
	
	public ConnectionController(String host, int port) throws IOException {
		this.start();
		this.connect(5000, host, port);
		this.addListener(new Listener(){
					public void received (Connection connection, Object object) {
						if(object instanceof AuthenticationResponse && State.connection == null){
							State.connection = connection;
							addObject(object);
						} else if (object instanceof FrameworkMessage) {
							
						}
						else {
							addObject(object);
						}
					}
		});
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

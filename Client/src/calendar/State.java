package calendar;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

import javafx.stage.Stage;
import controllers.ConnectionController;
import controllers.NotificationController;
import controllers.WindowController;
import models.User;

public class State {
	public static WindowController myWindowController = null;
	public static ConnectionController myConnectionController = null;
	public static Calendar myCalendar = null;
	public static User user = null;
	public static Stage stage = null;
	public static NotificationController notificationController = null;
	public static Connection connecton = null;
	public static ObjectSpace objectSpace = null;
	public static Connection connection;
	
	public static Stage getStage() {
		return stage;
	}

	public static void setStage(Stage stage) {
		State.stage = stage;
	}

	public static void setWindowController(WindowController myWindowController) {
		State.myWindowController = myWindowController;
	}

	public static ConnectionController getConnectionController() {
		return myConnectionController;
	}

	public static void setConnectionController(
			ConnectionController myConnectionController) {
		State.myConnectionController = myConnectionController;
	}

	public static Calendar getCalendar() {
		return myCalendar;
	}

	public static void setCalendar(Calendar myCalendar) {
		State.myCalendar = myCalendar;
	}
	
	public static void setUser(User user){
		State.user = user;
	}
	
	public static User getUser(){
		return user;
	}
	
	public static WindowController getWindowController() {
		return myWindowController;
	}

}

package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sun.media.jfxmedia.logging.Logger;
import communication.responses.NotificationResponse;

import models.Appointment;
import models.Notification;
import server.DatabaseConnector;

public class NotificationController {
	private static Connection db;
	private static PreparedStatement statement;
	private static ResultSet res;
	
	public static NotificationResponse getNotificationResponse(String username){
		NotificationResponse response = new NotificationResponse();
		try {
			response.setNotifications(getNotifications(username));
		} catch (SQLException e){
			Logger.logMsg(Logger.ERROR, "getNotificationResponse generated exception: " + e.getMessage());
		}
		
		return response;
	}
	
	private static void addNotification(Notification not) throws SQLException{
		db = DatabaseConnector.getDB();
		
		String isAlarm = not.isAlarm() ? "1" : "0";
		String type = not.getType().toString().toLowerCase();
		
		String addNotification = String.format("INSERT INTO Notification (type, message, created, is_alarm, aopointment_id, username, triggerdate) "
				+ "VALUES (%s, %s, %s, %s, %s, %s, %s)",
				 type, not.getMessage(), not.getCreated(), isAlarm, not.getAppointment().getId(), not.getUsername(), not.getTriggerDate());
		statement = db.prepareStatement(addNotification);
		statement.execute();
	}
	
	private static ArrayList<Notification> getNotifications(String username) throws SQLException{
		db = DatabaseConnector.getDB();
		ArrayList<Notification> notifications = new ArrayList<>();
		
		String getNotifications = String.format("SELECT * FROM Notification NATURAL JOIN Appointment WHERE username='%s'", username);	
		statement = db.prepareStatement(getNotifications);
		statement.execute();
		res = statement.getResultSet();
		
		Appointment app;
		Notification not;
		
		while(res.next()){
			app = AppointmentController.parseResultSetToAppointment(res);
			
			not = parseResultSetToNotification(res);
			not.setAppointment(app);
			
			notifications.add(not);
		}
		
//		String deleteNotifications = String.format("DELETE FROM Notification WHERE username='%s'", username);
//		statement = db.prepareStatement(deleteNotifications);
//		statement.execute();
		
		return notifications;
	}
	
	public static Notification parseResultSetToNotification(ResultSet res) throws SQLException{
		Notification not = new Notification();
		not.setMessage(res.getString("message"));
		not.setCreated(res.getString("created").substring(0,16));
		not.setAlarm(res.getInt("is_alarm"));
		not.setTriggerDate(res.getString("trigger_date"));
		not.setNotificationType(res.getString("type"));
		not.setUsername(res.getString("username"));
		
		return not;
	}

}

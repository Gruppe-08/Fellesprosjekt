package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Appointment;
import models.Notification;
import server.DatabaseConnector;

public class NotificationController {
	private static Connection db;
	private static PreparedStatement statement;
	private static ResultSet res;
	
	public static void addNotification(Notification not) throws SQLException{
		db = DatabaseConnector.getDB();
		
		String isAlarm = not.isAlarm() ? "1" : "0";
		String type = not.getType().toString().toLowerCase();
		
		String addNotification = String.format("INSERT INTO Notification (type, message, created, is_alarm, aopointment_id, username, triggerdate) "
				+ "VALUES (%s, %s, %s, %s, %s, %s, %s)",
				 type, not.getMessage(), not.getCreated(), isAlarm, not.getAppointment().getId(), not.getUsername(), not.getTriggerDate());
		statement = db.prepareStatement(addNotification);
		statement.execute();
	}
	
	public static List<Notification> getNotifications(String username) throws SQLException{
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
			
			System.out.println(app.toString() +  " " + not.toString());
		}
		return notifications;
	}
	
	public static Notification parseResultSetToNotification(ResultSet res) throws SQLException{
		Notification not = new Notification();
		not.setMessage(res.getString("message"));
		not.setCreated(res.getString("created"));
		not.setAlarm(res.getInt("is_alarm"));
		not.setTriggerDate(res.getString("trigger_date"));
		not.setNotificationType(res.getString("type"));
		not.setUsername(res.getString("username"));
		
		return not;
	}

}

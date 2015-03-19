package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.sun.media.jfxmedia.logging.Logger;

import communication.responses.NotificationResponse;
import models.Appointment;
import models.Notification;
import models.NotificationType;
import server.DatabaseConnector;
import util.DateUtil;

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
	
	public static void setReadNotification(int notificationId) {
		String queryString = String.format("UPDATE `Notification` SET `read`=1 WHERE `notification_id` = %s", notificationId);
		try {
			statement = db.prepareStatement(queryString);
			statement.execute();
		} catch (SQLException e) {
			Logger.logMsg(Logger.ERROR, "Could not set notification to read: " + e.getMessage());
		}
	}
	
	public static void setStatus(int appointmentId, String username, String status) {
		String statusString = null;
	
		switch(status) {
			case "not_attending": statusString = "not attending";
					break;
			case "attending": statusString = "attending";
					break;
			default: throw new IllegalArgumentException("Not a valid status");
		}

		String queryString = String.format("UPDATE UserAppointmentRelation SET status='%s' WHERE appointment_id=%s AND username ='%s'", 
		status, appointmentId, username);
		try {
			statement = db.prepareStatement(queryString);
			statement.execute();
		} catch (SQLException e) {
			Logger.logMsg(Logger.ERROR, "Could not set status: " + e.getMessage());
		}
		
		createUpdateNotification(appointmentId, username, statusString);
	}

	private static void createUpdateNotification(int appointmentId,
			String username, String statusString) {
		String timeString;
		Appointment appointment;
		try {
			appointment = AppointmentController.getAppointment(appointmentId);
			timeString  = DateUtil.presentString(appointment.getStartTime());
			
			String notificationString = String.format("User %s responded: %s to appointment %s at %s", username, statusString, appointment.getTitle(), timeString);
			
			String timeCreated = DateUtil.serializeDateTime(LocalDateTime.now());
			
			Notification notification = new Notification(NotificationType.USER, notificationString, timeCreated);
			notification.setUsername(appointment.getOwnerUsername());
			notification.setStatus("pending");
			notification.setTriggerDate(timeCreated);
			notification.setAppointment(appointment);
			
			addNotification(notification);
		} catch (SQLException e) {
			Logger.logMsg(Logger.ERROR, "Failed to set notification on a user updating status on an appointment. The exception was: " + e.getMessage());
		}
	}
	
	private static void addNotification(Notification not) throws SQLException{		
		String addNotification = String.format("INSERT INTO Notification (type, message, created, is_alarm, appointment_id, username, trigger_date) "
				+ "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s')",
				not.getType().toString().toLowerCase(),
				 not.getMessage(), 
				 not.getCreated(), 
				 not.isAlarm() ? "1" : "0",
				 not.getAppointment().getId(), 
				 not.getUsername(), 
				 not.getTriggerDate());
		System.out.println(addNotification);
		
		statement = db.prepareStatement(addNotification);
		statement.execute();
	}
	
	private static ArrayList<Notification> getNotifications(String username) throws SQLException{
		db = DatabaseConnector.getDB();
		ArrayList<Notification> notifications = new ArrayList<>();
		
		String getNotifications = String.format("SELECT n.*, a.*, ua.status "+
			"FROM Notification n, Appointment a, UserAppointmentRelation ua " +
			"WHERE n.username = ua.username "  + 
			"AND a.appointment_id = ua.appointment_id " + 
			"AND ua.appointment_id = n.appointment_id " + 
			"AND n.username =  '%s'", username);	
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
		
		return notifications;
	}
	
	public static Notification parseResultSetToNotification(ResultSet res) throws SQLException{
		Notification not = new Notification();
		not.setId(res.getInt("notification_id"));
		not.setMessage(res.getString("message"));
		not.setCreated(res.getString("created").substring(0,16));
		not.setAlarm(res.getInt("is_alarm"));
		not.setTriggerDate(res.getString("trigger_date"));
		not.setNotificationType(res.getString("type"));
		not.setUsername(res.getString("username"));
		not.setRead(Integer.valueOf(res.getString("read")));
		not.setStatus(res.getString("status"));
		return not;
	}

}

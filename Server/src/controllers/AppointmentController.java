package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.sun.media.jfxmedia.logging.Logger;

import communication.requests.AppointmentRequest;
import communication.requests.ChangeAppointmentStatusRequest;
import communication.requests.DeleteAppointmentRequest;
import communication.requests.PutAppointmentRequest;
import communication.responses.AppointmentResponse;
import communication.responses.BaseResponse;
import communication.responses.PutAppointmentResponse;
import server.DatabaseConnector;
import models.Appointment;
import models.Group;
import util.DateUtil;

public class AppointmentController {
	private static Connection db = DatabaseConnector.getDB();
	
	public static AppointmentResponse handleAppointmentRequest(
			AppointmentRequest request) {
		AppointmentResponse response = new AppointmentResponse();
		
		try {
			ArrayList<Appointment> loadedAppointments;
			loadedAppointments = AppointmentController.getAppointmentsByGroups(
					request.getGroupIDs());
			for(Appointment appointment : loadedAppointments) {
				response.addAppointment(appointment);
			}
			loadedAppointments = AppointmentController.getAppointmentsByUsers(
					request.getUsernames());
			for(Appointment appointment : loadedAppointments) {
				response.addAppointment(appointment);
			}
		}
		catch(SQLException e) {
			Logger.logMsg(Logger.ERROR, "HandleAppointmentRequest generated exception: " + e.getMessage());
		}
		
		return response;
	}

	public static BaseResponse handleStatusChangeRequest(ChangeAppointmentStatusRequest request) {
		
		String username = request.getUsername();
		int appointmentId = request.getAppointmentID();
		String status = request.getStatus();
		BaseResponse response = new BaseResponse();
		
		try {
			String query;
			if(request.isGroup()) {
				query = "UPDATE AppointmentGroupRelation SET status = '" + status + "' " + 
						"WHERE appointment_id = " + appointmentId + " AND group_id = " + username;
			}
			else {
				query = "UPDATE UserAppointmentRelation SET status = '" + status + "' " + 
						"WHERE appointment_id = " + request.getAppointmentID() + " AND username = '" + request.getUsername() + "'";
			}
			db.prepareStatement(query).execute();
			
			createUpdateNotification(appointmentId, username, statusString);
		} catch (SQLException e) {
			Logger.logMsg(Logger.ERROR, "Error when changing user appointment status: " + e);
			response.setErrorMessage("Internal error: Failed to change your status");
			response.setSuccessful(false);
		}
		
		
		
		return response;
	}
	
	public static BaseResponse handleDeleteAppointment(DeleteAppointmentRequest request){
		BaseResponse res = new BaseResponse();
		Logger.logMsg(Logger.DEBUG, "Handeling deleteAppointment: " + request.toString());

		try {
			deleteAppointment(request.getAppointmentId());
			res.setSuccessful(true);
		} catch(SQLException e){
			res.setErrorMessage("Could not delete this appointment.");
			res.setSuccessful(false);
			Logger.logMsg(Logger.ERROR, "Error when deleting an appointment: " + e);
		}
		
		return res;
	}
	
	public static PutAppointmentResponse handlePutAppointmentRequest( String clientUsername,
			PutAppointmentRequest request) {
		PutAppointmentResponse response = new PutAppointmentResponse();
		
		String requestUsername = request.getAppointment().getOwnerUsername();
		if(!isEqual(clientUsername, requestUsername)) {
			Logger.logMsg(Logger.ERROR, String.format("HandlePutAppointmentRequest : Expected user '%s', got '%s'", clientUsername, request.getAppointment().getOwnerUsername()));
			response.setSuccessful(false);
			response.setErrorMessage("You cannot change an appointment you do not own");
			return response;
		}
		
		if(request.isNewAppointment()) {
			try {
				int apppointmentId = addAppointment(request.getAppointment());
				request.getAppointment().setId(apppointmentId);
				response.setAppointment(request.getAppointment());
				response.setSuccessful(true);
			} catch (SQLException e) {
				Logger.logMsg(Logger.ERROR, "handlePutAppointmentRequest generated exception: " + e.getMessage());
				response.setSuccessful(false);
				response.setErrorMessage("Internal error");
			}
		}
		else {
			try {
				changeAppointment(request.getAppointment()); 
				response.setSuccessful(true);
				}
			catch (IllegalArgumentException e) {
				response.setSuccessful(false);
				response.setErrorMessage("The appointment no longer exists");
			}
			catch (SQLException e) {
				Logger.logMsg(Logger.ERROR, "handlePutAppointmentRequest generated exception: " + e.getMessage());
				response.setSuccessful(false);
				response.setErrorMessage("Internal error");
			}
		}
		
		return response;
	}

	private static boolean isEqual(String clientUsername, String requestUsername) {
		return clientUsername.equals(requestUsername);
	}
	
	private static ArrayList<Appointment> getAppointmentsByGroups(ArrayList<Integer> groupIDs)
			throws SQLException {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		ResultSet resultSet;
		
		if(groupIDs.size() == 0)
			return new ArrayList<Appointment>();

		Statement statement = db.createStatement();

		String query = "";
		//SELECT
		query += "SELECT * " +
		//FROM
		"FROM Appointment a, UserGroup g, AppointmentGroupRelation ag";
		//WHERE
		query += "WHERE (";
		// This makes sure the OR doesn't get added before the first line
		for(int group_id : groupIDs)
			query += "g.group_id = " + group_id + " OR ";
		query = query.substring(0, query.length()-4); //Strip last OR
		query += ") ";
		query += "AND a.appointment_id = ag.appointment_id " +
		"AND g.group_id = ag.group_id";
		
		//Executes and returns
		resultSet= statement.executeQuery(query);
		while(resultSet.next()) {
			Appointment appointment = parseResultSetToAppointment(resultSet);
			appointments.add(appointment);
			
		}

		return appointments; 
	}
	
	private static ArrayList<Appointment> getAppointmentsByUsers(ArrayList<String> usernames)
			throws SQLException {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		ResultSet resultSet;
		
		if(usernames.size() == 0)
			return new ArrayList<Appointment>();

		Statement statement = db.createStatement();

		String query = "SELECT * FROM  `Appointment`NATURAL LEFT JOIN UserAppointmentRelation WHERE ";

		for(String username : usernames)
			query += String.format("(username =  '%s' OR owner_username =  '%s') OR ", username, username);
		query = query.substring(0, query.length()-4); //Strip last OR
		query += "ORDER BY start_date ASC";
		//Executes and returns
		resultSet= statement.executeQuery(query);
		while(resultSet.next()){
			Appointment appointment = parseResultSetToAppointment(resultSet);
			appointments.add(appointment);
		}
				
		return appointments; 
	}
	
	private static void changeAppointment(Appointment appointment)
			throws IllegalArgumentException, SQLException {
		Logger.logMsg(Logger.DEBUG, "Updating appointment " + appointment.getTitle());
		getAppointment(appointment.getId()); // This will throw IllegalArgumentException if non-existent id

		String query = 
				"UPDATE Appointment " +
				"SET " +
				"start_date = '" + appointment.getStartTime().toString() + "'," +
				"end_date = '" + appointment.getEndTime().toString() + "'," + 
				"title = '" + appointment.getTitle() + "'," + 
				"description = '" + appointment.getDescription() + "'," + 
				"location = " + (appointment.getLocation() == null ? "null," : "'" + appointment.getLocation() + "',") + 
				"room_id = " + appointment.getRoomId() + ", " +
				"owner_username = '" + appointment.getOwnerUsername() + "' " +
				"WHERE appointment_id=" + appointment.getId() + ";";
		
		PreparedStatement statement = db.prepareStatement(query);
		statement.execute();
	}
	
	private static int addAppointment(Appointment appointment) throws SQLException {
		Logger.logMsg(Logger.DEBUG, "Adding appointment " + appointment.getTitle());
		PreparedStatement statement;
		ResultSet res;
		
		String query = 
				"INSERT INTO Appointment(start_date, end_date, title, description, location, room_id, owner_username)" +
				"VALUES ( " +
				"'" + appointment.getStartTime() + "'," +
				"'" + appointment.getEndTime() + "'," + 
				"'" + appointment.getTitle() + "'," + 
				"'" + appointment.getDescription() + "'," + 
				(appointment.getLocation() == null ? "null," : "'" + appointment.getLocation() + "',") + 
				appointment.getRoomId() + ", " +
				"'" + appointment.getOwnerUsername() + "');";
		statement = db.prepareStatement(query,  Statement.RETURN_GENERATED_KEYS);
		statement.execute();
		res = statement.getGeneratedKeys();
		
		int appointmentId;
		if (res.next()) {
			appointmentId = res.getInt(1);

			for(Entry<String, String> userRelation : appointment.getUserRelations().entrySet()) {
				query = String.format(
						"INSERT INTO UserAppointmentRelation(appointment_id, username, status) VALUES('%s', '%s', '%s')",
						appointmentId, userRelation.getKey(), userRelation.getValue());
				statement = db.prepareStatement(query);
				statement.execute();
				query = String.format(
						"INSERT INTO Notification(type, message, created, is_alarm, appointment_id, username, trigger_date) VALUES('%s', '%s', '%s','%s','%s','%s','%s')",
						"appointment", "You have been invited to an appointment", DateUtil.getNow(), 0, appointmentId, userRelation.getKey(), DateUtil.getNow());
				statement = db.prepareStatement(query);
				statement.execute();
			}
		}
		else
			throw new SQLException("Unable to get id of generated object");
		return appointmentId;
	}
	
	private static ArrayList<Appointment> getAppointments() {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		ResultSet resultSet;
		
		try {
			db = DatabaseConnector.getDB();
			Statement  statement = db.createStatement();
			
			String getAppointments = String.format("SELECT * FROM Appointment");
			resultSet = statement.executeQuery(getAppointments);
			
			while(resultSet.next()){
				Appointment appointment = parseResultSetToAppointment(resultSet);
				appointments.add(appointment);	
			}
		} catch(Exception e){
			Logger.logMsg(Logger.ERROR, e.toString());
		}
		
		return appointments;
	}
	
	protected static Appointment getAppointment(int appointmentId) throws SQLException {
		Appointment appointment;
		
		Statement statement = db.createStatement();
		
		String getAppointment = String.format("SELECT * FROM Appointment WHERE appointment_id='%s';", appointmentId);
		ResultSet resultSet = statement.executeQuery(getAppointment);
		
		if(resultSet.next())
			appointment = parseResultSetToAppointment(resultSet);
		else
			throw new IllegalArgumentException();
	
		return appointment;
	}
	
	private static void deleteAppointment(int appointmentId) throws SQLException{
			Statement statement = db.createStatement();
			String deleteAppointment = String.format("DELETE FROM Appointment WHERE appointment_id='%s'", appointmentId);
			statement.execute(deleteAppointment);
	}
	
	public static Appointment parseResultSetToAppointment(ResultSet resultSet)
			throws SQLException {
		Appointment appointment = new Appointment();
		appointment.setId(resultSet.getInt("appointment_id"));
		appointment.setStartTime(resultSet.getString("start_date").substring(0, 16));
		appointment.setEndTime(resultSet.getString("end_date").substring(0, 16));
		appointment.setTitle(resultSet.getString("title"));
		appointment.setDescription(resultSet.getString("description"));
		appointment.setRoomId(resultSet.getInt("room_id"));
		if(resultSet.wasNull()) appointment.setRoomId(null);
		appointment.setOwnerUsername(resultSet.getString("owner_username"));
		appointment.setLocation(resultSet.getString("location"));
		
		//Set location to room name if room is location
		if(appointment.getRoomId() != null) {
			String query = String.format("SELECT r.name FROM Room r WHERE r.room_id = %s;", appointment.getRoomId());
			ResultSet res = db.prepareStatement(query).executeQuery();
			
			if(res.next())
				appointment.setLocation(res.getString("name"));
			else appointment.setLocation("");
		}
		
		//Get attending users
		ResultSet users = db.createStatement().executeQuery(
				"SELECT * FROM UserAppointmentRelation ua "
				+ "WHERE ua.appointment_id = " + appointment.getId() + ";");
		while(users.next()) {
			appointment.getUserRelations().put(users.getString("username"), users.getString("status"));
		}
		
		//Get attending groups
				ResultSet groups = db.createStatement().executeQuery(
						"SELECT ag.* FROM GroupAppointmentRelation ag "
						+ "WHERE ag.appointment_id = " + appointment.getId() + ";");
				while(users.next()) {
					appointment.getGroupRelations().put(groups.getInt("group_id"), groups.getString("status"));
				}
		return appointment;
	}
}

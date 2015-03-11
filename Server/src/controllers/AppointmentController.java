package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.sun.media.jfxmedia.logging.Logger;

import communication.requests.AppointmentRequest;
import communication.requests.DeleteAppointmentRequest;
import communication.requests.PutAppointmentRequest;
import communication.responses.AppointmentResponse;
import communication.responses.BaseResponse;
import communication.responses.PutAppointmentResponse;
import server.DatabaseConnector;
import models.Appointment;
import models.RepetitionType;
import models.User;
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
	public static BaseResponse handleDeleteAppointment(DeleteAppointmentRequest request){
		BaseResponse res = new BaseResponse();
		Logger.logMsg(Logger.DEBUG, "Handeling deleteAppointment: " + request.toString());

		try {
			deleteAppointment(request.getAppointmentId());
			res.setSuccessful(true);
		} catch(SQLException e){
			res.setErrorMessage("Could not delete this appointment.");
			res.setSuccessful(false);
			Logger.logMsg(Logger.WARNING, "Error when deleting an appointment: " + e);
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
			Appointment appointment = request.getAppointment();
			try {
				int apppointmentId = addAppointment(appointment);
				request.getAppointment().setId(apppointmentId);
				response.setAppointment(request.getAppointment());
				response.setSuccessful(true);
				
				// For testing: Send a notification to the user who creates an appointment
				NotificationController.notify(requestUsername, appointment);
				
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
		while(resultSet.next()){
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
		getAppointment(appointment.getId()); // This will throw IllegalArgumentException if non-existent id

		String query = 
				"UPDATE Appointment " +
				"SET " +
				"start_date = '" + appointment.getStartTime().toString() + "'," +
				"end_date = '" + appointment.getEndTime().toString() + "'," + 
				"title = '" + appointment.getTitle() + "'," + 
				"description = '" + appointment.getDescription() + "'," + 
				"location = " + (appointment.getLocation() == null ? "null," : "'" + appointment.getDescription() + "',") + 
				"room_id = " + appointment.getRoomId() + ", " +
				"owner_username = '" + appointment.getOwnerUsername() + "' " +
				"WHERE appointment_id=" + appointment.getId() + ";";
		System.out.println(query);
		
		PreparedStatement statement = db.prepareStatement(query);
		statement.execute();		
	}
	
	private static int addAppointment(Appointment appointment) throws SQLException {
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
			
			query = String.format(
					"INSERT INTO UserAppointmentRelation(appointment_id, username) VALUES('%s', '%s')",
					appointmentId, appointment.getOwnerUsername());
			
			statement = db.prepareStatement(query);
			
			statement.execute();
		}
		else
			throw new SQLException("Unable to get id of generated object");
		return appointmentId;
	}
	
	private static ArrayList<Appointment> getAppointments(){
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
	
	private static Appointment getAppointment(int appointmentId) throws SQLException {
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
	
	private static Appointment parseResultSetToAppointment(ResultSet resultSet)
			throws SQLException {
		Appointment appointment = new Appointment();
		appointment.setId(resultSet.getInt("appointment_id"));
		appointment.setStartTime(resultSet.getString("start_date").substring(0, 16));
		appointment.setEndTime(resultSet.getString("end_date").substring(0, 16)); // TODO: Needs a string parser
		appointment.setTitle(resultSet.getString("title"));
		appointment.setDescription(resultSet.getString("description"));
		appointment.setRoomId(resultSet.getInt("room_id"));
		appointment.setOwnerUsername(resultSet.getString("owner_username"));
		return appointment;
	}
}

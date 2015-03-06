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
import communication.requests.CreateUserRequest;
import communication.requests.PutAppointmentRequest;
import communication.responses.AppointmentResponse;
import communication.responses.CreateUserResponse;
import communication.responses.PutAppointmentResponse;
import server.DatabaseConnector;
import models.Appointment;
import models.RepetitionType;
import models.Room;
import models.User;

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
	
	public static PutAppointmentResponse handlePutAppointmentRequest( String clientUsername,
			PutAppointmentRequest request) {
		PutAppointmentResponse response = new PutAppointmentResponse();
		if(clientUsername != request.getAppointment().getOwnerUsername()) {
			response.setSuccessful(false);
			response.setErrorMessage("You cannot change an appointment you do now own");
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

		String query = "";
		query += "SELECT * " +
		"FROM Appointment a, User u, UserAppointmentRelation au";
		query += " WHERE (";
		for(String username : usernames)
			query += "u.username = '" + username + "' OR ";
		query = query.substring(0, query.length()-4); //Strip last OR
		query += ") ";
		query += "AND a.appointment_id = au.appointment_id " +
		"AND u.username = au.username";
		
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
				"repetition_type = " + (appointment.getRepetitionType() == null ? "null," : "'" + appointment.getRepetitionType().toString() + "',") + // TODO: run update script on server database to make the repetition type models mattch
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
				"INSERT INTO Appointment(start_date, end_date, title, description, location, room_id, repetition_type, owner_username)" +
				"VALUES ( " +
				"'" + appointment.getStartTime().toString() + "'," +
				"'" + appointment.getEndTime().toString() + "'," + 
				"'" + appointment.getTitle() + "'," + 
				"'" + appointment.getDescription() + "'," + 
				(appointment.getLocation() == null ? "null," : "'" + appointment.getDescription() + "',") + 
				appointment.getRoomId() + ", " +
				(appointment.getRepetitionType() == null ? "null," : "'" + appointment.getRepetitionType().toString() + "',") + // TODO: run update script on server database to make the repetition type models mattch
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
		appointment.setStartTime(LocalDateTime.now().toString()); // TODO: Needs a string parser
		appointment.setEndTime(LocalDateTime.now().toString()); // TODO: Needs a string parser
		appointment.setTitle(resultSet.getString("title"));
		appointment.setDescription(resultSet.getString("description"));
		appointment.setRoomId(resultSet.getInt("room_id")); // TODO: get the correct room from db
		appointment.setRepetitionType(RepetitionType.fromString(resultSet.getString("repetition_type"))); // TODO: make this match the db value
		appointment.setOwnerUsername(resultSet.getString("owner_username"));
		return appointment;
	}
}

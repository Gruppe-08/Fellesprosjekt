package controllers;


import models.Appointment;
import models.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.sun.media.jfxmedia.logging.Logger;

import communication.requests.AuthenticationRequest;
import communication.requests.BusyCheckRequest;
import communication.requests.CreateUserRequest;
import communication.requests.GetUsersRequest;
import communication.requests.UpdateUserRequest;
import communication.responses.AuthenticationResponse;
import communication.responses.BaseResponse;
import communication.responses.BusyCheckResponse;
import communication.responses.CreateUserResponse;
import communication.responses.GetUsersResponse;
import communication.responses.UserResponse;
import server.DatabaseConnector;
import util.DateUtil;

public class UserController {
	
	public static AuthenticationResponse handleAuthenticationResponse(AuthenticationRequest request) {
		AuthenticationResponse response = new AuthenticationResponse();
		String hash = getHashForUser(request.getUsername());
		Boolean status = compareHashes(request.getPassword(), hash);
		response.setSuccessful(status);
		response.setUser(getUser(request.getUsername()));
		return response;
	}
	
	public static GetUsersResponse handleGetUsersResponse(GetUsersRequest request) {
		GetUsersResponse response = new GetUsersResponse();
		try {
			response.setUserList(getUsers());
			response.setSuccessful(true);
		}
		catch(SQLException e) {
			Logger.logMsg(Logger.ERROR, e.getMessage());
			response.setSuccessful(false);
		}
		
		return response;
	}
	
	public static CreateUserResponse handleCreateUserRequest(CreateUserRequest request) {
		CreateUserResponse response = new CreateUserResponse();
		try {
			createNewUser(request.getUser(), request.getPassword());
			response.setSuccessful(true);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			response.setSuccessful(false);
			response.setErrorMessage(e.getMessage());
		}
		return response;
	}
	
	public static GetUsersResponse handleGetUsersRequest(
			GetUsersRequest request){
		GetUsersResponse response = new GetUsersResponse();
		try {
			response.setUserList(getUsers());
			response.setSuccessful(true);
		}
		catch(Exception e){
			response.setSuccessful(false);
			response.setErrorMessage(e.getMessage());
		}
		return response;
	}
	
	public static BusyCheckResponse handleBusyCheck(BusyCheckRequest request) {
		BusyCheckResponse response = new BusyCheckResponse();
		
		for(String username : request.getUsernames()) {
			try {
				if(isUserBusy(request.getAppointment(), username)) {
					response.getUsernames().add(username);
				}
				
				response.setSuccessful(true);
			} catch (SQLException e) {
				response.setSuccessful(false);
			}
		}
		
		return response;
	}	
	
	public static BaseResponse handleUpdateUserRequest(UpdateUserRequest request) {
		BaseResponse res = new BaseResponse();
		
		try {
			if(request.isDeleteRequest()){
				String username = request.getUser().getUsername();
				deleteUser(username);
			} else {
				updateUser(request.getUser());
			}
			res.setSuccessful(true);
		} catch (SQLException e) {
			res.setSuccessful(false);
			res.setErrorMessage(e.getMessage());
			Logger.logMsg(Logger.ERROR, e.getMessage());
		}
		
		return res;
	}
	
	//Retrieves password hash of the specified user from the server
	private static String getHashForUser(String username) {
		String hash = null;
		Connection db = DatabaseConnector.getDB();
		try {
			Statement stm = db.createStatement();
			String query = String.format("SELECT password_hash FROM User WHERE username = '%s'", username);
			ResultSet result = stm.executeQuery(query);
			while(result.next()) {
				hash = result.getString("password_hash");
			}		
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return hash;
	}
	
	private static String hashString(String str, String algorithm) throws NoSuchAlgorithmException {		 
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(str.getBytes());
 
        byte byteData[] = md.digest();
 
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
	}
	
	private static Boolean compareHashes(String password, String storedHash) {
		try {
			String hashedPass = hashString(password, "SHA-1");
			if (hashedPass.equals(storedHash)) {			//The .equals method in the String class is not safe for timing attacks, 
				return true;								//though safeguarding against this is not a priority	
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static void createNewUser(User user, String password)
			throws IllegalArgumentException, IllegalStateException {
		Boolean exists = userAlreadyExists(user.getUsername());
		if (!exists) {
			try {
				password = hashString(password, "SHA-1");
				
				Connection db = DatabaseConnector.getDB();
				
				Statement stm = db.createStatement();
				
				String insertUser = String.format("INSERT INTO User (username, password_hash, firstname, lastname, admin) VALUES ('%s', '%s', '%s', '%s', '%s')",
						user.getUsername(), password, user.getFirstname(), user.getLastname(), Integer.toString(user.isAdmin() ? 1 : 0));
				stm.execute(insertUser);	
			} catch (SQLException e) {
				Logger.logMsg(Logger.ERROR, e.getMessage());
				throw new IllegalStateException("Internal error");
			} catch (NoSuchAlgorithmException e) {
				Logger.logMsg(Logger.ERROR, e.getMessage());
				throw new IllegalStateException("Internal error");
			}
		} else throw new IllegalArgumentException("Username taken");
	}
	
	private static Boolean userAlreadyExists(String username) {
		User user = UserController.getUser(username);
		if (user == null) {
			return false;
		}
		return true;
	}
	
	public static User getUser(String username) {
		User user = null;
		Connection db = DatabaseConnector.getDB();
		try {
			Statement stm = db.createStatement();
			String getUser = String.format("SELECT * FROM User WHERE username = '%s'", username);
			ResultSet rs = stm.executeQuery(getUser);
			rs.next();
			user = parseResultSetToUser(rs);
		}
		catch (SQLException e){
			System.out.println(e);
		}
		return user;
	}
	
	private static void updateUser(User user) throws SQLException {
		Connection db = DatabaseConnector.getDB();
		
		String admin = Integer.toString((user.isAdmin() ? 1 : 0)); // Convert from boolean to 1/0

		Statement stm = db.createStatement();
		String updateUser = String.format("UPDATE User SET firstname = '%s', lastname = '%s', admin='%s' WHERE username = '%s'",
				user.getFirstname(), 
				user.getLastname(),
				admin,
				user.getUsername()
				);
		
		stm.execute(updateUser);
	}
	
	private static void deleteUser(String username) throws SQLException {
		Connection db = DatabaseConnector.getDB();
		Statement stm = db.createStatement();
		String deleteUser = String.format("DELETE FROM User WHERE username='%s'", username);
		stm.execute(deleteUser);
	}

	private static boolean isUserBusy(Appointment a, String username) throws SQLException {
		Connection db = DatabaseConnector.getDB();
		String query = 
				"Select * FROM Appointment a, UserAppointmentRelation ua, User u "
				+ "WHERE a.appointment_id = ua.appointment_id AND "
				+ "u.username = ua.username AND u.username = '" + username + "' AND ("
				+ "a.start_date BETWEEN '" + a.getStartTime() + "' AND '"+ a.getEndTime()
				+ "' OR a.end_date BETWEEN '" + a.getStartTime() + "' AND '"+ a.getEndTime() + "')";
		ResultSet res = db.createStatement().executeQuery(query);
		
		return res.next(); //Returns false if there was no matching appointments
	}
	
	private static ArrayList<User> getUsers() throws SQLException {
		Connection db = DatabaseConnector.getDB();
		ArrayList<User> users = new ArrayList<User>();
		Statement stm = db.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * FROM User");
		while(rs.next()) {
			users.add(parseResultSetToUser(rs));
		}
		return users;
	}
	
	private static User parseResultSetToUser(ResultSet rs) throws SQLException{
		User user = new User();
		user.setUsername(rs.getString("username"));
		user.setFirstname(rs.getString("firstname"));
		user.setLastname(rs.getString("lastname"));
		user.setIsAdmin((rs.getInt("admin") == 1));
		return user;
	}

	

}

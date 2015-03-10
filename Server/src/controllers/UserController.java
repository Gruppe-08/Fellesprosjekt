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

import com.sun.glass.ui.Window.Level;
import com.sun.media.jfxmedia.logging.Logger;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import communication.requests.AuthenticationRequest;
import communication.requests.CreateUserRequest;
import communication.requests.GetUsersRequest;
import communication.responses.AuthenticationResponse;
import communication.responses.CreateUserResponse;
import communication.responses.UserResponse;
import server.DatabaseConnector;

public class UserController {
	
	public static AuthenticationResponse handleAuthenticationResponse(AuthenticationRequest request) {
		AuthenticationResponse response = new AuthenticationResponse();
		String hash = getHashForUser(request.getUsername());
		Boolean status = compareHashes(request.getPassword(), hash);
		response.setSuccessful(status);
		response.setUser(getUser(request.getUsername()));
		return response;
	}
	
	public static UserResponse handleGetUsersResponse(GetUsersRequest request) {
		UserResponse response = new UserResponse();
		try {
			ArrayList<User> users = getUsers();
			for(User user : users) {
				response.addUser(user);
			}
			response.setSuccessful(true);
		}
		catch(SQLException e) {
			Logger.logMsg(Logger.ERROR, e.getMessage());
			response.setSuccessful(false);
		}
		
		return response;
	}
	
	public static CreateUserResponse handleCreateUserRequest(
			CreateUserRequest request) {
		CreateUserResponse response = new CreateUserResponse();
		try {
			createNewUser(request.getUser(), request.getPassword());
			response.setSuccessful(true);
		}
		catch(Exception e) {
			response.setSuccessful(false);
			response.setErrorMessage(e.getMessage());
		}
		return response;
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
				
				String insertUser = String.format("INSERT INTO User(username, password_hash, firstname, lastname) VALUES ('%s', '%s', '%s', '%s')",
						user.getUsername(), password, user.getFirstname(), user.getLastname());
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
		if (user.getUsername() == null) {
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
	
	private static void updateUser(User user) {
		Connection db = DatabaseConnector.getDB();
		try {
			Statement stm = db.createStatement();
			String updateUser = String.format("UPDATE User SET username = '%s', firstname = '%s', lastname = '%s' WHERE username = '%s'",
					user.getFirstname(), 
					user.getLastname(), 
					user.getUsername());
			
			stm.execute(updateUser);
		}
		catch (SQLException e){
			System.out.println(e);
		}
	}
	
	private static void deleteUser(String username) {
		Connection db = DatabaseConnector.getDB();
		try {
			Statement stm = db.createStatement();
			String deleteUser = String.format("DELETE FROM User WHERE username='%s'", username);
			stm.execute(deleteUser);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	private static ArrayList<User> getUsers() throws SQLException {
		Connection db = DatabaseConnector.getDB();
		ArrayList<User> users = new ArrayList<User>();
		Statement stm = db.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * FROM User WHERE 1");
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
		return user;
	}	
}

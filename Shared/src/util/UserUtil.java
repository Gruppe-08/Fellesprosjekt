package util;

import models.User;

public class UserUtil {
	public static Boolean isValidUser(User user){
		// TODO: Implement a check to server to verify that the username is not taken.
		return !(user.getFirstname().equals("") && user.getLastname().equals("") && user.getUsername().equals(""));
	}
	
	public static Boolean isValidPassword(String password){
		return password.length() >= 6;
	}
	
	public static Boolean isMatchingPasswords(String p1, String p2){
		return p1.equals(p2);
	}

}

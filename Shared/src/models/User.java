package models;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;

public class User {
	private String username;
	private String firstname;
	private String lastname;
	private Boolean isAdmin;
	private ArrayList<Integer> attendingAppointments;
	
	
	public ArrayList<Integer> getAttendingAppointments() {
		return attendingAppointments;
	}

	public void setAttendingAppointments(ArrayList<Integer> attendingAppointments) {
		this.attendingAppointments = attendingAppointments;
	}

	public User(String username, String firstname, String lastname, Boolean isAdmin) {
		setUsername(username);
		setFirstname(firstname);
		setLastname(lastname);
		setIsAdmin(isAdmin);
	}
	
	public User() {	}
	
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname() {
		return lastname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}	
	
	public void setIsAdmin(Boolean isAdmin){
		this.isAdmin = isAdmin;
	}
	
	public Boolean isAdmin(){
		return isAdmin;
	}

}

package models;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;

public class User {
	
	public ArrayList<Integer> getAttendingAppointments() {
		return attendingAppointments;
	}

	public void setAttendingAppointments(ArrayList<Integer> attendingAppointments) {
		this.attendingAppointments = attendingAppointments;
	}

	public User(String username, String firstname, String lastname) {
		setUsername(username);
		setFirstname(firstname);
		setLastname(lastname);
	}
	
	public User() {
		
	}
	
	private Property<String> username = new SimpleStringProperty();
	private Property<String> firstname = new SimpleStringProperty();
	private Property<String> lastname = new SimpleStringProperty();
	private ArrayList<Integer> attendingAppointments = new ArrayList<Integer>();
	
	
	public String getUsername() {
		return username.getValue();
	}
	
	public void setUsername(String username) {
		this.username.setValue(username);
	}
	
	public String getFirstname() {
		return firstname.getValue();
	}
	
	public void setFirstname(String firstname) {
		this.firstname.setValue(firstname);
	}
	
	public String getLastname() {
		return lastname.getValue();
	}
	
	public void setLastname(String lastname) {
		this.lastname.setValue(lastname);
	}	

}

package models;

import java.util.ArrayList;

public class User {
	private String username;
	private String  firstname;
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
		attendingAppointments = new ArrayList<>();
	}
	
	public User() {
		username = "";
		firstname = "";
		lastname = "";
		isAdmin = false;
		attendingAppointments = new ArrayList<>();
	}
	
	
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
	
	@Override
	public String toString(){
		return this.firstname + " " + this.lastname;
	}

}

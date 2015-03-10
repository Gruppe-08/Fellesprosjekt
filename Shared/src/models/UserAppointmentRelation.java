package models;

public class UserAppointmentRelation {
	private String username;
	private int appointmentID;
	
	UserAppointmentRelation(){}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getAppointmentID() {
		return appointmentID;
	}
	public void setAppointmentID(int appointmentID) {
		this.appointmentID = appointmentID;
	}
}

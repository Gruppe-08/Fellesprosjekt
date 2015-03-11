package communication.requests;

import java.util.ArrayList;

import models.Appointment;

public class BusyCheckRequest {
	Appointment appointment;
	private ArrayList<String> usernames = new ArrayList<String>();
	
	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	public BusyCheckRequest() {
		
	}

	public ArrayList<String> getUsernames() {
		return usernames;
	}

	public void setUsernames(ArrayList<String> usernames) {
		this.usernames = usernames;
	}
}

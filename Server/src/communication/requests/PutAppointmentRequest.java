package communication.requests;

import models.Appointment;

public class PutAppointmentRequest {
	Appointment appointment;
	boolean isNewAppointment;

	public boolean isNewAppointment() {
		return isNewAppointment;
	}

	public void setNewAppointment(boolean isNewAppointment) {
		this.isNewAppointment = isNewAppointment;
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
	
	
}

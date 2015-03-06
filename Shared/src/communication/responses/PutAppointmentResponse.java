package communication.responses;

import models.Appointment;

public class PutAppointmentResponse extends BaseResponse {
	private Appointment appointment;
	private boolean isNew;
	
	public PutAppointmentResponse() {}
	
	public PutAppointmentResponse(Appointment appointment) {
		setAppointment(appointment);
	}
	public Appointment getAppointment() {
		return appointment;
	}
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
}
